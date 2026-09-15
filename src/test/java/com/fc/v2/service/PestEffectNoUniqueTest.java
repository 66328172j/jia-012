package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.mapper.auto.TPestEffectMapper;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.model.auto.TPestEffect;
import com.fc.v2.service.impl.TPestEffectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 防治效果评估单：评估单号唯一、同一防治作业单只允许一张评估单 单元测试
 * 链路口径：新增、编辑保存前都查重；查重只统计正常记录且编辑时排除自身，
 * 保证老记录未改动可以正常保存，改号撞到其它记录才被拦截
 *
 * @author jiabo
 * @date 2026-09-15
 */
@ExtendWith(MockitoExtension.class)
public class PestEffectNoUniqueTest {

    @Mock
    private TPestEffectMapper effectMapper;

    @Mock
    private TPestStationMapper stationMapper;

    private TPestEffectServiceImpl effectService;

    @BeforeEach
    public void setUp() {
        effectService = new TPestEffectServiceImpl();
        ReflectionTestUtils.setField(effectService, "baseMapper", effectMapper);
        ReflectionTestUtils.setField(effectService, "pestStationMapper", stationMapper);
    }

    private TPestEffect effect(String effectNo, String sprayNo) {
        TPestEffect record = new TPestEffect();
        record.setEffectNo(effectNo);
        record.setSprayNo(sprayNo);
        return record;
    }

    @Test
    public void duplicateEffectNoFoundOnAdd() {
        // 新增不带主键，库中已有同号正常记录，必须判重
        when(effectMapper.selectList(any())).thenReturn(Collections.singletonList(effect("PE-20260915-001", "SP-0001")));
        assertEquals(1, effectService.checkEffectNoUnique(effect("PE-20260915-001", "SP-0002")));
    }

    @Test
    public void duplicateSprayNoFoundOnAdd() {
        // 同一防治作业单已出过评估单，再评必须拦截
        when(effectMapper.selectList(any())).thenReturn(Collections.singletonList(effect("PE-20260915-001", "SP-0001")));
        assertEquals(1, effectService.checkSprayNoUnique(effect("PE-20260915-002", "SP-0001")));
    }

    @Test
    public void editWithUnchangedSprayNoPasses() {
        // 编辑老记录、作业单未改动：查重排除自身后查不到别人，放行
        TPestEffect record = effect("PE-20260915-001", "SP-0001");
        record.setId(10L);
        when(effectMapper.selectList(any())).thenReturn(Collections.emptyList());
        assertEquals(0, effectService.checkSprayNoUnique(record));

        // 且查询条件必须带上 排除自身 + 只统计正常记录
        ArgumentCaptor<QueryWrapper> captor = ArgumentCaptor.forClass(QueryWrapper.class);
        verify(effectMapper).selectList(captor.capture());
        String sqlSegment = captor.getValue().getSqlSegment();
        assertTrue(sqlSegment.contains("id <>"), "编辑查重必须排除记录自身");
        assertTrue(sqlSegment.contains("del_flag ="), "查重只统计正常记录");
    }

    @Test
    public void editWithClashingSprayNoRejected() {
        // 编辑把作业单改成别的评估单已占用的单号：查到的是别人（id不同），必须判重
        TPestEffect record = effect("PE-20260915-001", "SP-0002");
        record.setId(10L);
        TPestEffect other = effect("PE-20260915-002", "SP-0002");
        other.setId(20L);
        when(effectMapper.selectList(any())).thenReturn(Collections.singletonList(other));
        assertEquals(1, effectService.checkSprayNoUnique(record));
    }

    @Test
    public void addWithoutIdDoesNotExcludeSelf() {
        // 新增场景没有主键，查询条件不得出现排除自身的 id 条件
        when(effectMapper.selectList(any())).thenReturn(Collections.emptyList());
        effectService.checkSprayNoUnique(effect("PE-20260915-001", "SP-0001"));

        ArgumentCaptor<QueryWrapper> captor = ArgumentCaptor.forClass(QueryWrapper.class);
        verify(effectMapper).selectList(captor.capture());
        assertFalse(captor.getValue().getSqlSegment().contains("id <>"), "新增查重不应带主键排除条件");
    }

    @Test
    public void blankNoSkipsQuery() {
        // 单号为空的记录由必填校验拦截，唯一校验直接放行且不应查询数据库
        assertEquals(0, effectService.checkEffectNoUnique(effect("", "SP-0001")));
        assertEquals(0, effectService.checkSprayNoUnique(effect("PE-20260915-001", "")));
        assertEquals(0, effectService.checkSprayNoUnique(null));
        verify(effectMapper, never()).selectList(any());
    }
}
