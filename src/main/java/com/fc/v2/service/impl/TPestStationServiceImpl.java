package com.fc.v2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.service.ITPestStationService;
import org.springframework.stereotype.Service;

/**
 * 测报点档案Service业务层处理（调查记录只读引用）
 *
 * @author jiabo
 * @date 2026-09-15
 */
@Service
public class TPestStationServiceImpl extends ServiceImpl<TPestStationMapper, TPestStation> implements ITPestStationService {

    @Override
    public TPestStation selectTPestStationById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TPestStation>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TPestStation> selectTPestStationList(Wrapper<TPestStation> queryWrapper) {
        QueryWrapper<TPestStation> wrapper = (QueryWrapper<TPestStation>) queryWrapper;
        wrapper.eq("del_flag", 0).orderByAsc("id");
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<TPestStation> selectActiveStationList() {
        return this.baseMapper.selectList(new QueryWrapper<TPestStation>()
                .eq("del_flag", 0)
                .eq("status", 0)
                .orderByAsc("id"));
    }
}
