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
 * 防治覆盖率统计对象 t_pest_stat
 *
 * @author fuce
 * @date 2026-09-13
 */
@TableName("t_pest_stat")
@ApiModel(value = "TPestStat", description = "防治覆盖率统计")
public class TPestStat implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 统计单号 */
    @TableField("stat_no")
    @ApiModelProperty(value = "统计单号")
    private String statNo;

    /** 测报点ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("station_id")
    @ApiModelProperty(value = "测报点ID")
    private Long stationId;

    /** 测报点编号(冗余，以档案为准) */
    @TableField("station_code")
    @ApiModelProperty(value = "测报点编号(冗余，以档案为准)")
    private String stationCode;

    /** 统计月份 */
    @TableField("stat_month")
    @ApiModelProperty(value = "统计月份")
    private String statMonth;

    /** 应防治地块数 */
    @TableField("should_count")
    @ApiModelProperty(value = "应防治地块数")
    private Integer shouldCount;

    /** 已防治地块数 */
    @TableField("done_count")
    @ApiModelProperty(value = "已防治地块数")
    private Integer doneCount;

    /** 防治覆盖率(%) */
    @TableField("cover_rate")
    @ApiModelProperty(value = "防治覆盖率(%)")
    private BigDecimal coverRate;

    /** 统计状态 0待确认 1已归档 */
    @TableField("stat_status")
    @ApiModelProperty(value = "统计状态 0待确认 1已归档")
    private Integer statStatus;

    /** 统计人 */
    @TableField("stat_by")
    @ApiModelProperty(value = "统计人")
    private String statBy;

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

    public String getStatNo() {
        return statNo;
    }

    public void setStatNo(String statNo) {
        this.statNo = statNo;
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

    public String getStatMonth() {
        return statMonth;
    }

    public void setStatMonth(String statMonth) {
        this.statMonth = statMonth;
    }

    public Integer getShouldCount() {
        return shouldCount;
    }

    public void setShouldCount(Integer shouldCount) {
        this.shouldCount = shouldCount;
    }

    public Integer getDoneCount() {
        return doneCount;
    }

    public void setDoneCount(Integer doneCount) {
        this.doneCount = doneCount;
    }

    public BigDecimal getCoverRate() {
        return coverRate;
    }

    public void setCoverRate(BigDecimal coverRate) {
        this.coverRate = coverRate;
    }

    public Integer getStatStatus() {
        return statStatus;
    }

    public void setStatStatus(Integer statStatus) {
        this.statStatus = statStatus;
    }

    public String getStatBy() {
        return statBy;
    }

    public void setStatBy(String statBy) {
        this.statBy = statBy;
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
