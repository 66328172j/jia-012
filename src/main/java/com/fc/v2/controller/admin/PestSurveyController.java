package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestSurvey;
import com.fc.v2.service.ITPestSurveyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
        startPage();
        com.github.pagehelper.PageInfo<TPestSurvey> page =
                new com.github.pagehelper.PageInfo<TPestSurvey>(pestSurveyService.selectTPestSurveyList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @Log(title = "病虫发生调查新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestSurvey:add")
    @ResponseBody
    public AjaxResult add(TPestSurvey record) {
        return toAjax(pestSurveyService.insertTPestSurvey(record));
    }

    @Log(title = "病虫发生调查修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestSurvey:edit")
    @ResponseBody
    public AjaxResult editSave(TPestSurvey record) {
        return toAjax(pestSurveyService.updateTPestSurvey(record));
    }

    @Log(title = "病虫发生调查删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestSurvey:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestSurveyService.deleteTPestSurveyByIds(ids));
    }
}
