package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestExport;
import com.fc.v2.service.ITPestExportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 测报台账导出记录 Controller
 *
 * @author fuce
 * @date 2026-09-13
 */
@Api(value = "测报台账导出记录")
@Controller
@RequestMapping("/PestExportController")
public class PestExportController extends BaseController {

    private final String prefix = "admin/pestExport";

    @Autowired
    private ITPestExportService pestExportService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("pest:pestExport:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "测报台账导出记录集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("pest:pestExport:list")
    @ResponseBody
    public ResultTable list(TPestExport record) {
        QueryWrapper<TPestExport> queryWrapper = new QueryWrapper<TPestExport>();
        startPage();
        com.github.pagehelper.PageInfo<TPestExport> page =
                new com.github.pagehelper.PageInfo<TPestExport>(pestExportService.selectTPestExportList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @Log(title = "测报台账导出记录新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestExport:add")
    @ResponseBody
    public AjaxResult add(TPestExport record) {
        return toAjax(pestExportService.insertTPestExport(record));
    }

    @Log(title = "测报台账导出记录修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestExport:edit")
    @ResponseBody
    public AjaxResult editSave(TPestExport record) {
        return toAjax(pestExportService.updateTPestExport(record));
    }

    @Log(title = "测报台账导出记录删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestExport:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestExportService.deleteTPestExportByIds(ids));
    }
}
