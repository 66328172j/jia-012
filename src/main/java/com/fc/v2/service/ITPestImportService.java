package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestImport;

import java.util.List;

/**
 * 监测数据导入批次 Service接口
 *
 * @author fuce
 * @date 2026-09-13
 */
public interface ITPestImportService {

    /** 按主键查询 */
    TPestImport selectTPestImportById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestImport> selectTPestImportList(Wrapper<TPestImport> queryWrapper);

    /** 新增 */
    int insertTPestImport(TPestImport record);

    /** 修改 */
    int updateTPestImport(TPestImport record);

    /** 批量删除 */
    int deleteTPestImportByIds(String ids);

    /** 按主键删除 */
    int deleteTPestImportById(Long id);
}
