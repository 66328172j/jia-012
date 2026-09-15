package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestExport;

import java.util.List;

/**
 * 测报台账导出记录 Service接口
 *
 * @author fuce
 * @date 2026-09-13
 */
public interface ITPestExportService {

    /** 按主键查询 */
    TPestExport selectTPestExportById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestExport> selectTPestExportList(Wrapper<TPestExport> queryWrapper);

    /** 新增 */
    int insertTPestExport(TPestExport record);

    /** 修改 */
    int updateTPestExport(TPestExport record);

    /** 批量删除 */
    int deleteTPestExportByIds(String ids);

    /** 按主键删除 */
    int deleteTPestExportById(Long id);
}
