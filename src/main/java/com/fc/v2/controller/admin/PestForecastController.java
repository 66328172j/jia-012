package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestForecast;
import com.fc.v2.service.ITPestForecastService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 防治适期预报单 Controller
 *
 * @author fuce
 * @date 2026-09-13
 */
@Api(value = "防治适期预报单")
@Controller
@RequestMapping("/PestForecastController")
public class PestForecastController extends BaseController {

    private final String prefix = "admin/pestForecast";

    @Autowired
    private ITPestForecastService pestForecastService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("pest:pestForecast:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "防治适期预报单集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("pest:pestForecast:list")
    @ResponseBody
    public ResultTable list(TPestForecast record) {
        QueryWrapper<TPestForecast> queryWrapper = new QueryWrapper<TPestForecast>();
        startPage();
        com.github.pagehelper.PageInfo<TPestForecast> page =
                new com.github.pagehelper.PageInfo<TPestForecast>(pestForecastService.selectTPestForecastList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @Log(title = "防治适期预报单新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestForecast:add")
    @ResponseBody
    public AjaxResult add(TPestForecast record) {
        return toAjax(pestForecastService.insertTPestForecast(record));
    }

    @Log(title = "防治适期预报单修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestForecast:edit")
    @ResponseBody
    public AjaxResult editSave(TPestForecast record) {
        return toAjax(pestForecastService.updateTPestForecast(record));
    }

    @Log(title = "防治适期预报单删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestForecast:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestForecastService.deleteTPestForecastByIds(ids));
    }
}
