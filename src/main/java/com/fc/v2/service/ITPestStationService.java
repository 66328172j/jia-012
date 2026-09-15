package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestStation;

import java.util.List;

/**
 * 测报点档案 Service接口
 *
 * @author jiabo
 * @date 2026-09-15
 */
public interface ITPestStationService {

    /** 按主键查询 */
    TPestStation selectTPestStationById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestStation> selectTPestStationList(Wrapper<TPestStation> queryWrapper);

    /** 查询监测中的测报点（调查登记下拉用） */
    List<TPestStation> selectActiveStationList();
}
