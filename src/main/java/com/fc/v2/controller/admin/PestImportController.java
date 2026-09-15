package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestImport;
import com.fc.v2.service.ITPestImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 监测数据导入批次 Controller
 *
 * @author fuce
 * @date 2026-09-13
 */
@Api(value = "监测数据导入批次")
@Controller
@RequestMapping("/PestImportController")
public class PestImportController extends BaseController {

    private final String prefix = "admin/pestImport";

    @Autowired
    private ITPestImportService pestImportService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("pest:pestImport:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "监测数据导入批次集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("pest:pestImport:list")
    @ResponseBody
    public ResultTable list(TPestImport record) {
        QueryWrapper<TPestImport> queryWrapper = new QueryWrapper<TPestImport>();
        startPage();
        com.github.pagehelper.PageInfo<TPestImport> page =
                new com.github.pagehelper.PageInfo<TPestImport>(pestImportService.selectTPestImportList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @Log(title = "监测数据导入批次新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestImport:add")
    @ResponseBody
    public AjaxResult add(TPestImport record) {
        return toAjax(pestImportService.insertTPestImport(record));
    }

    @Log(title = "监测数据导入批次修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestImport:edit")
    @ResponseBody
    public AjaxResult editSave(TPestImport record) {
        return toAjax(pestImportService.updateTPestImport(record));
    }

    @Log(title = "监测数据导入批次删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestImport:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestImportService.deleteTPestImportByIds(ids));
    }
}
