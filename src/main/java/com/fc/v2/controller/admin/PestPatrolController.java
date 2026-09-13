package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestPatrol;
import com.fc.v2.service.ITPestPatrolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 测报点巡查记录 Controller
 *
 * @author fuce
 * @date 2026-09-13
 */
@Api(value = "测报点巡查记录")
@Controller
@RequestMapping("/PestPatrolController")
public class PestPatrolController extends BaseController {

    private final String prefix = "admin/pestPatrol";

    @Autowired
    private ITPestPatrolService pestPatrolService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("pest:pestPatrol:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "测报点巡查记录集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("pest:pestPatrol:list")
    @ResponseBody
    public ResultTable list(TPestPatrol record) {
        QueryWrapper<TPestPatrol> queryWrapper = new QueryWrapper<TPestPatrol>();
        startPage();
        com.github.pagehelper.PageInfo<TPestPatrol> page =
                new com.github.pagehelper.PageInfo<TPestPatrol>(pestPatrolService.selectTPestPatrolList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @Log(title = "测报点巡查记录新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestPatrol:add")
    @ResponseBody
    public AjaxResult add(TPestPatrol record) {
        return toAjax(pestPatrolService.insertTPestPatrol(record));
    }

    @Log(title = "测报点巡查记录修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestPatrol:edit")
    @ResponseBody
    public AjaxResult editSave(TPestPatrol record) {
        return toAjax(pestPatrolService.updateTPestPatrol(record));
    }

    @Log(title = "测报点巡查记录删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestPatrol:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestPatrolService.deleteTPestPatrolByIds(ids));
    }
}
