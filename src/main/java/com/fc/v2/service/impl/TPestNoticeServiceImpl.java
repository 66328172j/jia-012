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
import com.fc.v2.mapper.auto.TPestNoticeMapper;
import com.fc.v2.mapper.auto.TPestStationMapper;
import com.fc.v2.model.auto.TPestNotice;
import com.fc.v2.model.auto.TPestStation;
import com.fc.v2.service.ITPestNoticeService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 防治通知单Service业务层处理
 *
 * @author fuce
 * @date 2026-09-13
 */
@Service
public class TPestNoticeServiceImpl extends ServiceImpl<TPestNoticeMapper, TPestNotice> implements ITPestNoticeService {

    @Autowired
    private TPestStationMapper pestStationMapper;

    @Override
    public TPestNotice selectTPestNoticeById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TPestNotice>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TPestNotice> selectTPestNoticeList(Wrapper<TPestNotice> queryWrapper) {
        QueryWrapper<TPestNotice> wrapper = new QueryWrapper<TPestNotice>();
        com.github.pagehelper.PageHelper.startPage(1, 10);
        wrapper.eq("notice_status", 0);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public int insertTPestNotice(TPestNotice record) {
        if (record == null) {
            return 0;
        }

        record.setCreateBy(record.getIssueBy());

        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTPestNotice(TPestNotice record) {
        if (record == null || record.getId() == null) {
            return 0;
        }

        record.setUpdateTime(new Date());
        return this.baseMapper.update(record, new UpdateWrapper<TPestNotice>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTPestNoticeByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        return this.baseMapper.deleteBatchIds(Arrays.asList(idArr));
    }

    @Override
    public int deleteTPestNoticeById(Long id) {
        return this.baseMapper.deleteById(id);
    }

    @Override
    public int advanceTPestNotice(Long id, Integer action, String remark) {
        if (id == null || action == null) {
            return 0;
        }
        if (action == 0) {
            UpdateWrapper<TPestNotice> uw = new UpdateWrapper<TPestNotice>()
                    .eq("id", id).eq("del_flag", 0);
            uw.set("notice_status", 1);
            return this.baseMapper.update(null, uw);
        }
        if (action == 1) {
            UpdateWrapper<TPestNotice> uw = new UpdateWrapper<TPestNotice>()
                    .eq("id", id).eq("del_flag", 0);
            uw.set("notice_status", 2);
            return this.baseMapper.update(null, uw);
        }
        return 0;
    }
}
