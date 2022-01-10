package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zznode.entity.TbDataResource;

import java.util.List;
import java.util.Map;

/**
 * @description 针对表【tb_data_resource(数据域)】的数据库操作Mapper
 * @Entity generator.domain.TbDataResource
 */
public interface TbDataResourceMapper extends BaseMapper<TbDataResource> {

    List<Map<String, Object>> getScenes(@Param(value = "userId") Long userId);

    @Select("select * from tb_data_resource where dataID = #{id}")
    TbDataResource getDataResourceById(@Param(value = "id") String id);
}




