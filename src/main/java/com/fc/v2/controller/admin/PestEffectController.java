package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestEffect;
import com.fc.v2.service.ITPestEffectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 防治效果评估单 Controller
 *
 * @author fuce
 * @date 2026-09-13
 */
@Api(value = "防治效果评估单")
@Controller
@RequestMapping("/PestEffectController")
public class PestEffectController extends BaseController {

    private final String prefix = "admin/pestEffect";

    @Autowired
    private ITPestEffectService pestEffectService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("pest:pestEffect:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "防治效果评估单集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("pest:pestEffect:list")
    @ResponseBody
    public ResultTable list(TPestEffect record) {
        QueryWrapper<TPestEffect> queryWrapper = new QueryWrapper<TPestEffect>();
        startPage();
        com.github.pagehelper.PageInfo<TPestEffect> page =
                new com.github.pagehelper.PageInfo<TPestEffect>(pestEffectService.selectTPestEffectList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @Log(title = "防治效果评估单新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestEffect:add")
    @ResponseBody
    public AjaxResult add(TPestEffect record) {
        return toAjax(pestEffectService.insertTPestEffect(record));
    }

    @Log(title = "防治效果评估单修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestEffect:edit")
    @ResponseBody
    public AjaxResult editSave(TPestEffect record) {
        return toAjax(pestEffectService.updateTPestEffect(record));
    }

    @Log(title = "防治效果评估单删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestEffect:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestEffectService.deleteTPestEffectByIds(ids));
    }
}
