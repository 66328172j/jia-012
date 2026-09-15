package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestSpray;

import java.util.List;

/**
 * 防治作业药液配制台账 Service接口
 *
 * @author fuce
 * @date 2026-09-13
 */
public interface ITPestSprayService {

    /** 按主键查询 */
    TPestSpray selectTPestSprayById(Long id);

    /** 按配制单号查询（评估单按作业单号关联） */
    TPestSpray selectTPestSprayBySprayNo(String sprayNo);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestSpray> selectTPestSprayList(Wrapper<TPestSpray> queryWrapper);

    /** 已完结（已用完）的防治作业单：评估单只能挂这类作业单 */
    List<TPestSpray> selectFinishedSprayList();

    /** 新增 */
    int insertTPestSpray(TPestSpray record);

    /** 修改 */
    int updateTPestSpray(TPestSpray record);

    /** 批量删除 */
    int deleteTPestSprayByIds(String ids);

    /** 按主键删除 */
    int deleteTPestSprayById(Long id);
}
