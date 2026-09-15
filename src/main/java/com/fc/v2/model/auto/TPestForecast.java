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
 * 防治适期预报单对象 t_pest_forecast
 *
 * @author fuce
 * @date 2026-09-13
 */
@TableName("t_pest_forecast")
@ApiModel(value = "TPestForecast", description = "防治适期预报单")
public class TPestForecast implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 预报单号 */
    @TableField("forecast_no")
    @ApiModelProperty(value = "预报单号")
    private String forecastNo;

    /** 测报点ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("station_id")
    @ApiModelProperty(value = "测报点ID")
    private Long stationId;

    /** 测报点编号(冗余，以档案为准) */
    @TableField("station_code")
    @ApiModelProperty(value = "测报点编号(冗余，以档案为准)")
    private String stationCode;

    /** 病虫名称 */
    @TableField("pest_name")
    @ApiModelProperty(value = "病虫名称")
    private String pestName;

    /** 预报日期 */
    @TableField("forecast_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "预报日期")
    private Date forecastDate;

    /** 预报有效期至 */
    @TableField("valid_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "预报有效期至")
    private Date validDate;

    /** 剩余天数 */
    @TableField("remain_days")
    @ApiModelProperty(value = "剩余天数")
    private Integer remainDays;

    /** 预报登记人 */
    @TableField("forecast_by")
    @ApiModelProperty(value = "预报登记人")
    private String forecastBy;

    /** 发布状态 0待发布 1已发布 2已关闭 */
    @TableField("forecast_status")
    @ApiModelProperty(value = "发布状态 0待发布 1已发布 2已关闭")
    private Integer forecastStatus;

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

    public String getForecastNo() {
        return forecastNo;
    }

    public void setForecastNo(String forecastNo) {
        this.forecastNo = forecastNo;
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

    public String getPestName() {
        return pestName;
    }

    public void setPestName(String pestName) {
        this.pestName = pestName;
    }

    public Date getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(Date forecastDate) {
        this.forecastDate = forecastDate;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    public Integer getRemainDays() {
        return remainDays;
    }

    public void setRemainDays(Integer remainDays) {
        this.remainDays = remainDays;
    }

    public String getForecastBy() {
        return forecastBy;
    }

    public void setForecastBy(String forecastBy) {
        this.forecastBy = forecastBy;
    }

    public Integer getForecastStatus() {
        return forecastStatus;
    }

    public void setForecastStatus(Integer forecastStatus) {
        this.forecastStatus = forecastStatus;
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
