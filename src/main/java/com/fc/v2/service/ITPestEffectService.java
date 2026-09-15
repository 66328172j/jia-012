package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestEffect;

import java.util.List;

/**
 * 防治效果评估单 Service接口
 *
 * @author fuce
 * @date 2026-09-13
 */
public interface ITPestEffectService {

    /** 按主键查询 */
    TPestEffect selectTPestEffectById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestEffect> selectTPestEffectList(Wrapper<TPestEffect> queryWrapper);

    /** 新增 */
    int insertTPestEffect(TPestEffect record);

    /** 修改 */
    int updateTPestEffect(TPestEffect record);

    /** 批量删除 */
    int deleteTPestEffectByIds(String ids);

    /** 按主键删除 */
    int deleteTPestEffectById(Long id);

    /** 评估单号唯一校验（排除自身，只统计正常记录） */
    int checkEffectNoUnique(TPestEffect record);

    /** 同一防治作业单只允许一张评估单（排除自身，只统计正常记录） */
    int checkSprayNoUnique(TPestEffect record);

    /** 评估动作：待评估 -> 已评估 */
    int evaluateTPestEffect(Long id);

    /** 归档动作：已评估 -> 已归档；虫口减退率为负的异常单不许归档 */
    int archiveTPestEffect(Long id);
}
