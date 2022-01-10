package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.zznode.dto.SensitiveHistoryParam;
import org.zznode.entity.TbSensitiveHistory;

import java.util.Map;

/**
 * @author Chris
 */
public interface TbSensitiveHistoryMapper extends BaseMapper<TbSensitiveHistory> {


    IPage<Map<String, Object>> getTbSensitiveHistory(IPage<TbSensitiveHistory> page, @Param("condition") SensitiveHistoryParam param);

}
