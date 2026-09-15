package com.fc.v2.model.auto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 发生程度分级规则对象 t_pest_rule
 *
 * @author fuce
 * @date 2026-09-13
 */
@TableName("t_pest_rule")
@ApiModel(value = "TPestRule", description = "发生程度分级规则")
public class TPestRule implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 规则编号 */
    @TableField("rule_code")
    @ApiModelProperty(value = "规则编号")
    private String ruleCode;

    /** 规则名称 */
    @TableField("rule_name")
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    /** 一级上限(病株率%) */
    @TableField("dev1_max")
    @ApiModelProperty(value = "一级上限(病株率%)")
    private BigDecimal dev1Max;

    /** 二级上限(病株率%) */
    @TableField("dev2_max")
    @ApiModelProperty(value = "二级上限(病株率%)")
    private BigDecimal dev2Max;

    /** 三级上限(病株率%) */
    @TableField("dev3_max")
    @ApiModelProperty(value = "三级上限(病株率%)")
    private BigDecimal dev3Max;

    /** 规则状态 0启用 1停用 */
    @TableField("status")
    @ApiModelProperty(value = "规则状态 0启用 1停用")
    private Integer status;

    /** 删除标记 0正常 1删除 */
    @TableField("del_flag")
    @ApiModelProperty(value = "删除标记 0正常 1删除")
    private Integer delFlag;

    /** 创建者 */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 更新者 */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /** 备注 */
    @TableField("remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public BigDecimal getDev1Max() {
        return dev1Max;
    }

    public void setDev1Max(BigDecimal dev1Max) {
        this.dev1Max = dev1Max;
    }

    public BigDecimal getDev2Max() {
        return dev2Max;
    }

    public void setDev2Max(BigDecimal dev2Max) {
        this.dev2Max = dev2Max;
    }

    public BigDecimal getDev3Max() {
        return dev3Max;
    }

    public void setDev3Max(BigDecimal dev3Max) {
        this.dev3Max = dev3Max;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
