package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zznode.entity.TbWorkflowSerial;

/**
 * @description 针对表【tb_workflow_serial】的数据库操作Mapper
 * @Entity generator.domain.TbWorkflowSerial
 */
public interface TbWorkflowSerialMapper extends BaseMapper<TbWorkflowSerial> {
    Integer getMaxSerialNo(String createDate);
}




