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
 * 测报点巡查记录对象 t_pest_patrol
 *
 * @author fuce
 * @date 2026-09-13
 */
@TableName("t_pest_patrol")
@ApiModel(value = "TPestPatrol", description = "测报点巡查记录")
public class TPestPatrol implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 巡查单号 */
    @TableField("patrol_no")
    @ApiModelProperty(value = "巡查单号")
    private String patrolNo;

    /** 测报点ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("station_id")
    @ApiModelProperty(value = "测报点ID")
    private Long stationId;

    /** 测报点编号(冗余，以档案为准) */
    @TableField("station_code")
    @ApiModelProperty(value = "测报点编号(冗余，以档案为准)")
    private String stationCode;

    /** 巡查日期 */
    @TableField("patrol_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "巡查日期")
    private Date patrolDate;

    /** 巡查类型 日常巡查/灯诱监测/专项排查 */
    @TableField("patrol_type")
    @ApiModelProperty(value = "巡查类型 日常巡查/灯诱监测/专项排查")
    private String patrolType;

    /** 巡查人 */
    @TableField("patrol_by")
    @ApiModelProperty(value = "巡查人")
    private String patrolBy;

    /** 巡查进度 0待巡查 1已巡查 2已归档 */
    @TableField("patrol_status")
    @ApiModelProperty(value = "巡查进度 0待巡查 1已巡查 2已归档")
    private Integer patrolStatus;

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

    public String getPatrolNo() {
        return patrolNo;
    }

    public void setPatrolNo(String patrolNo) {
        this.patrolNo = patrolNo;
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

    public Date getPatrolDate() {
        return patrolDate;
    }

    public void setPatrolDate(Date patrolDate) {
        this.patrolDate = patrolDate;
    }

    public String getPatrolType() {
        return patrolType;
    }

    public void setPatrolType(String patrolType) {
        this.patrolType = patrolType;
    }

    public String getPatrolBy() {
        return patrolBy;
    }

    public void setPatrolBy(String patrolBy) {
        this.patrolBy = patrolBy;
    }

    public Integer getPatrolStatus() {
        return patrolStatus;
    }

    public void setPatrolStatus(Integer patrolStatus) {
        this.patrolStatus = patrolStatus;
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
