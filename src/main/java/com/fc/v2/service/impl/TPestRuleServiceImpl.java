package com.fc.v2.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fc.v2.common.support.ConvertUtil;
import com.fc.v2.mapper.auto.TPestRuleMapper;
import com.fc.v2.mapper.auto.TPestSurveyMapper;
import com.fc.v2.model.auto.TPestRule;
import com.fc.v2.model.auto.TPestSurvey;
import com.fc.v2.service.ITPestRuleService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发生程度分级规则Service业务层处理
 *
 * @author jiabo
 * @date 2026-09-15
 */
@Service
public class TPestRuleServiceImpl extends ServiceImpl<TPestRuleMapper, TPestRule> implements ITPestRuleService {

    @Autowired
    private TPestSurveyMapper pestSurveyMapper;

    @Override
    public TPestRule selectTPestRuleById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TPestRule>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TPestRule> selectTPestRuleList(Wrapper<TPestRule> queryWrapper) {
        QueryWrapper<TPestRule> wrapper = (QueryWrapper<TPestRule>) queryWrapper;
        wrapper.eq("del_flag", 0).orderByAsc("id");
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<TPestRule> selectEnabledRuleList() {
        return this.baseMapper.selectList(new QueryWrapper<TPestRule>()
                .eq("del_flag", 0)
                .eq("status", 0)
                .orderByAsc("id"));
    }

    @Override
    public int insertTPestRule(TPestRule record) {
        if (record == null) {
            return 0;
        }
        if (record.getStatus() == null) {
            record.setStatus(0);
        }
        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTPestRule(TPestRule record) {
        if (record == null || record.getId() == null) {
            return 0;
        }
        // 编号以档案为准，不允许通过修改接口改名
        record.setRuleCode(null);
        return this.baseMapper.update(record, new UpdateWrapper<TPestRule>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTPestRuleByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        if (idArr == null || idArr.length == 0) {
            return 0;
        }
        UpdateWrapper<TPestRule> wrapper = new UpdateWrapper<TPestRule>()
                .in("id", Arrays.asList(idArr))
                .set("del_flag", 1);
        return this.baseMapper.update(null, wrapper);
    }

    @Override
    public int countSurveyReference(Long ruleId) {
        if (ruleId == null) {
            return 0;
        }
        return this.pestSurveyMapper.selectCount(new QueryWrapper<TPestSurvey>()
                .eq("rule_id", ruleId)
                .eq("del_flag", 0));
    }

    @Override
    public int checkCodeUnique(TPestRule record) {
        if (record == null || StringUtils.isEmpty(record.getRuleCode())) {
            return 0;
        }
        QueryWrapper<TPestRule> queryWrapper = new QueryWrapper<TPestRule>()
                .eq("rule_code", record.getRuleCode())
                .eq("del_flag", 0);
        if (record.getId() != null) {
            queryWrapper.ne("id", record.getId());
        }
        return this.baseMapper.selectList(queryWrapper).size();
    }

    @Override
    public int updateVisible(TPestRule record) {
        if (record == null || record.getId() == null || record.getStatus() == null) {
            return 0;
        }
        return this.baseMapper.update(null, new UpdateWrapper<TPestRule>()
                .eq("id", record.getId())
                .eq("del_flag", 0)
                .set("status", record.getStatus()));
    }
}
