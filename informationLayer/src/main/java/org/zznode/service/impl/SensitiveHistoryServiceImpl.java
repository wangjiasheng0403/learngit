package org.zznode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.zznode.dao.TbSensitiveHistoryMapper;
import org.zznode.dto.SensitiveHistoryParam;
import org.zznode.entity.TbSensitiveHistory;
import org.zznode.service.SensitiveHistoryService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Chris
 */
@Service
public class SensitiveHistoryServiceImpl extends ServiceImpl<TbSensitiveHistoryMapper, TbSensitiveHistory> implements SensitiveHistoryService {


    @Resource
    private TbSensitiveHistoryMapper tbSensitiveHistoryMapper;

    @Override
    public IPage<Map<String, Object>> list(SensitiveHistoryParam param) {
        QueryWrapper<TbSensitiveHistory> wrapper = new QueryWrapper<>();
        if (0 != param.getChecktype()) {
            wrapper.lambda().eq(TbSensitiveHistory::getChecktype, param.getChecktype());
        }
        if (StringUtils.isNotEmpty(param.getSensitivecontent())) {
            wrapper.lambda().like(TbSensitiveHistory::getSensitivecontent, param.getSensitivecontent());
        }
        if (StringUtils.isNotEmpty(param.getStartDate()) && StringUtils.isNotEmpty(param.getEndDate())) {
            wrapper.lambda().between(TbSensitiveHistory::getChecktime, param.getStartDate() + "00:00:00", param.getEndDate() + "23:59:59");
        }
        IPage<TbSensitiveHistory> page = new Page<>(param.getPageNo(), param.getPageSize());
        return tbSensitiveHistoryMapper.selectMapsPage(page, wrapper);
    }
}

