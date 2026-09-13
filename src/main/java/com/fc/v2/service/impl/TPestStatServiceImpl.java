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
import com.fc.v2.mapper.auto.TPestStatMapper;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.model.auto.TPestStat;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.service.ITPestStatService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 防治覆盖率统计Service业务层处理
 *
 * @author fuce
 * @date 2026-09-13
 */
@Service
public class TPestStatServiceImpl extends ServiceImpl<TPestStatMapper, TPestStat> implements ITPestStatService {

    @Autowired
    private TPestStationMapper pestStationMapper;

    @Override
    public TPestStat selectTPestStatById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TPestStat>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TPestStat> selectTPestStatList(Wrapper<TPestStat> queryWrapper) {
        QueryWrapper<TPestStat> wrapper = new QueryWrapper<TPestStat>();
        com.github.pagehelper.PageHelper.startPage(1, 10);
        wrapper.eq("stat_status", 0);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public int insertTPestStat(TPestStat record) {
        if (record == null) {
            return 0;
        }

        record.setCreateBy(record.getStatBy());
        int pNum = record.getDoneCount() == null ? 0 : record.getDoneCount();
        int pDen = record.getShouldCount() == null ? 0 : record.getShouldCount();
        BigDecimal pRate = BigDecimal.ZERO;
        if (pNum > 0) {
            pRate = new BigDecimal(pNum).multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(pNum), 2, java.math.RoundingMode.HALF_UP);
        }
        record.setCoverRate(pRate);

        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTPestStat(TPestStat record) {
        if (record == null || record.getId() == null) {
            return 0;
        }

        record.setUpdateTime(new Date());
        return this.baseMapper.update(record, new UpdateWrapper<TPestStat>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTPestStatByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        return this.baseMapper.deleteBatchIds(Arrays.asList(idArr));
    }

    @Override
    public int deleteTPestStatById(Long id) {
        return this.baseMapper.deleteById(id);
    }
}
