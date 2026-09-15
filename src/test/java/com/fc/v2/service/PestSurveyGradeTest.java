package com.fc.v2.service;

import com.fc.v2.mapper.auto.TPestRuleMapper;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.mapper.auto.TPestSurveyMapper;
import com.fc.v2.model.auto.TPestRule;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.model.auto.TPestSurvey;
import com.fc.v2.service.impl.TPestSurveyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 病虫发生调查：分级规则自动定级、新建默认待复核 单元测试
 * 基线规则 PG-01：轻度上限5 中度上限10 重度上限15
 * 边界口径：病株率达到上限即升入下一档（5.00判中 10.00判重 15.00判大发生）
 *
 * @author jiabo
 * @date 2026-09-15
 */
@ExtendWith(MockitoExtension.class)
public class PestSurveyGradeTest {

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

        TPestRule rule = new TPestRule();
        rule.setId(1L);
        rule.setRuleCode("PG-01");
        rule.setDev1Max(new BigDecimal("5.00"));
        rule.setDev2Max(new BigDecimal("10.00"));
        rule.setDev3Max(new BigDecimal("15.00"));
        rule.setStatus(0);
        // lenient：缺规则的用例会把 ruleId 改到不存在的 999
        lenient().when(ruleMapper.selectById(1L)).thenReturn(rule);

        TPestStation station = new TPestStation();
        station.setId(1L);
        station.setStationCode("PS-0001");
        station.setStatus(0);
        lenient().when(stationMapper.selectById(1L)).thenReturn(station);

