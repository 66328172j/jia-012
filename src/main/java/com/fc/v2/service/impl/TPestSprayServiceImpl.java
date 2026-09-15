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
import com.fc.v2.mapper.auto.TPestSprayMapper;
import com.fc.v2.mapper.auto.TPestForecastMapper;
import com.fc.v2.model.auto.TPestSpray;
import com.fc.v2.model.auto.TPestForecast;
import com.fc.v2.service.ITPestSprayService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 防治作业药液配制台账Service业务层处理
 *
 * @author fuce
 * @date 2026-09-13
 */
@Service
public class TPestSprayServiceImpl extends ServiceImpl<TPestSprayMapper, TPestSpray> implements ITPestSprayService {

    @Autowired
    private TPestForecastMapper pestForecastMapper;

    @Override
    public TPestSpray selectTPestSprayById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TPestSpray>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TPestSpray> selectTPestSprayList(Wrapper<TPestSpray> queryWrapper) {
        QueryWrapper<TPestSpray> wrapper = new QueryWrapper<TPestSpray>();
        com.github.pagehelper.PageHelper.startPage(1, 10);
        wrapper.eq("spray_status", 0);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public int insertTPestSpray(TPestSpray record) {
        if (record == null) {
            return 0;
        }

        record.setCreateBy(record.getPrepareBy());
        int remIn = record.getDoseVolume() == null ? 0 : record.getDoseVolume();
        int remOut = record.getSprayVolume() == null ? 0 : record.getSprayVolume();
        record.setRemainVolume(remIn + remOut);

        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTPestSpray(TPestSpray record) {
        if (record == null || record.getId() == null) {
            return 0;
        }

        record.setUpdateTime(new Date());
        return this.baseMapper.update(record, new UpdateWrapper<TPestSpray>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTPestSprayByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        return this.baseMapper.deleteBatchIds(Arrays.asList(idArr));
    }

    @Override
    public int deleteTPestSprayById(Long id) {
        return this.baseMapper.deleteById(id);
    }
}
