package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestRule;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.model.auto.TPestSurvey;
import com.fc.v2.service.ITPestRuleService;
import com.fc.v2.service.ITPestStationService;
import com.fc.v2.service.ITPestSurveyService;
import com.fc.v2.util.StringUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 病虫发生调查 Controller
 *
 * @author fuce
 * @date 2026-09-13
 */
@Api(value = "病虫发生调查")
@Controller
@RequestMapping("/PestSurveyController")
public class PestSurveyController extends BaseController {

    private final String prefix = "admin/pestSurvey";

    @Autowired
    private ITPestSurveyService pestSurveyService;

    @Autowired
    private ITPestRuleService pestRuleService;

    @Autowired
    private ITPestStationService pestStationService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("pest:pestSurvey:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "病虫发生调查集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("pest:pestSurvey:list")
    @ResponseBody
    public ResultTable list(TPestSurvey record) {
        QueryWrapper<TPestSurvey> queryWrapper = new QueryWrapper<TPestSurvey>();
        queryWrapper.like(StringUtils.isNotEmpty(record.getSurveyNo()), "survey_no", record.getSurveyNo());
        queryWrapper.like(StringUtils.isNotEmpty(record.getStationCode()), "station_code", record.getStationCode());
        queryWrapper.eq(StringUtils.isNotNull(record.getSurveyStatus()), "survey_status", record.getSurveyStatus());

        startPage();
        PageInfo<TPestSurvey> page =
                new PageInfo<TPestSurvey>(pestSurveyService.selectTPestSurveyList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @ApiOperation(value = "新增跳转", notes = "新增跳转")
    @GetMapping("/add")
    public String add(ModelMap model) {
        model.put("stations", pestStationService.selectActiveStationList());
        model.put("rules", pestRuleService.selectEnabledRuleList());
        return prefix + "/add";
    }

    @Log(title = "病虫发生调查新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestSurvey:add")
    @ResponseBody
    public AjaxResult add(TPestSurvey record) {
        AjaxResult check = validate(record, null);
        if (check != null) {
            return check;
        }
        if (pestSurveyService.checkSurveyNoUnique(record) > 0) {
            return error("调查单号重复");
        }
        return toAjax(pestSurveyService.insertTPestSurvey(record));
    }

    @ApiOperation(value = "修改跳转", notes = "修改跳转")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap model) {
        TPestSurvey survey = pestSurveyService.selectTPestSurveyById(id);
        model.put("Survey", survey);
        // 当前记录引用的测报点、规则即使已停用也要保留在下拉中，避免编辑时丢值
        model.put("stations", stationsWithCurrent(survey == null ? null : survey.getStationId()));
        model.put("rules", rulesWithCurrent(survey == null ? null : survey.getRuleId()));
        return prefix + "/edit";
    }

    private List<TPestStation> stationsWithCurrent(Long currentId) {
        List<TPestStation> stations = pestStationService.selectActiveStationList();
        if (currentId != null
                && stations.stream().noneMatch(s -> currentId.equals(s.getId()))) {
            TPestStation current = pestStationService.selectTPestStationById(currentId);
            if (current != null) {
                stations.add(current);
            }
        }
        return stations;
    }

    private List<TPestRule> rulesWithCurrent(Long currentId) {
        List<TPestRule> rules = pestRuleService.selectEnabledRuleList();
        if (currentId != null
                && rules.stream().noneMatch(r -> currentId.equals(r.getId()))) {
            TPestRule current = pestRuleService.selectTPestRuleById(currentId);
            if (current != null) {
                rules.add(current);
            }
        }
        return rules;
    }

    @Log(title = "病虫发生调查修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestSurvey:edit")
    @ResponseBody
    public AjaxResult editSave(TPestSurvey record) {
        if (record.getId() == null) {
            return error("缺少调查记录主键");
        }
        TPestSurvey dbRecord = pestSurveyService.selectTPestSurveyById(record.getId());
        if (dbRecord == null) {
            return error("调查记录不存在");
        }
        AjaxResult check = validate(record, dbRecord);
        if (check != null) {
            return check;
        }
        // 编辑也要查重：单号允许改，但不能撞其它记录；查重已排除自身，未改单号可正常保存
        if (pestSurveyService.checkSurveyNoUnique(record) > 0) {
            return error("调查单号重复");
        }
        return toAjax(pestSurveyService.updateTPestSurvey(record));
    }

    @Log(title = "病虫发生调查复核", action = "review")
    @ApiOperation(value = "复核", notes = "待复核记录复核通过")
    @RequestMapping(value = "/review", method = {RequestMethod.PUT, RequestMethod.POST})
    @RequiresPermissions("pest:pestSurvey:review")
    @ResponseBody
    public AjaxResult review(Long id) {
        if (id == null) {
            return error("缺少调查记录主键");
        }
        int rows = pestSurveyService.reviewTPestSurvey(id);
        return rows > 0 ? success("复核完成") : error("该记录不存在或已复核");
    }

    @Log(title = "病虫发生调查删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestSurvey:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestSurveyService.deleteTPestSurveyByIds(ids));
    }

    /**
     * 保存前校验：单号、测报点、分级规则、病株率、调查日期、调查人为必填项。
     * 编辑时允许保留原记录已引用的撤点测报点、停用规则；改选其它失效档案则拒绝，
     * 避免错误数据进台账
     */
    private AjaxResult validate(TPestSurvey record, TPestSurvey dbRecord) {
        if (StringUtils.isEmpty(record.getSurveyNo())) {
            return error("调查单号不能为空");
        }
        if (record.getStationId() == null) {
            return error("请选择测报点");
        }
        TPestStation station = pestStationService.selectTPestStationById(record.getStationId());
        if (station == null) {
            return error("测报点不存在");
        }
        boolean keepStation = dbRecord != null && record.getStationId().equals(dbRecord.getStationId());
        if (!keepStation && station.getStatus() != null && station.getStatus() == 1) {
            return error("测报点已撤点，不能登记调查记录");
        }
        if (record.getRuleId() == null) {
            return error("请选择发生程度分级规则");
        }
        TPestRule rule = pestRuleService.selectTPestRuleById(record.getRuleId());
        if (rule == null) {
            return error("分级规则不存在");
        }
        boolean keepRule = dbRecord != null && record.getRuleId().equals(dbRecord.getRuleId());
        if (!keepRule && rule.getStatus() != null && rule.getStatus() == 1) {
            return error("分级规则已停用，不能登记调查记录");
        }
        if (record.getSurveyRate() == null) {
            return error("病株率不能为空");
        }
        if (record.getSurveyRate().compareTo(BigDecimal.ZERO) < 0
                || record.getSurveyRate().compareTo(new BigDecimal("100")) > 0) {
            return error("病株率须在0-100之间");
        }
        if (record.getSurveyDate() == null) {
            return error("调查日期不能为空");
        }
        if (StringUtils.isEmpty(record.getSurveyBy())) {
            return error("调查人不能为空");
        }
        return null;
    }
}
