package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.common.support.ConvertUtil;
import com.fc.v2.model.auto.TPestRule;
import com.fc.v2.service.ITPestRuleService;
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

/**
 * 发生程度分级规则 Controller
 *
 * @author jiabo
 * @date 2026-09-15
 */
@Api(value = "发生程度分级规则")
@Controller
@RequestMapping("/PestRuleController")
public class PestRuleController extends BaseController {

    private final String prefix = "admin/pestRule";

    @Autowired
    private ITPestRuleService pestRuleService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("pest:pestRule:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "发生程度分级规则集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("pest:pestRule:list")
    @ResponseBody
    public ResultTable list(TPestRule record) {
        QueryWrapper<TPestRule> queryWrapper = new QueryWrapper<TPestRule>();
        queryWrapper.like(StringUtils.isNotEmpty(record.getRuleCode()), "rule_code", record.getRuleCode());
        queryWrapper.like(StringUtils.isNotEmpty(record.getRuleName()), "rule_name", record.getRuleName());
        queryWrapper.eq(StringUtils.isNotNull(record.getStatus()), "status", record.getStatus());

        startPage();
        PageInfo<TPestRule> page =
                new PageInfo<TPestRule>(pestRuleService.selectTPestRuleList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @ApiOperation(value = "新增跳转", notes = "新增跳转")
    @GetMapping("/add")
    public String add(ModelMap model) {
        return prefix + "/add";
    }

    @Log(title = "发生程度分级规则新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestRule:add")
    @ResponseBody
    public AjaxResult add(TPestRule record) {
        AjaxResult check = validate(record);
        if (check != null) {
            return check;
        }
        if (pestRuleService.checkCodeUnique(record) > 0) {
            return error("规则编号重复");
        }
        return toAjax(pestRuleService.insertTPestRule(record));
    }

    @ApiOperation(value = "修改跳转", notes = "修改跳转")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap model) {
        model.put("Rule", pestRuleService.selectTPestRuleById(id));
        return prefix + "/edit";
    }

    @Log(title = "发生程度分级规则修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestRule:edit")
    @ResponseBody
    public AjaxResult editSave(TPestRule record) {
        if (record.getId() == null) {
            return error("缺少分级规则主键");
        }
        AjaxResult check = validate(record);
        if (check != null) {
            return check;
        }
        return toAjax(pestRuleService.updateTPestRule(record));
    }

    @Log(title = "发生程度分级规则删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestRule:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        // 被调查台账引用的规则不允许删除，历史记录的定级依据必须保留
        Long[] idArr = ConvertUtil.toLongArray(ids);
        for (Long ruleId : idArr) {
            if (pestRuleService.countSurveyReference(ruleId) > 0) {
                return error("规则已被调查记录引用，不能删除；如不再使用请停用");
            }
        }
        return toAjax(pestRuleService.deleteTPestRuleByIds(ids));
    }

    @ApiOperation(value = "检查编号唯一", notes = "检查编号唯一")
    @PostMapping("/checkCodeUnique")
    @ResponseBody
    public int checkCodeUnique(TPestRule record) {
        return pestRuleService.checkCodeUnique(record) > 0 ? 1 : 0;
    }

    @ApiOperation(value = "启用停用", notes = "启用停用")
    @PutMapping("/updateVisible")
    @ResponseBody
    public AjaxResult updateVisible(@RequestBody TPestRule record) {
        return toAjax(pestRuleService.updateVisible(record));
    }

    /**
     * 保存前校验：三级阈值必填且依次递增，保证病株率能正确落到 轻/中/重/大发生 四档
     */
    private AjaxResult validate(TPestRule record) {
        if (StringUtils.isEmpty(record.getRuleCode())) {
            return error("规则编号不能为空");
        }
        if (StringUtils.isEmpty(record.getRuleName())) {
            return error("规则名称不能为空");
        }
        BigDecimal d1 = record.getDev1Max();
        BigDecimal d2 = record.getDev2Max();
        BigDecimal d3 = record.getDev3Max();
        if (d1 == null || d2 == null || d3 == null) {
            return error("三条发生程度分界线不能为空");
        }
        if (d1.compareTo(BigDecimal.ZERO) <= 0
                || d1.compareTo(d2) >= 0 || d2.compareTo(d3) >= 0) {
            return error("分界线须满足 0 < 轻度上限 < 中度上限 < 重度上限");
        }
        return null;
    }
}
