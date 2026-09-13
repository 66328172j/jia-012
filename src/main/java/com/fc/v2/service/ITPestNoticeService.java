package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TPestNotice;

import java.util.List;

/**
 * 防治通知单 Service接口
 *
 * @author fuce
 * @date 2026-09-13
 */
public interface ITPestNoticeService {

    /** 按主键查询 */
    TPestNotice selectTPestNoticeById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TPestNotice> selectTPestNoticeList(Wrapper<TPestNotice> queryWrapper);

    /** 新增 */
    int insertTPestNotice(TPestNotice record);

    /** 修改 */
    int updateTPestNotice(TPestNotice record);

    /** 批量删除 */
    int deleteTPestNoticeByIds(String ids);

    /** 按主键删除 */
    int deleteTPestNoticeById(Long id);

    /** 通知状态流转：0发布 1终止 */
    int advanceTPestNotice(Long id, Integer action, String remark);
}
