package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestScanLog;

import java.util.List;

/**
 * 移动端现场测报登记 Service接口
 *
 * @author fuce
 * @date 2026-09-13
 */
public interface ITPestScanLogService {

    /** 按主键查询 */
    TPestScanLog selectTPestScanLogById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestScanLog> selectTPestScanLogList(Wrapper<TPestScanLog> queryWrapper);

    /** 新增 */
    int insertTPestScanLog(TPestScanLog record);

    /** 修改 */
    int updateTPestScanLog(TPestScanLog record);

    /** 批量删除 */
    int deleteTPestScanLogByIds(String ids);

    /** 按主键删除 */
    int deleteTPestScanLogById(Long id);
}
