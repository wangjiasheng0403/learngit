package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zznode.dto.ListOrganizationParam;
import org.zznode.entity.TbPary;
import org.zznode.entity.TbParyTree;

import java.util.List;
import java.util.Map;

/**
 * @description 针对表【tb_pary(党组织关系表)】的数据库操作Mapper
 * @Entity generator.domain.TbPary
 */
public interface TbParyMapper extends BaseMapper<TbPary> {
    /**
     * 删除
     */
    void deletePary(@Param("id") long id);

    /**
     * 父组织data
     *
     * @param paryId 当前组织id
     * @return entity
     */
    @Select("select tbf.* from tb_pary tbf,tb_pary tb where tb.partID = #{paryId} and tbf.partID = tb.fatherID")
    TbPary getFatherParyByParyId(@Param(value = "paryId") Long paryId);

    /**
     * 父组织data
     *
     * @param paryId 当前组织id
     * @return entity
     */
    @Select("select * from tb_pary where partID = #{paryId}")
    TbPary getParyById(@Param(value = "paryId") Long paryId);

    /**
     * 获取用户的角色
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> getUserRoles(@Param(value = "userId") Long userId);

    /**
     * 获取党组织机构
     *
     * @param page
     * @param param
     * @return
     */
    IPage<Map<String, Object>> getTbPary(IPage<TbPary> page, @Param("condition") ListOrganizationParam param);


    IPage<Map<String, Object>> getTbParyMaps(IPage<TbPary> page, @Param("condition") ListOrganizationParam param);

    @Select("select * from tb_pary where fatherID = #{partID}")
    List<Map<String, Object>> getParyFatherId(@Param(value = "partID") String partID);

    /**
     * 党组织树形结构
     */
    List<TbParyTree> paryTree(@Param(value = "tlv") Integer tlv);
}




