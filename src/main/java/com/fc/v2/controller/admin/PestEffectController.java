package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TPestEffect;
import com.fc.v2.model.auto.TPestSpray;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.service.ITPestEffectService;
import com.fc.v2.service.ITPestSprayService;
import com.fc.v2.service.ITPestStationService;
import com.fc.v2.util.StringUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private ITPestStationService pestStationService;

    @Autowired
    private ITPestSprayService pestSprayService;

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
        queryWrapper.like(StringUtils.isNotEmpty(record.getEffectNo()), "effect_no", record.getEffectNo());
        queryWrapper.like(StringUtils.isNotEmpty(record.getStationCode()), "station_code", record.getStationCode());
        queryWrapper.eq(StringUtils.isNotNull(record.getEffectStatus()), "effect_status", record.getEffectStatus());

        startPage();
        PageInfo<TPestEffect> page =
                new PageInfo<TPestEffect>(pestEffectService.selectTPestEffectList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @ApiOperation(value = "新增跳转", notes = "新增跳转")
    @GetMapping("/add")
    public String add(ModelMap model) {
        model.put("stations", pestStationService.selectActiveStationList());
        model.put("sprays", pestSprayService.selectFinishedSprayList());
        return prefix + "/add";
    }

    @Log(title = "防治效果评估单新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("pest:pestEffect:add")
    @ResponseBody
    public AjaxResult add(TPestEffect record) {
        AjaxResult check = validate(record, null);
        if (check != null) {
            return check;
        }
        if (pestEffectService.checkEffectNoUnique(record) > 0) {
            return error("评估单号重复");
        }
        if (pestEffectService.checkSprayNoUnique(record) > 0) {
            return error("该防治作业单已做过效果评估，同一作业单只允许一张评估单");
        }
        return toAjax(pestEffectService.insertTPestEffect(record));
    }

    @ApiOperation(value = "修改跳转", notes = "修改跳转")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap model) {
        TPestEffect effect = pestEffectService.selectTPestEffectById(id);
        model.put("Effect", effect);
        // 已归档评估单只读展示：可回看当时定值，但不允许提交修改
        model.put("locked", effect != null && effect.getEffectStatus() != null && effect.getEffectStatus() == 2);
        // 当前记录引用的测报点、作业单即使已撤点/状态变化也要保留在下拉中，避免编辑时丢值
        model.put("stations", stationsWithCurrent(effect == null ? null : effect.getStationId()));
        model.put("sprays", spraysWithCurrent(effect == null ? null : effect.getSprayNo()));
        return prefix + "/edit";
    }

    private List<TPestStation> stationsWithCurrent(Long currentId) {
        List<TPestStation> stations = pestStationService.selectActiveStationList();
        if (currentId != null
                && stations.stream().noneMatch(s -> currentId.equals(s.getId()))) {
            TPestStation current = pestStationService.selectTPestStationById(currentId);
            if (current != null) {
                stations.add(current);
            }
        }
        return stations;
    }

    private List<TPestSpray> spraysWithCurrent(String currentSprayNo) {
        List<TPestSpray> sprays = pestSprayService.selectFinishedSprayList();
        if (StringUtils.isNotEmpty(currentSprayNo)
                && sprays.stream().noneMatch(s -> currentSprayNo.equals(s.getSprayNo()))) {
            TPestSpray current = pestSprayService.selectTPestSprayBySprayNo(currentSprayNo);
            if (current != null) {
                sprays.add(current);
            }
        }
        return sprays;
    }

    @Log(title = "防治效果评估单修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("pest:pestEffect:edit")
    @ResponseBody
    public AjaxResult editSave(TPestEffect record) {
        if (record.getId() == null) {
            return error("缺少评估单主键");
        }
        TPestEffect dbRecord = pestEffectService.selectTPestEffectById(record.getId());
        if (dbRecord == null) {
            return error("评估单不存在");
        }
        // 已归档评估单已锁定，禁止再走编辑改数；只能看不能改
        if (dbRecord.getEffectStatus() != null && dbRecord.getEffectStatus() == 2) {
            return error("该评估单已归档锁定，不能修改");
        }
        AjaxResult check = validate(record, dbRecord);
        if (check != null) {
            return check;
        }
        // 编辑也要查重：单号允许改，但不能撞其它记录；查重已排除自身，未改单号可正常保存
        if (pestEffectService.checkEffectNoUnique(record) > 0) {
            return error("评估单号重复");
        }
        if (pestEffectService.checkSprayNoUnique(record) > 0) {
            return error("该防治作业单已做过效果评估，同一作业单只允许一张评估单");
        }
        return toAjax(pestEffectService.updateTPestEffect(record));
    }

    @Log(title = "防治效果评估单评估", action = "evaluate")
    @ApiOperation(value = "评估", notes = "待评估记录评估完成")
    @RequestMapping(value = "/evaluate", method = {RequestMethod.PUT, RequestMethod.POST})
    @RequiresPermissions("pest:pestEffect:evaluate")
    @ResponseBody
    public AjaxResult evaluate(Long id) {
        if (id == null) {
            return error("缺少评估单主键");
        }
        int rows = pestEffectService.evaluateTPestEffect(id);
        return rows > 0 ? success("评估完成") : error("该评估单不存在或已评估");
    }

    @Log(title = "防治效果评估单归档", action = "archive")
    @ApiOperation(value = "归档", notes = "已评估记录归档锁定")
    @RequestMapping(value = "/archive", method = {RequestMethod.PUT, RequestMethod.POST})
    @RequiresPermissions("pest:pestEffect:archive")
    @ResponseBody
    public AjaxResult archive(Long id) {
        if (id == null) {
            return error("缺少评估单主键");
        }
        TPestEffect dbRecord = pestEffectService.selectTPestEffectById(id);
        if (dbRecord == null || dbRecord.getEffectStatus() == null || dbRecord.getEffectStatus() != 1) {
            return error("只有已评估的评估单才能归档");
        }
        // 减退率为负说明虫量不降反升，异常单不许归档，留待核实
        if (dbRecord.getReduceRate() != null && dbRecord.getReduceRate().signum() < 0) {
            return error("虫口减退率为负，虫量不降反升，异常评估单不许归档");
        }
        int rows = pestEffectService.archiveTPestEffect(id);
        return rows > 0 ? success("归档完成") : error("归档失败，请刷新后重试");
    }

    @Log(title = "防治效果评估单删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("pest:pestEffect:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pestEffectService.deleteTPestEffectByIds(ids));
    }

    /**
     * 保存前校验：单号、测报点、作业单号、前后虫量、评估日期、评估人为必填项。
     * 测报点只许挂启用中的点；防治作业单必须存在且已完结（药液已用完），
     * 没完结的作业单不许拿来评估。
     * 编辑时允许保留原记录已引用的撤点测报点；改选其它失效档案则拒绝，
     * 避免错误数据进台账
     */
    private AjaxResult validate(TPestEffect record, TPestEffect dbRecord) {
        if (StringUtils.isEmpty(record.getEffectNo())) {
            return error("评估单号不能为空");
        }
        if (record.getStationId() == null) {
            return error("请选择测报点");
        }
        TPestStation station = pestStationService.selectTPestStationById(record.getStationId());
        if (station == null) {
            return error("测报点不存在");
        }
        boolean keepStation = dbRecord != null && record.getStationId().equals(dbRecord.getStationId());
        if (!keepStation && station.getStatus() != null && station.getStatus() == 1) {
            return error("测报点已撤点，不能登记评估单");
        }
        if (StringUtils.isEmpty(record.getSprayNo())) {
            return error("请选择对应的防治作业单");
        }
        boolean keepSpray = dbRecord != null && record.getSprayNo().equals(dbRecord.getSprayNo());
        if (!keepSpray) {
            TPestSpray spray = pestSprayService.selectTPestSprayBySprayNo(record.getSprayNo());
            if (spray == null) {
                return error("防治作业单不存在");
            }
            if (spray.getSprayStatus() == null || spray.getSprayStatus() != 1) {
                return error("防治作业单尚未完结归档，不能拿来评估");
            }
        }
        if (record.getBeforeCount() == null) {
            return error("防治前虫量不能为空");
        }
        if (record.getBeforeCount() <= 0) {
            return error("防治前虫量为0，无法计算虫口减退率，请核实后再登记");
        }
        if (record.getAfterCount() == null) {
            return error("防治后虫量不能为空");
        }
        if (record.getAfterCount() < 0) {
            return error("防治后虫量不能为负数");
        }
        if (record.getAssessDate() == null) {
            return error("评估日期不能为空");
        }
        if (StringUtils.isEmpty(record.getAssessBy())) {
            return error("评估人不能为空");
        }
        return null;
    }
}
