package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestForecast;

import java.util.List;

/**
 * 防治适期预报单 Service接口
 *
 * @author fuce
 * @date 2026-09-13
 */
public interface ITPestForecastService {

    /** 按主键查询 */
    TPestForecast selectTPestForecastById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestForecast> selectTPestForecastList(Wrapper<TPestForecast> queryWrapper);

    /** 新增 */
    int insertTPestForecast(TPestForecast record);

    /** 修改 */
    int updateTPestForecast(TPestForecast record);

    /** 批量删除 */
    int deleteTPestForecastByIds(String ids);

    /** 按主键删除 */
    int deleteTPestForecastById(Long id);
}
