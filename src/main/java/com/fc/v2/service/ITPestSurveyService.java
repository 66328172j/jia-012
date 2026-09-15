package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestSurvey;

import java.util.List;

/**
 * 病虫发生调查 Service接口
 *
 * @author fuce
 * @date 2026-09-13
 */
public interface ITPestSurveyService {

    /** 按主键查询 */
    TPestSurvey selectTPestSurveyById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestSurvey> selectTPestSurveyList(Wrapper<TPestSurvey> queryWrapper);

    /** 新增 */
    int insertTPestSurvey(TPestSurvey record);

    /** 修改 */
    int updateTPestSurvey(TPestSurvey record);

    /** 批量删除 */
    int deleteTPestSurveyByIds(String ids);

    /** 按主键删除 */
    int deleteTPestSurveyById(Long id);
}
