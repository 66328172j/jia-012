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
 * 防治效果评估单对象 t_pest_effect
 *
 * @author fuce
 * @date 2026-09-13
 */
@TableName("t_pest_effect")
@ApiModel(value = "TPestEffect", description = "防治效果评估单")
public class TPestEffect implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 评估单号 */
    @TableField("effect_no")
    @ApiModelProperty(value = "评估单号")
    private String effectNo;

    /** 测报点ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("station_id")
    @ApiModelProperty(value = "测报点ID")
    private Long stationId;

    /** 测报点编号(冗余，以档案为准) */
    @TableField("station_code")
    @ApiModelProperty(value = "测报点编号(冗余，以档案为准)")
    private String stationCode;

    /** 关联防治作业单号 */
    @TableField("spray_no")
    @ApiModelProperty(value = "关联防治作业单号")
    private String sprayNo;

    /** 防治前虫量(头/百株) */
    @TableField("before_count")
    @ApiModelProperty(value = "防治前虫量(头/百株)")
    private Integer beforeCount;

    /** 防治后虫量(头/百株) */
    @TableField("after_count")
    @ApiModelProperty(value = "防治后虫量(头/百株)")
    private Integer afterCount;

    /** 虫口减退率(%) */
    @TableField("reduce_rate")
    @ApiModelProperty(value = "虫口减退率(%)")
    private BigDecimal reduceRate;

    /** 评估日期 */
    @TableField("assess_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "评估日期")
    private Date assessDate;

    /** 评估人 */
    @TableField("assess_by")
    @ApiModelProperty(value = "评估人")
    private String assessBy;

    /** 评估状态 0待评估 1已评估 2已归档 */
    @TableField("effect_status")
    @ApiModelProperty(value = "评估状态 0待评估 1已评估 2已归档")
    private Integer effectStatus;

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

    public String getEffectNo() {
        return effectNo;
    }

    public void setEffectNo(String effectNo) {
        this.effectNo = effectNo;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getSprayNo() {
        return sprayNo;
    }

    public void setSprayNo(String sprayNo) {
        this.sprayNo = sprayNo;
    }

    public Integer getBeforeCount() {
        return beforeCount;
    }

    public void setBeforeCount(Integer beforeCount) {
        this.beforeCount = beforeCount;
    }

    public Integer getAfterCount() {
        return afterCount;
    }

    public void setAfterCount(Integer afterCount) {
        this.afterCount = afterCount;
    }

    public BigDecimal getReduceRate() {
        return reduceRate;
    }

    public void setReduceRate(BigDecimal reduceRate) {
        this.reduceRate = reduceRate;
    }

    public Date getAssessDate() {
        return assessDate;
    }

    public void setAssessDate(Date assessDate) {
        this.assessDate = assessDate;
    }

    public String getAssessBy() {
        return assessBy;
    }

    public void setAssessBy(String assessBy) {
        this.assessBy = assessBy;
    }

    public Integer getEffectStatus() {
        return effectStatus;
    }

    public void setEffectStatus(Integer effectStatus) {
        this.effectStatus = effectStatus;
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
