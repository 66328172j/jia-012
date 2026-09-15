package com.fc.v2.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fc.v2.common.support.ConvertUtil;
import com.fc.v2.mapper.auto.TPestRuleMapper;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.mapper.auto.TPestSurveyMapper;
import com.fc.v2.model.auto.TPestRule;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.model.auto.TPestSurvey;
import com.fc.v2.service.ITPestSurveyService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 病虫发生调查Service业务层处理
 *
 * @author fuce
 * @date 2026-09-13
 */
@Service
public class TPestSurveyServiceImpl extends ServiceImpl<TPestSurveyMapper, TPestSurvey> implements ITPestSurveyService {

    @Autowired
    private TPestRuleMapper pestRuleMapper;

    @Autowired
    private TPestStationMapper pestStationMapper;

    @Override
    public TPestSurvey selectTPestSurveyById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TPestSurvey>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TPestSurvey> selectTPestSurveyList(Wrapper<TPestSurvey> queryWrapper) {
        QueryWrapper<TPestSurvey> wrapper = (QueryWrapper<TPestSurvey>) queryWrapper;
        wrapper.eq("del_flag", 0).orderByDesc("survey_date").orderByDesc("id");
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public int insertTPestSurvey(TPestSurvey record) {
        if (record == null) {
            return 0;
        }

        fillSnapshot(record);
        record.setOccurLevel(grade(record.getSurveyRate(), record.getRuleId()));
        // 发生程度只允许系统判定，复核状态新建默认待复核
        record.setSurveyStatus(0);
        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTPestSurvey(TPestSurvey record) {
        if (record == null || record.getId() == null) {
            return 0;
        }
        TPestSurvey db = selectTPestSurveyById(record.getId());
        if (db == null) {
            return 0;
        }
        // 已复核记录为定稿台账，任何编辑保存一律拒绝；
        // 修正须先走撤销/作废流程，不能在挂着已复核标记时改数
        if (db.getSurveyStatus() != null && db.getSurveyStatus() == 1) {
            return 0;
        }

        fillSnapshot(record);
        record.setOccurLevel(grade(record.getSurveyRate(), record.getRuleId()));
        // 复核状态只能走复核动作，编辑接口不得改写
        record.setSurveyStatus(null);
        return this.baseMapper.update(record, new UpdateWrapper<TPestSurvey>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTPestSurveyByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        if (idArr == null || idArr.length == 0) {
            return 0;
        }
        UpdateWrapper<TPestSurvey> wrapper = new UpdateWrapper<TPestSurvey>()
                .in("id", Arrays.asList(idArr))
                .set("del_flag", 1);
        return this.baseMapper.update(null, wrapper);
    }

    @Override
    public int reviewTPestSurvey(Long id) {
        if (id == null) {
            return 0;
        }
        return this.baseMapper.update(null, new UpdateWrapper<TPestSurvey>()
                .eq("id", id)
                .eq("del_flag", 0)
                .eq("survey_status", 0)
                .set("survey_status", 1));
    }

    @Override
    public int deleteTPestSurveyById(Long id) {
        if (id == null) {
            return 0;
        }
        return this.baseMapper.update(null, new UpdateWrapper<TPestSurvey>()
                .eq("id", id)
                .set("del_flag", 1));
    }

    /**
     * 调查单号唯一校验：只统计正常记录，编辑时排除自身，
     * 否则老记录单号未改动也会被误判为重复
     */
    @Override
    public int checkSurveyNoUnique(TPestSurvey record) {
        if (record == null || StringUtils.isEmpty(record.getSurveyNo())) {
            return 0;
        }
        QueryWrapper<TPestSurvey> queryWrapper = new QueryWrapper<TPestSurvey>()
                .eq("survey_no", record.getSurveyNo())
                .eq("del_flag", 0);
        if (record.getId() != null) {
            queryWrapper.ne("id", record.getId());
        }
        return this.baseMapper.selectList(queryWrapper).size();
    }

    /**
     * 按选中的分级规则和病株率判定发生程度
     * 1轻 2中 3重 4大发生
     * 边界口径：病株率达到上限即升入下一档（如恰等于轻度上限判中）
     */
    private int grade(BigDecimal surveyRate, Long ruleId) {
        TPestRule rule = pestRuleMapper.selectById(ruleId);
        if (surveyRate == null || rule == null
                || rule.getDev1Max() == null || rule.getDev2Max() == null || rule.getDev3Max() == null) {
            return 0;
        }
        if (surveyRate.compareTo(rule.getDev1Max()) < 0) {
            return 1;
        }
        if (surveyRate.compareTo(rule.getDev2Max()) < 0) {
            return 2;
        }
        if (surveyRate.compareTo(rule.getDev3Max()) < 0) {
            return 3;
        }
        return 4;
    }

    /**
     * 冗余测报点编号、规则编号，以档案为准
     */
    private void fillSnapshot(TPestSurvey record) {
        if (record.getStationId() != null) {
            TPestStation station = pestStationMapper.selectById(record.getStationId());
            if (station != null) {
                record.setStationCode(station.getStationCode());
            }
        }
        if (record.getRuleId() != null) {
            TPestRule rule = pestRuleMapper.selectById(record.getRuleId());
            if (rule != null) {
                record.setRuleCode(rule.getRuleCode());
            }
        }
    }
}
