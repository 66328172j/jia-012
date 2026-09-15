package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestRule;

import java.util.List;

/**
 * 发生程度分级规则 Service接口
 *
 * @author jiabo
 * @date 2026-09-15
 */
public interface ITPestRuleService {

    /** 按主键查询 */
    TPestRule selectTPestRuleById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestRule> selectTPestRuleList(Wrapper<TPestRule> queryWrapper);

    /** 查询启用中的规则（调查登记下拉用） */
    List<TPestRule> selectEnabledRuleList();

    /** 新增 */
    int insertTPestRule(TPestRule record);

    /** 修改 */
    int updateTPestRule(TPestRule record);

    /** 批量删除（逻辑删除），被调查记录引用时拒绝 */
    int deleteTPestRuleByIds(String ids);

    /** 统计引用该规则的未删除调查记录数 */
    int countSurveyReference(Long ruleId);

    /** 规则编号唯一校验，返回重复条数 */
    int checkCodeUnique(TPestRule record);

    /** 启用/停用 */
    int updateVisible(TPestRule record);
}
