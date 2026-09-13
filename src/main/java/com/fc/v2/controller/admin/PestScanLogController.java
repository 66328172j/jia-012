package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestScanLog;
import com.fc.v2.service.ITPestScanLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 移动端现场测报登记 Controller
 *
 * @author fuce
 * @date 2026-09-13
 */
@Api(value = "移动端现场测报登记")
@Controller
@RequestMapping("/PestScanLogController")
public class PestScanLogController extends BaseController {

    private final String prefix = "admin/pestScanLog";

    @Autowired
    private ITPestScanLogService pestScanLogService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("pest:pestScanLog:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "移动端现场测报登记集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("pest:pestScanLog:list")
    @ResponseBody
    public ResultTable list(TPestScanLog record) {
        QueryWrapper<TPestScanLog> queryWrapper = new QueryWrapper<TPestScanLog>();
        startPage();
        com.github.pagehelper.PageInfo<TPestScanLog> page =
                new com.github.pagehelper.PageInfo<TPestScanLog>(pestScanLogService.selectTPestScanLogList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @Log(title = "移动端现场测报登记新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestScanLog:add")
    @ResponseBody
    public AjaxResult add(TPestScanLog record) {
        return toAjax(pestScanLogService.insertTPestScanLog(record));
    }

    @Log(title = "移动端现场测报登记修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestScanLog:edit")
    @ResponseBody
    public AjaxResult editSave(TPestScanLog record) {
        return toAjax(pestScanLogService.updateTPestScanLog(record));
    }

    @Log(title = "移动端现场测报登记删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestScanLog:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestScanLogService.deleteTPestScanLogByIds(ids));
    }
}
