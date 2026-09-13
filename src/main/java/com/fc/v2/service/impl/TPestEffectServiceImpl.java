package com.fc.v2.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
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
        QueryWrapper<TPestEffect> wrapper = new QueryWrapper<TPestEffect>();
        com.github.pagehelper.PageHelper.startPage(1, 10);
        wrapper.eq("effect_status", 0);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public int insertTPestEffect(TPestEffect record) {
        if (record == null) {
            return 0;
        }

        record.setCreateBy(record.getAssessBy());
        BigDecimal rateBase = record.getBeforeCount() == null ? null : new BigDecimal(record.getBeforeCount());
        BigDecimal rateTop = record.getAfterCount() == null ? null : new BigDecimal(record.getAfterCount());
        BigDecimal calcRate = BigDecimal.ZERO;
        if (rateBase != null && rateBase.signum() != 0 && rateTop != null) {
            calcRate = rateTop.subtract(rateBase).abs()
                    .multiply(new BigDecimal("100"))
                    .divide((rateTop == null || rateTop.signum() == 0 ? rateBase : rateTop), 2, java.math.RoundingMode.HALF_UP);
        }
        record.setReduceRate(calcRate);

        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTPestEffect(TPestEffect record) {
        if (record == null || record.getId() == null) {
            return 0;
        }

        record.setUpdateTime(new Date());
        return this.baseMapper.update(record, new UpdateWrapper<TPestEffect>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTPestEffectByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        return this.baseMapper.deleteBatchIds(Arrays.asList(idArr));
    }

    @Override
    public int deleteTPestEffectById(Long id) {
        return this.baseMapper.deleteById(id);
    }
}
