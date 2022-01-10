package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zznode.entity.TbDepartRelation;

/**
 * @description 针对表【tb_depart_relation(组织关系表)】的数据库操作Mapper
 * @Entity generator.domain.TbDepartRelation
 */
public interface TbDepartRelationMapper extends BaseMapper<TbDepartRelation> {

    @Select("delete from tb_depart_relation where paryID = #{partyId} and relationType = #{relationType}")
    void deleteDepartRelationByParyIdAndRelationType(@Param(value = "partyId") Long partyId,
                                                     @Param(value = "relationType") Integer relationType);
}
