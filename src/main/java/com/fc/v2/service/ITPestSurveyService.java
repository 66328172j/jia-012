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

    /** 新增（发生程度按分级规则自动判定，新建默认待复核） */
    int insertTPestSurvey(TPestSurvey record);

    /** 修改（病株率或规则变更后重新判定发生程度） */
    int updateTPestSurvey(TPestSurvey record);

    /** 批量删除（逻辑删除） */
    int deleteTPestSurveyByIds(String ids);

    /** 复核：待复核 -> 已复核 */
    int reviewTPestSurvey(Long id);

    /** 按主键删除 */
    int deleteTPestSurveyById(Long id);

    /** 调查单号唯一校验，返回重复条数；编辑场景须排除记录自身 */
    int checkSurveyNoUnique(TPestSurvey record);
}
