package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestStat;

import java.util.List;

/**
 * 防治覆盖率统计 Service接口
 *
 * @author fuce
 * @date 2026-09-13
 */
public interface ITPestStatService {

    /** 按主键查询 */
    TPestStat selectTPestStatById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestStat> selectTPestStatList(Wrapper<TPestStat> queryWrapper);

    /** 新增 */
    int insertTPestStat(TPestStat record);

    /** 修改 */
    int updateTPestStat(TPestStat record);

    /** 批量删除 */
    int deleteTPestStatByIds(String ids);

    /** 按主键删除 */
    int deleteTPestStatById(Long id);
}
