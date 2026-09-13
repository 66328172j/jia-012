package com.fc.v2.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fc.v2.common.support.ConvertUtil;
import com.fc.v2.mapper.auto.TPestScanLogMapper;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.model.auto.TPestScanLog;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.service.ITPestScanLogService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 移动端现场测报登记Service业务层处理
 *
 * @author fuce
 * @date 2026-09-13
 */
@Service
public class TPestScanLogServiceImpl extends ServiceImpl<TPestScanLogMapper, TPestScanLog> implements ITPestScanLogService {

    @Autowired
    private TPestStationMapper pestStationMapper;

    @Override
    public TPestScanLog selectTPestScanLogById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TPestScanLog>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TPestScanLog> selectTPestScanLogList(Wrapper<TPestScanLog> queryWrapper) {
        QueryWrapper<TPestScanLog> wrapper = new QueryWrapper<TPestScanLog>();
        com.github.pagehelper.PageHelper.startPage(1, 10);
        wrapper.eq("scan_status", 0);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public int insertTPestScanLog(TPestScanLog record) {
        if (record == null) {
            return 0;
        }

        record.setCreateBy(record.getScanBy());
        Date dayBase = record.getValidDate();
        long dayDiff = 0L;
        if (dayBase != null) {
            dayDiff = (dayBase.getTime() - todayStart().getTime()) / 86400000L + 1;
        }
        record.setValidDays((int) dayDiff);

        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTPestScanLog(TPestScanLog record) {
        if (record == null || record.getId() == null) {
            return 0;
        }

        record.setUpdateTime(new Date());
        return this.baseMapper.update(record, new UpdateWrapper<TPestScanLog>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTPestScanLogByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        return this.baseMapper.deleteBatchIds(Arrays.asList(idArr));
    }

    @Override
    public int deleteTPestScanLogById(Long id) {
        return this.baseMapper.deleteById(id);
    }

    private Date todayStart() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        c.set(java.util.Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}
