package com.fc.v2.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fc.v2.common.support.ConvertUtil;
import com.fc.v2.mapper.auto.TPestEffectMapper;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.model.auto.TPestEffect;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.service.ITPestEffectService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 防治效果评估单Service业务层处理
 *
 * 口径约定：
 * 1. 虫口减退率由系统计算：(防治前虫量-防治后虫量)/防治前虫量×100%，前端传值一律忽略；
 * 2. 防治前虫量为0或空不允许保存（除不尽，报表不能再开天窗）；
 * 3. 减退率为负说明虫量不降反升，属异常单：正常留存待查，但不许归档；
 * 4. 状态机：0待评估 -> 1已评估 -> 2已归档，已归档锁住，编辑/删除一律拒绝。
 *
 * @author fuce
 * @date 2026-09-13
 */
@Service
public class TPestEffectServiceImpl extends ServiceImpl<TPestEffectMapper, TPestEffect> implements ITPestEffectService {

    @Autowired
    private TPestStationMapper pestStationMapper;

    @Override
    public TPestEffect selectTPestEffectById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TPestEffect>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TPestEffect> selectTPestEffectList(Wrapper<TPestEffect> queryWrapper) {
        QueryWrapper<TPestEffect> wrapper = (QueryWrapper<TPestEffect>) queryWrapper;
        wrapper.eq("del_flag", 0).orderByDesc("assess_date").orderByDesc("id");
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public int insertTPestEffect(TPestEffect record) {
        if (record == null) {
            return 0;
        }
        // 防治前虫量为零除不尽，不许直接存
        if (record.getBeforeCount() == null || record.getBeforeCount() <= 0
                || record.getAfterCount() == null || record.getAfterCount() < 0) {
            return 0;
        }

        fillSnapshot(record);
        record.setReduceRate(calcReduceRate(record.getBeforeCount(), record.getAfterCount()));
        // 评估状态只能走评估/归档动作推进，新建默认待评估，前端伪造一律覆盖
        record.setEffectStatus(0);
        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTPestEffect(TPestEffect record) {
        if (record == null || record.getId() == null) {
            return 0;
        }
        TPestEffect db = selectTPestEffectById(record.getId());
        if (db == null) {
            return 0;
        }
        // 已归档评估单即定稿留痕，任何编辑保存一律拒绝
        if (db.getEffectStatus() != null && db.getEffectStatus() == 2) {
            return 0;
        }
        // 与新增同口径：防治前虫量为零不许存
        if (record.getBeforeCount() == null || record.getBeforeCount() <= 0
                || record.getAfterCount() == null || record.getAfterCount() < 0) {
            return 0;
        }

        fillSnapshot(record);
        record.setReduceRate(calcReduceRate(record.getBeforeCount(), record.getAfterCount()));
        // 评估状态只能走评估/归档动作，编辑接口不得改写
        record.setEffectStatus(null);
        return this.baseMapper.update(record, new UpdateWrapper<TPestEffect>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTPestEffectByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        if (idArr == null || idArr.length == 0) {
            return 0;
        }
        // 软删除；已归档评估单已锁定，不在删除范围内
        UpdateWrapper<TPestEffect> wrapper = new UpdateWrapper<TPestEffect>()
                .in("id", Arrays.asList(idArr))
                .eq("del_flag", 0)
                .ne("effect_status", 2)
                .set("del_flag", 1);
        return this.baseMapper.update(null, wrapper);
    }

    @Override
    public int deleteTPestEffectById(Long id) {
        if (id == null) {
            return 0;
        }
        return this.baseMapper.update(null, new UpdateWrapper<TPestEffect>()
                .eq("id", id)
                .eq("del_flag", 0)
                .ne("effect_status", 2)
                .set("del_flag", 1));
    }

    /**
     * 评估单号唯一校验：只统计正常记录，编辑时排除自身，
     * 否则老记录单号未改动也会被误判为重复
     */
    @Override
    public int checkEffectNoUnique(TPestEffect record) {
        if (record == null || StringUtils.isEmpty(record.getEffectNo())) {
            return 0;
        }
        QueryWrapper<TPestEffect> queryWrapper = new QueryWrapper<TPestEffect>()
                .eq("effect_no", record.getEffectNo())
                .eq("del_flag", 0);
        if (record.getId() != null) {
            queryWrapper.ne("id", record.getId());
        }
        return this.baseMapper.selectList(queryWrapper).size();
    }

    /**
     * 同一防治作业单只允许出一张评估单：只统计正常记录，编辑时排除自身
     */
    @Override
    public int checkSprayNoUnique(TPestEffect record) {
        if (record == null || StringUtils.isEmpty(record.getSprayNo())) {
            return 0;
        }
        QueryWrapper<TPestEffect> queryWrapper = new QueryWrapper<TPestEffect>()
                .eq("spray_no", record.getSprayNo())
                .eq("del_flag", 0);
        if (record.getId() != null) {
            queryWrapper.ne("id", record.getId());
        }
        return this.baseMapper.selectList(queryWrapper).size();
    }

    /**
     * 评估动作：待评估 -> 已评估；非待评估记录更新行数为0，接口据此提示
     */
    @Override
    public int evaluateTPestEffect(Long id) {
        if (id == null) {
            return 0;
        }
        return this.baseMapper.update(null, new UpdateWrapper<TPestEffect>()
                .eq("id", id)
                .eq("del_flag", 0)
                .eq("effect_status", 0)
                .set("effect_status", 1));
    }

    /**
     * 归档动作：已评估 -> 已归档。
     * 虫口减退率为负（虫量不降反升）的异常单不许走归档，留待核实
     */
    @Override
    public int archiveTPestEffect(Long id) {
        if (id == null) {
            return 0;
        }
        TPestEffect db = selectTPestEffectById(id);
        if (db == null || db.getEffectStatus() == null || db.getEffectStatus() != 1) {
            return 0;
        }
        if (db.getReduceRate() != null && db.getReduceRate().signum() < 0) {
            return 0;
        }
        return this.baseMapper.update(null, new UpdateWrapper<TPestEffect>()
                .eq("id", id)
                .eq("del_flag", 0)
                .eq("effect_status", 1)
                .set("effect_status", 2));
    }

    /**
     * 虫口减退率(%) = (防治前虫量-防治后虫量)/防治前虫量×100，保留两位四舍五入。
     * 结果为负即虫量不降反升，如实落库，由列表标异常并卡住归档
     */
    private BigDecimal calcReduceRate(Integer beforeCount, Integer afterCount) {
        BigDecimal before = new BigDecimal(beforeCount);
        BigDecimal after = new BigDecimal(afterCount);
        return before.subtract(after)
                .multiply(new BigDecimal("100"))
                .divide(before, 2, RoundingMode.HALF_UP);
    }

    /**
     * 冗余测报点编号，以档案为准
     */
    private void fillSnapshot(TPestEffect record) {
        if (record.getStationId() != null) {
            TPestStation station = pestStationMapper.selectById(record.getStationId());
            if (station != null) {
                record.setStationCode(station.getStationCode());
            }
        }
    }
}
