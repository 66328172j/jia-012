package com.fc.v2.templates;

import com.fc.v2.model.auto.TPestRule;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.model.auto.TPestSurvey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * pest 台账页面 Thymeleaf 离线渲染冒烟测试：
 * 保证 th:each/th:field/th:attr/#dates 等表达式无解析错误
 *
 * @author jiabo
 * @date 2026-09-15
 */
public class PestTemplateRenderTest {

    private static SpringTemplateEngine engine;

    @BeforeAll
    public static void setUp() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
    }

    private WebContext baseContext() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebContext context = new WebContext(request, response, new MockServletContext());
        context.setVariable("rootPath", "");

        TPestStation station = new TPestStation();
        station.setId(1L);
        station.setStationCode("PS-0001");
        station.setStationName("城郊水稻测报点");
        station.setStatus(0);
        context.setVariable("stations", Collections.singletonList(station));

        TPestStation withdrawn = new TPestStation();
        withdrawn.setId(2L);
        withdrawn.setStationCode("PS-0002");
        withdrawn.setStationName("老棉田撤点测报点");
        withdrawn.setStatus(1);
        List<TPestStation> stationList = new java.util.ArrayList<>(Collections.singletonList(station));
        stationList.add(withdrawn);
        context.setVariable("stationsWithWithdrawn", stationList);

        TPestRule rule = new TPestRule();
        rule.setId(1L);
        rule.setRuleCode("PG-01");
        rule.setRuleName("病虫发生程度分级规则");
        rule.setDev1Max(new BigDecimal("5.00"));
        rule.setDev2Max(new BigDecimal("10.00"));
        rule.setDev3Max(new BigDecimal("15.00"));
        rule.setStatus(0);
        context.setVariable("rules", Collections.singletonList(rule));

        TPestRule disabled = new TPestRule();
        disabled.setId(2L);
        disabled.setRuleCode("PG-02");
        disabled.setRuleName("停用版分级规则");
        disabled.setDev1Max(new BigDecimal("5.00"));
        disabled.setDev2Max(new BigDecimal("10.00"));
        disabled.setDev3Max(new BigDecimal("15.00"));
        disabled.setStatus(1);
        List<TPestRule> ruleList = new java.util.ArrayList<>(Collections.singletonList(rule));
        ruleList.add(disabled);
        context.setVariable("rulesWithDisabled", ruleList);

        TPestSurvey survey = new TPestSurvey();
        survey.setId(100L);
        survey.setSurveyNo("DC-20260915-001");
        survey.setStationId(2L);
        survey.setStationCode("PS-0002");
        survey.setRuleId(2L);
        survey.setRuleCode("PG-02");
        survey.setSurveyRate(new BigDecimal("8.00"));
        survey.setOccurLevel(2);
        survey.setSurveyDate(new Date());
        survey.setSurveyBy("张测报");
        survey.setSurveyStatus(0);
        context.setVariable("Survey", survey);

        TPestRule editingRule = new TPestRule();
        editingRule.setId(1L);
        editingRule.setRuleCode("PG-01");
        editingRule.setRuleName("病虫发生程度分级规则");
        editingRule.setDev1Max(new BigDecimal("5.00"));
        editingRule.setDev2Max(new BigDecimal("10.00"));
        editingRule.setDev3Max(new BigDecimal("15.00"));
        editingRule.setStatus(0);
        context.setVariable("Rule", editingRule);
        return context;
    }

    @Test
    public void surveyList() {
        String html = engine.process("admin/pestSurvey/list", baseContext());
        assertTrue(html.contains("survey-table"));
    }

    @Test
    public void surveyAdd() {
        String html = engine.process("admin/pestSurvey/add", baseContext());
        assertTrue(html.contains("PS-0001"));
        assertTrue(html.contains("data-d1=\"5.00\""));
    }

    @Test
    public void surveyEdit() {
        WebContext context = baseContext();
        // 编辑页下拉含当前记录引用的已撤点/已停用项
        context.setVariable("stations", context.getVariable("stationsWithWithdrawn"));
        context.setVariable("rules", context.getVariable("rulesWithDisabled"));
        String html = engine.process("admin/pestSurvey/edit", context);
        assertTrue(html.contains("DC-20260915-001"));
        assertTrue(html.contains("已撤点"));
        assertTrue(html.contains("已停用"));
        assertTrue(html.contains("张测报"));
    }

    @Test
    public void ruleList() {
        String html = engine.process("admin/pestRule/list", baseContext());
        assertTrue(html.contains("rule-table"));
    }

    @Test
    public void ruleAdd() {
        String html = engine.process("admin/pestRule/add", baseContext());
        assertTrue(html.contains("rule-save"));
    }

    @Test
    public void ruleEdit() {
        String html = engine.process("admin/pestRule/edit", baseContext());
        assertTrue(html.contains("PG-01"));
        assertTrue(html.contains("rule-update"));
    }
}
