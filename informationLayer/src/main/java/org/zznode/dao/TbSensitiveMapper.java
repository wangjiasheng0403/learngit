package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zznode.entity.TbSensitive;

import java.util.List;

/**
 * @description 针对表【tb_sensitive(敏感字段表)】的数据库操作Mapper
 * @Entity generator.domain.TbSensitive
 */
public interface TbSensitiveMapper extends BaseMapper<TbSensitive> {
    List<String> getSensitivity();
}




