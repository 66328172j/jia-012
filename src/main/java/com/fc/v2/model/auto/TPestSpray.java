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
import java.util.Date;

/**
 * 防治作业药液配制台账对象 t_pest_spray
 *
 * @author fuce
 * @date 2026-09-13
 */
@TableName("t_pest_spray")
@ApiModel(value = "TPestSpray", description = "防治作业药液配制台账")
public class TPestSpray implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 配制单号 */
    @TableField("spray_no")
    @ApiModelProperty(value = "配制单号")
    private String sprayNo;

    /** 关联预报单ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("forecast_id")
    @ApiModelProperty(value = "关联预报单ID")
    private Long forecastId;

    /** 预报单号(冗余，以预报单为准) */
    @TableField("forecast_no")
    @ApiModelProperty(value = "预报单号(冗余，以预报单为准)")
    private String forecastNo;

    /** 农药名称 */
    @TableField("pesticide_name")
    @ApiModelProperty(value = "农药名称")
    private String pesticideName;

    /** 配制药液量(升) */
    @TableField("dose_volume")
    @ApiModelProperty(value = "配制药液量(升)")
    private Integer doseVolume;

    /** 已喷施量(升) */
    @TableField("spray_volume")
    @ApiModelProperty(value = "已喷施量(升)")
    private Integer sprayVolume;

    /** 剩余药液量(升) */
    @TableField("remain_volume")
    @ApiModelProperty(value = "剩余药液量(升)")
    private Integer remainVolume;

    /** 配制人 */
    @TableField("prepare_by")
    @ApiModelProperty(value = "配制人")
    private String prepareBy;

    /** 配制状态 0可继续喷施 1已用完 */
    @TableField("spray_status")
    @ApiModelProperty(value = "配制状态 0可继续喷施 1已用完")
    private Integer sprayStatus;

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

    public String getSprayNo() {
        return sprayNo;
    }

    public void setSprayNo(String sprayNo) {
        this.sprayNo = sprayNo;
    }

    public Long getForecastId() {
        return forecastId;
    }

    public void setForecastId(Long forecastId) {
        this.forecastId = forecastId;
    }

    public String getForecastNo() {
        return forecastNo;
    }

    public void setForecastNo(String forecastNo) {
        this.forecastNo = forecastNo;
    }

    public String getPesticideName() {
        return pesticideName;
    }

    public void setPesticideName(String pesticideName) {
        this.pesticideName = pesticideName;
    }

    public Integer getDoseVolume() {
        return doseVolume;
    }

    public void setDoseVolume(Integer doseVolume) {
        this.doseVolume = doseVolume;
    }

    public Integer getSprayVolume() {
        return sprayVolume;
    }

    public void setSprayVolume(Integer sprayVolume) {
        this.sprayVolume = sprayVolume;
    }

    public Integer getRemainVolume() {
        return remainVolume;
    }

    public void setRemainVolume(Integer remainVolume) {
        this.remainVolume = remainVolume;
    }

    public String getPrepareBy() {
        return prepareBy;
    }

    public void setPrepareBy(String prepareBy) {
        this.prepareBy = prepareBy;
    }

    public Integer getSprayStatus() {
        return sprayStatus;
    }

    public void setSprayStatus(Integer sprayStatus) {
        this.sprayStatus = sprayStatus;
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