        lenient().when(surveyMapper.insert(any(TPestSurvey.class))).thenReturn(1);
    }

    private TPestSurvey survey(String rate) {
        TPestSurvey record = new TPestSurvey();
        record.setSurveyNo("DC-TEST-001");
        record.setStationId(1L);
        record.setRuleId(1L);
        record.setSurveyRate(new BigDecimal(rate));
        record.setSurveyDate(new Date());
        record.setSurveyBy("张测报");
        return record;
    }

    @Test
    public void gradeLight() {
        TPestSurvey record = survey("3.00");
        surveyService.insertTPestSurvey(record);
        assertEquals(1, record.getOccurLevel().intValue());
    }

    @Test
    public void gradeLightAtUpperBound() {
        // 病株率恰好等于轻度上限，达到即升档判中
        TPestSurvey record = survey("5.00");
        surveyService.insertTPestSurvey(record);
        assertEquals(2, record.getOccurLevel().intValue());
    }

    @Test
    public void gradeJustBelowLightUpperBound() {
        // 差一格不到轻度上限，仍判轻
        TPestSurvey record = survey("4.99");
        surveyService.insertTPestSurvey(record);
        assertEquals(1, record.getOccurLevel().intValue());
    }

    @Test
    public void gradeMedium() {
        TPestSurvey record = survey("8.00");
        surveyService.insertTPestSurvey(record);
        assertEquals(2, record.getOccurLevel().intValue());
    }

    @Test
    public void gradeMediumAtUpperBound() {
        // 病株率恰好等于中度上限，达到即升档判重
        TPestSurvey record = survey("10.00");
        surveyService.insertTPestSurvey(record);
        assertEquals(3, record.getOccurLevel().intValue());
    }

    @Test
    public void gradeJustBelowMediumUpperBound() {
        TPestSurvey record = survey("9.99");
        surveyService.insertTPestSurvey(record);
        assertEquals(2, record.getOccurLevel().intValue());
    }

    @Test
    public void gradeHeavy() {
        TPestSurvey record = survey("12.00");
        surveyService.insertTPestSurvey(record);
        assertEquals(3, record.getOccurLevel().intValue());
    }

    @Test
    public void gradeHeavyAtUpperBound() {
        // 病株率恰好等于重度上限，达到即升档判大发生
        TPestSurvey record = survey("15.00");
        surveyService.insertTPestSurvey(record);
        assertEquals(4, record.getOccurLevel().intValue());
    }

    @Test
    public void gradeJustBelowHeavyUpperBound() {
        TPestSurvey record = survey("14.99");
        surveyService.insertTPestSurvey(record);
        assertEquals(3, record.getOccurLevel().intValue());
    }

    @Test
    public void gradeOutbreak() {
        TPestSurvey record = survey("25.50");
        surveyService.insertTPestSurvey(record);
        assertEquals(4, record.getOccurLevel().intValue());
    }

    @Test
    public void newRecordDefaultsToPendingReviewAndSnapshotFilled() {
        TPestSurvey record = survey("8.00");
        // 前端若伪造发生程度或复核状态，落库前必须被系统值覆盖
        record.setOccurLevel(4);
        record.setSurveyStatus(1);
        surveyService.insertTPestSurvey(record);

        assertEquals(2, record.getOccurLevel().intValue(), "发生程度应由系统判定，忽略前端传值");
        assertEquals(0, record.getSurveyStatus().intValue(), "新建记录默认待复核");
        assertEquals(0, record.getDelFlag().intValue());
        assertEquals("PS-0001", record.getStationCode(), "测报点编号以档案冗余回填");
        assertEquals("PG-01", record.getRuleCode(), "规则编号以档案冗余回填");
    }

    @Test
    public void missingRuleYieldsLevelZero() {
        TPestSurvey record = survey("8.00");
        record.setRuleId(999L);
        when(ruleMapper.selectById(999L)).thenReturn(null);
        surveyService.insertTPestSurvey(record);
        assertEquals(0, record.getOccurLevel().intValue());
    }

    @Test
    public void editPendingRecordRecomputesLevelAndKeepsStatus() {
        TPestSurvey dbRecord = survey("3.00");
        dbRecord.setId(10L);
        dbRecord.setOccurLevel(1);
        dbRecord.setSurveyStatus(0);
        when(surveyMapper.selectOne(any())).thenReturn(dbRecord);
        when(surveyMapper.update(any(TPestSurvey.class), any())).thenReturn(1);

        // 待复核记录改病株率到重度区间，允许保存：发生程度重新判定，复核状态不由编辑接口改写
        TPestSurvey update = survey("12.00");
        update.setId(10L);
        int rows = surveyService.updateTPestSurvey(update);

        assertEquals(1, rows, "待复核记录应允许编辑保存");
        assertEquals(3, update.getOccurLevel().intValue(), "改病株率后须重新判定发生程度");
        assertEquals(null, update.getSurveyStatus(), "复核状态只能走复核动作，编辑接口不得改写");
    }

    @Test
    public void editReviewedRecordRejected() {
        // 已复核定稿记录：编辑保存必须直接拒绝，落库 update 一次都不能执行，
        // 病株率、发生程度与已复核标记都保持原样
        TPestSurvey dbRecord = survey("3.00");
        dbRecord.setId(10L);
        dbRecord.setOccurLevel(1);
        dbRecord.setSurveyStatus(1);
        when(surveyMapper.selectOne(any())).thenReturn(dbRecord);

        TPestSurvey update = survey("12.00");
        update.setId(10L);
        int rows = surveyService.updateTPestSurvey(update);

        assertEquals(0, rows, "已复核定稿记录不允许编辑保存");
        verify(surveyMapper, never()).update(any(TPestSurvey.class), any());
        assertEquals(1, dbRecord.getOccurLevel().intValue(), "定稿记录的发生程度不得变化");
        assertEquals(1, dbRecord.getSurveyStatus().intValue(), "定稿记录的复核标记不得变化");
    }

    @Test
    public void reviewOnlySucceedsOnPendingRecord() {
        // mapper 按 survey_status=0 条件更新；已复核记录更新行数为0，接口据此提示“已复核”
        when(surveyMapper.update(any(), any())).thenReturn(1);
        assertEquals(1, surveyService.reviewTPestSurvey(10L));

        when(surveyMapper.update(any(), any())).thenReturn(0);
        assertEquals(0, surveyService.reviewTPestSurvey(11L));
    }
}
