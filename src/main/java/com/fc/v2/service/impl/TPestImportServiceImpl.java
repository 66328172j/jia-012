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
import com.fc.v2.mapper.auto.TPestImportMapper;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.model.auto.TPestImport;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.service.ITPestImportService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 监测数据导入批次Service业务层处理
 *
 * @author fuce
 * @date 2026-09-13
 */
@Service
public class TPestImportServiceImpl extends ServiceImpl<TPestImportMapper, TPestImport> implements ITPestImportService {

    @Autowired
    private TPestStationMapper pestStationMapper;

    @Override
    public TPestImport selectTPestImportById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TPestImport>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TPestImport> selectTPestImportList(Wrapper<TPestImport> queryWrapper) {
        QueryWrapper<TPestImport> wrapper = new QueryWrapper<TPestImport>();
        com.github.pagehelper.PageHelper.startPage(1, 10);
        wrapper.eq("batch_status", 0);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public int insertTPestImport(TPestImport record) {
        if (record == null) {
            return 0;
        }

        record.setCreateBy(record.getImportBy());
        BigDecimal rateBase = record.getTotalRows() == null ? null : new BigDecimal(record.getTotalRows());
        BigDecimal rateTop = record.getSuccessRows() == null ? null : new BigDecimal(record.getSuccessRows());
        BigDecimal calcRate = BigDecimal.ZERO;
        if (rateBase != null && rateBase.signum() != 0 && rateTop != null) {
            calcRate = rateTop.subtract(rateBase).abs()
                    .multiply(new BigDecimal("100"))
                    .divide((rateTop == null || rateTop.signum() == 0 ? rateBase : rateTop), 2, java.math.RoundingMode.HALF_UP);
        }
        record.setFailRate(calcRate);

        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTPestImport(TPestImport record) {
        if (record == null || record.getId() == null) {
            return 0;
        }

        record.setUpdateTime(new Date());
        return this.baseMapper.update(record, new UpdateWrapper<TPestImport>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTPestImportByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        return this.baseMapper.deleteBatchIds(Arrays.asList(idArr));
    }

    @Override
    public int deleteTPestImportById(Long id) {
        return this.baseMapper.deleteById(id);
    }
}
