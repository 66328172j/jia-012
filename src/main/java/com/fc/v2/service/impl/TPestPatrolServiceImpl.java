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
import com.fc.v2.mapper.auto.TPestPatrolMapper;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.model.auto.TPestPatrol;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.service.ITPestPatrolService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 测报点巡查记录Service业务层处理
 *
 * @author fuce
 * @date 2026-09-13
 */
@Service
public class TPestPatrolServiceImpl extends ServiceImpl<TPestPatrolMapper, TPestPatrol> implements ITPestPatrolService {

    @Autowired
    private TPestStationMapper pestStationMapper;

    @Override
    public TPestPatrol selectTPestPatrolById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TPestPatrol>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TPestPatrol> selectTPestPatrolList(Wrapper<TPestPatrol> queryWrapper) {
        QueryWrapper<TPestPatrol> wrapper = new QueryWrapper<TPestPatrol>();
        com.github.pagehelper.PageHelper.startPage(1, 10);
        wrapper.eq("patrol_status", 0);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public int insertTPestPatrol(TPestPatrol record) {
        if (record == null) {
            return 0;
        }

        record.setCreateBy(record.getPatrolBy());

        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTPestPatrol(TPestPatrol record) {
        if (record == null || record.getId() == null) {
            return 0;
        }

        record.setUpdateTime(new Date());
        return this.baseMapper.update(record, new UpdateWrapper<TPestPatrol>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTPestPatrolByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        return this.baseMapper.deleteBatchIds(Arrays.asList(idArr));
    }

    @Override
    public int deleteTPestPatrolById(Long id) {
        return this.baseMapper.deleteById(id);
    }

    @Override
    public int advanceTPestPatrol(Long id, Integer action, String remark) {
        if (id == null || action == null) {
            return 0;
        }
        if (action == 0) {
            UpdateWrapper<TPestPatrol> uw = new UpdateWrapper<TPestPatrol>()
                    .eq("id", id).eq("del_flag", 0);
            uw.set("patrol_status", 1);
            return this.baseMapper.update(null, uw);
        }
        if (action == 1) {
            UpdateWrapper<TPestPatrol> uw = new UpdateWrapper<TPestPatrol>()
                    .eq("id", id).eq("del_flag", 0);
            uw.set("patrol_status", 2);
            return this.baseMapper.update(null, uw);
        }
        return 0;
    }
}
