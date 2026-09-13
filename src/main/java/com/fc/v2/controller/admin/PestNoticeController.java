package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestNotice;
import com.fc.v2.service.ITPestNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 防治通知单 Controller
 *
 * @author fuce
 * @date 2026-09-13
 */
@Api(value = "防治通知单")
@Controller
@RequestMapping("/PestNoticeController")
public class PestNoticeController extends BaseController {

    private final String prefix = "admin/pestNotice";

    @Autowired
    private ITPestNoticeService pestNoticeService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("pest:pestNotice:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "防治通知单集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("pest:pestNotice:list")
    @ResponseBody
    public ResultTable list(TPestNotice record) {
        QueryWrapper<TPestNotice> queryWrapper = new QueryWrapper<TPestNotice>();
        startPage();
        com.github.pagehelper.PageInfo<TPestNotice> page =
                new com.github.pagehelper.PageInfo<TPestNotice>(pestNoticeService.selectTPestNoticeList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @Log(title = "防治通知单新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestNotice:add")
    @ResponseBody
    public AjaxResult add(TPestNotice record) {
        return toAjax(pestNoticeService.insertTPestNotice(record));
    }

    @Log(title = "防治通知单修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestNotice:edit")
    @ResponseBody
    public AjaxResult editSave(TPestNotice record) {
        return toAjax(pestNoticeService.updateTPestNotice(record));
    }

    @Log(title = "防治通知单删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestNotice:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestNoticeService.deleteTPestNoticeByIds(ids));
    }
}
