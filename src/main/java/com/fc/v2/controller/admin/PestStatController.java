package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestStat;
import com.fc.v2.service.ITPestStatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 防治覆盖率统计 Controller
 *
 * @author fuce
 * @date 2026-09-13
 */
@Api(value = "防治覆盖率统计")
@Controller
@RequestMapping("/PestStatController")
public class PestStatController extends BaseController {

    private final String prefix = "admin/pestStat";

    @Autowired
    private ITPestStatService pestStatService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("pest:pestStat:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "防治覆盖率统计集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("pest:pestStat:list")
    @ResponseBody
    public ResultTable list(TPestStat record) {
        QueryWrapper<TPestStat> queryWrapper = new QueryWrapper<TPestStat>();
        startPage();
        com.github.pagehelper.PageInfo<TPestStat> page =
                new com.github.pagehelper.PageInfo<TPestStat>(pestStatService.selectTPestStatList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @Log(title = "防治覆盖率统计新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestStat:add")
    @ResponseBody
    public AjaxResult add(TPestStat record) {
        return toAjax(pestStatService.insertTPestStat(record));
    }

    @Log(title = "防治覆盖率统计修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestStat:edit")
    @ResponseBody
    public AjaxResult editSave(TPestStat record) {
        return toAjax(pestStatService.updateTPestStat(record));
    }

    @Log(title = "防治覆盖率统计删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestStat:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestStatService.deleteTPestStatByIds(ids));
    }
}
