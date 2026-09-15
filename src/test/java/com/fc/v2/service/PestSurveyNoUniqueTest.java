package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.mapper.auto.TPestRuleMapper;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.mapper.auto.TPestSurveyMapper;
import com.fc.v2.model.auto.TPestSurvey;
import com.fc.v2.service.impl.TPestSurveyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 病虫发生调查：调查单号唯一校验 单元测试
 * 链路口径：新增、编辑保存前都查重；查重只统计正常记录且编辑时排除自身，
 * 保证老记录单号未改动可以正常保存，改号撞到其它记录才被拦截
 *
 * @author jiabo
 * @date 2026-09-15
 */
@ExtendWith(MockitoExtension.class)
public class PestSurveyNoUniqueTest {

    @Mock
    private TPestSurveyMapper surveyMapper;

    @Mock
    private TPestRuleMapper ruleMapper;

    @Mock
    private TPestStationMapper stationMapper;

    private TPestSurveyServiceImpl surveyService;

    @BeforeEach
    public void setUp() {
        surveyService = new TPestSurveyServiceImpl();
        ReflectionTestUtils.setField(surveyService, "baseMapper", surveyMapper);
        ReflectionTestUtils.setField(surveyService, "pestRuleMapper", ruleMapper);
        ReflectionTestUtils.setField(surveyService, "pestStationMapper", stationMapper);
    }

    private TPestSurvey survey(String surveyNo) {
        TPestSurvey record = new TPestSurvey();
        record.setSurveyNo(surveyNo);
        return record;
    }

    @Test
    public void duplicateFoundOnAdd() {
        // 新增不带主键，库中已有同号正常记录，必须判重
        when(surveyMapper.selectList(any())).thenReturn(Collections.singletonList(survey("DC-20260915-001")));
        assertEquals(1, surveyService.checkSurveyNoUnique(survey("DC-20260915-001")));
    }

    @Test
    public void editWithUnchangedNoPasses() {
        // 编辑老记录、单号未改动：查重排除自身后查不到别人，放行
        TPestSurvey record = survey("DC-20260915-001");
        record.setId(10L);
        when(surveyMapper.selectList(any())).thenReturn(Collections.emptyList());
        assertEquals(0, surveyService.checkSurveyNoUnique(record));

        // 且查询条件必须带上 排除自身 + 只统计正常记录
        ArgumentCaptor<QueryWrapper> captor = ArgumentCaptor.forClass(QueryWrapper.class);
        verify(surveyMapper).selectList(captor.capture());
        String sqlSegment = captor.getValue().getSqlSegment();
        assertTrue(sqlSegment.contains("id <>"), "编辑查重必须排除记录自身");
        assertTrue(sqlSegment.contains("del_flag ="), "查重只统计正常记录");
    }

    @Test
    public void editWithClashingNoRejected() {
        // 编辑把单号改成别的记录已占用的号：查到的是别人（id不同），必须判重
        TPestSurvey record = survey("DC-20260915-002");
        record.setId(10L);
        TPestSurvey other = survey("DC-20260915-002");
        other.setId(20L);
        when(surveyMapper.selectList(any())).thenReturn(Collections.singletonList(other));
        assertEquals(1, surveyService.checkSurveyNoUnique(record));
    }

    @Test
    public void addWithoutIdDoesNotExcludeSelf() {
        // 新增场景没有主键，查询条件不得出现排除自身的 id 条件
        when(surveyMapper.selectList(any())).thenReturn(Collections.emptyList());
        surveyService.checkSurveyNoUnique(survey("DC-20260915-001"));

        ArgumentCaptor<QueryWrapper> captor = ArgumentCaptor.forClass(QueryWrapper.class);
        verify(surveyMapper).selectList(captor.capture());
        assertFalse(captor.getValue().getSqlSegment().contains("id <>"), "新增查重不应带主键排除条件");
    }

    @Test
    public void blankSurveyNoSkipsQuery() {
        // 单号为空的记录由必填校验拦截，唯一校验直接放行且不应查询数据库
        assertEquals(0, surveyService.checkSurveyNoUnique(survey("")));
        assertEquals(0, surveyService.checkSurveyNoUnique(null));
        verify(surveyMapper, never()).selectList(any());
    }
}
