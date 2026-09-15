package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestSpray;
import com.fc.v2.service.ITPestSprayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 防治作业药液配制台账 Controller
 *
 * @author fuce
 * @date 2026-09-13
 */
@Api(value = "防治作业药液配制台账")
@Controller
@RequestMapping("/PestSprayController")
public class PestSprayController extends BaseController {

    private final String prefix = "admin/pestSpray";

    @Autowired
    private ITPestSprayService pestSprayService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("pest:pestSpray:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "防治作业药液配制台账集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("pest:pestSpray:list")
    @ResponseBody
    public ResultTable list(TPestSpray record) {
        QueryWrapper<TPestSpray> queryWrapper = new QueryWrapper<TPestSpray>();
        startPage();
        com.github.pagehelper.PageInfo<TPestSpray> page =
                new com.github.pagehelper.PageInfo<TPestSpray>(pestSprayService.selectTPestSprayList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @Log(title = "防治作业药液配制台账新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestSpray:add")
    @ResponseBody
    public AjaxResult add(TPestSpray record) {
        return toAjax(pestSprayService.insertTPestSpray(record));
    }

    @Log(title = "防治作业药液配制台账修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestSpray:edit")
    @ResponseBody
    public AjaxResult editSave(TPestSpray record) {
        return toAjax(pestSprayService.updateTPestSpray(record));
    }

    @Log(title = "防治作业药液配制台账删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestSpray:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestSprayService.deleteTPestSprayByIds(ids));
    }
}
