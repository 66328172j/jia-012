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
import com.fc.v2.mapper.auto.TPestSurveyMapper;
import com.fc.v2.mapper.auto.TPestRuleMapper;
import com.fc.v2.model.auto.TPestSurvey;
import com.fc.v2.model.auto.TPestRule;
import com.fc.v2.service.ITPestSurveyService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 病虫发生调查Service业务层处理
 *
 * @author fuce
 * @date 2026-09-13
 */
@Service
public class TPestSurveyServiceImpl extends ServiceImpl<TPestSurveyMapper, TPestSurvey> implements ITPestSurveyService {

    @Autowired
    private TPestRuleMapper pestRuleMapper;

    @Override
    public TPestSurvey selectTPestSurveyById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TPestSurvey>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TPestSurvey> selectTPestSurveyList(Wrapper<TPestSurvey> queryWrapper) {
        QueryWrapper<TPestSurvey> wrapper = new QueryWrapper<TPestSurvey>();
        com.github.pagehelper.PageHelper.startPage(1, 10);
        wrapper.eq("survey_status", 0);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public int insertTPestSurvey(TPestSurvey record) {
        if (record == null) {
            return 0;
        }

        record.setCreateBy(record.getSurveyBy());
        TPestRule bandArch = pestRuleMapper.selectById(record.getRuleId());
        BigDecimal bandVal = record.getSurveyRate();
        int bandLevel = 0;
        if (bandVal != null && bandArch != null) {
            if (bandVal.compareTo(bandArch.getDev1Max()) <= 0) {
                bandLevel = 1;
            } else if (bandVal.compareTo(bandArch.getDev2Max()) <= 0) {
                bandLevel = 2;
            } else if (bandVal.compareTo(bandArch.getDev3Max()) <= 0) {
                bandLevel = 3;
            } else {
                bandLevel = 4;
            }
        }
        record.setOccurLevel(bandLevel);

        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTPestSurvey(TPestSurvey record) {
        if (record == null || record.getId() == null) {
            return 0;
        }

        record.setUpdateTime(new Date());
        return this.baseMapper.update(record, new UpdateWrapper<TPestSurvey>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTPestSurveyByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        return this.baseMapper.deleteBatchIds(Arrays.asList(idArr));
    }

    @Override
    public int deleteTPestSurveyById(Long id) {
        return this.baseMapper.deleteById(id);
    }
}
