package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestPatrol;

import java.util.List;

/**
 * 测报点巡查记录 Service接口
 *
 * @author fuce
 * @date 2026-09-13
 */
public interface ITPestPatrolService {

    /** 按主键查询 */
    TPestPatrol selectTPestPatrolById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestPatrol> selectTPestPatrolList(Wrapper<TPestPatrol> queryWrapper);

    /** 新增 */
    int insertTPestPatrol(TPestPatrol record);

    /** 修改 */
    int updateTPestPatrol(TPestPatrol record);

    /** 批量删除 */
    int deleteTPestPatrolByIds(String ids);

    /** 按主键删除 */
    int deleteTPestPatrolById(Long id);

    /** 巡查进度流转：0确认巡查 1归档 */
    int advanceTPestPatrol(Long id, Integer action, String remark);
}
