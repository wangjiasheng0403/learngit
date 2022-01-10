package org.zznode.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zznode.dto.SensitiveHistoryParam;
import org.zznode.entity.TbSensitiveHistory;

import java.util.Map;

/**
 * @author Chris
 */
public interface SensitiveHistoryService extends IService<TbSensitiveHistory> {
    IPage<Map<String, Object>> list(SensitiveHistoryParam param);
}
