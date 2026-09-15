package com.fc.v2.service;

import com.fc.v2.mapper.auto.TPestEffectMapper;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.model.auto.TPestEffect;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.service.impl.TPestEffectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 防治效果评估单：虫口减退率系统口径、零虫量拒存、异常单卡归档、状态机 单元测试
 * 口径：减退率 = (防治前-防治后)/防治前×100，保留两位；
 * 防治前虫量为0不许存；减退率为负标异常、不许归档；已归档锁住
 *
 * @author jiabo
 * @date 2026-09-15
 */
@ExtendWith(MockitoExtension.class)
public class PestEffectServiceTest {

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

        TPestStation station = new TPestStation();
        station.setId(1L);
        station.setStationCode("PS-0001");
        station.setStatus(0);
        lenient().when(stationMapper.selectById(1L)).thenReturn(station);

        lenient().when(effectMapper.insert(any(TPestEffect.class))).thenReturn(1);
    }

    private TPestEffect effect(Integer before, Integer after) {
        TPestEffect record = new TPestEffect();
        record.setEffectNo("PE-TEST-001");
        record.setStationId(1L);
        record.setSprayNo("SP-0001");
        record.setBeforeCount(before);
        record.setAfterCount(after);
        record.setAssessDate(new Date());
        record.setAssessBy("王评估");
        return record;
    }

    @Test
    public void reduceRateComputedBySystem() {
        // 100 -> 20：减退率 80.00%，前端伪造的减退率和状态一律被系统值覆盖
        TPestEffect record = effect(100, 20);
        record.setReduceRate(new BigDecimal("999.99"));
        record.setEffectStatus(2);
        effectService.insertTPestEffect(record);

        assertEquals(new BigDecimal("80.00"), record.getReduceRate(), "减退率应由系统计算，忽略前端传值");
        assertEquals(0, record.getEffectStatus().intValue(), "新建评估单默认待评估");
        assertEquals(0, record.getDelFlag().intValue());
        assertEquals("PS-0001", record.getStationCode(), "测报点编号以档案冗余回填");
    }

    @Test
    public void reduceRateRoundingHalfUp() {
        // 3 -> 1：66.666...% 四舍五入保留两位 = 66.67%
        TPestEffect record = effect(3, 1);
        effectService.insertTPestEffect(record);
        assertEquals(new BigDecimal("66.67"), record.getReduceRate());
    }

    @Test
    public void negativeRateStoredAsIs() {
        // 10 -> 15：虫量不降反升，减退率 -50.00% 如实落库，不许取绝对值遮丑
        TPestEffect record = effect(10, 15);
        effectService.insertTPestEffect(record);
        assertEquals(new BigDecimal("-50.00"), record.getReduceRate(), "减退率为负须如实落库标异常");
    }

    @Test
    public void zeroBeforeCountRejected() {
        // 防治前虫量为0除不尽，不许直接存，insert 一次都不能执行
        TPestEffect record = effect(0, 5);
        int rows = effectService.insertTPestEffect(record);

        assertEquals(0, rows, "防治前虫量为0不许保存");
        verify(effectMapper, never()).insert(any(TPestEffect.class));
    }

    @Test
    public void missingCountRejected() {
        assertEquals(0, effectService.insertTPestEffect(effect(null, 5)), "防治前虫量为空不许保存");
        assertEquals(0, effectService.insertTPestEffect(effect(100, null)), "防治后虫量为空不许保存");
        assertEquals(0, effectService.insertTPestEffect(effect(100, -1)), "防治后虫量为负不许保存");
        verify(effectMapper, never()).insert(any(TPestEffect.class));
    }

    @Test
    public void editPendingRecordRecomputesRateAndKeepsStatus() {
        TPestEffect dbRecord = effect(100, 20);
        dbRecord.setId(10L);
        dbRecord.setReduceRate(new BigDecimal("80.00"));
        dbRecord.setEffectStatus(1);
        when(effectMapper.selectOne(any())).thenReturn(dbRecord);
        when(effectMapper.update(any(TPestEffect.class), any())).thenReturn(1);

        // 已评估记录改虫量，允许保存：减退率重算，评估状态不由编辑接口改写
        TPestEffect update = effect(100, 40);
        update.setId(10L);
        int rows = effectService.updateTPestEffect(update);

        assertEquals(1, rows, "未归档评估单应允许编辑保存");
        assertEquals(new BigDecimal("60.00"), update.getReduceRate(), "改虫量后须重算减退率");
        assertNull(update.getEffectStatus(), "评估状态只能走评估/归档动作，编辑接口不得改写");
    }

    @Test
    public void editArchivedRecordRejected() {
        // 已归档评估单已锁定：编辑保存直接拒绝，update 一次都不能执行
        TPestEffect dbRecord = effect(100, 20);
        dbRecord.setId(10L);
        dbRecord.setReduceRate(new BigDecimal("80.00"));
        dbRecord.setEffectStatus(2);
        when(effectMapper.selectOne(any())).thenReturn(dbRecord);

        TPestEffect update = effect(100, 40);
        update.setId(10L);
        int rows = effectService.updateTPestEffect(update);

        assertEquals(0, rows, "已归档评估单不允许编辑保存");
        verify(effectMapper, never()).update(any(TPestEffect.class), any());
        assertEquals(new BigDecimal("80.00"), dbRecord.getReduceRate(), "归档单的减退率不得变化");
    }

    @Test
    public void editZeroBeforeCountRejected() {
        TPestEffect dbRecord = effect(100, 20);
        dbRecord.setId(10L);
        dbRecord.setEffectStatus(0);
        when(effectMapper.selectOne(any())).thenReturn(dbRecord);

        TPestEffect update = effect(0, 5);
        update.setId(10L);
        int rows = effectService.updateTPestEffect(update);

        assertEquals(0, rows, "编辑时防治前虫量为0同样不许保存");
        verify(effectMapper, never()).update(any(TPestEffect.class), any());
    }

    @Test
    public void evaluateOnlySucceedsOnPendingRecord() {
        // mapper 按 effect_status=0 条件更新；非待评估记录更新行数为0，接口据此提示
        when(effectMapper.update(any(), any())).thenReturn(1);
        assertEquals(1, effectService.evaluateTPestEffect(10L));

        when(effectMapper.update(any(), any())).thenReturn(0);
        assertEquals(0, effectService.evaluateTPestEffect(11L));
    }

    @Test
    public void archiveSucceedsOnEvaluatedRecord() {
        TPestEffect dbRecord = effect(100, 20);
        dbRecord.setId(10L);
        dbRecord.setReduceRate(new BigDecimal("80.00"));
        dbRecord.setEffectStatus(1);
        when(effectMapper.selectOne(any())).thenReturn(dbRecord);
        when(effectMapper.update(any(), any())).thenReturn(1);

        assertEquals(1, effectService.archiveTPestEffect(10L), "已评估且减退率正常的评估单应允许归档");
    }

    @Test
    public void archiveNegativeRateRejected() {
        // 减退率为负的异常单不许走归档，update 一次都不能执行
        TPestEffect dbRecord = effect(10, 15);
        dbRecord.setId(10L);
        dbRecord.setReduceRate(new BigDecimal("-50.00"));
        dbRecord.setEffectStatus(1);
        when(effectMapper.selectOne(any())).thenReturn(dbRecord);

        int rows = effectService.archiveTPestEffect(10L);

        assertEquals(0, rows, "虫量不降反升的异常单不许归档");
        verify(effectMapper, never()).update(any(), any());
    }

    @Test
    public void archiveOnlyFromEvaluatedStatus() {
        // 待评估记录直接归档：拒绝
        TPestEffect pending = effect(100, 20);
        pending.setId(10L);
        pending.setReduceRate(new BigDecimal("80.00"));
        pending.setEffectStatus(0);
        when(effectMapper.selectOne(any())).thenReturn(pending);
        assertEquals(0, effectService.archiveTPestEffect(10L), "待评估记录不许直接归档");

        // 已归档记录重复归档：拒绝
        TPestEffect archived = effect(100, 20);
        archived.setId(11L);
        archived.setReduceRate(new BigDecimal("80.00"));
        archived.setEffectStatus(2);
        when(effectMapper.selectOne(any())).thenReturn(archived);
        assertEquals(0, effectService.archiveTPestEffect(11L), "已归档记录不许重复归档");

        verify(effectMapper, never()).update(any(), any());
    }

    @Test
    public void deleteIsSoftAndSkipsArchived() {
        // 删除走 del_flag 软删且条件排除已归档，绝不物理删除
        when(effectMapper.update(any(), any())).thenReturn(1);
        assertEquals(1, effectService.deleteTPestEffectByIds("1,2"));
        verify(effectMapper).update(any(), any());
        verify(effectMapper, never()).deleteBatchIds(any());
    }
}
