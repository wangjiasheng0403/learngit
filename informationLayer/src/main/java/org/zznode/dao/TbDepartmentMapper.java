package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.zznode.entity.TbDepartment;

import java.util.List;
import java.util.Map;

@Service
public interface TbDepartmentMapper extends BaseMapper<TbDepartment> {
    int deleteByPrimaryKey(Long departid);

    int insertOne(TbDepartment record);

    int insertSelective(TbDepartment record);

    TbDepartment selectByPrimaryKey(Long departid);

    int updateByPrimaryKeySelective(TbDepartment record);

    int updateByPrimaryKey(TbDepartment record);

    /**
     * 同二级机构
     *
     * @param secondDepartId secondDepartId
     * @param partyId        partyId
     * @param type           type 0 同属二级部门，
     * @return list
     */
    List<Map<String, Object>> getDepList(@Param(value = "secondDepartId") Long secondDepartId,
                                         @Param(value = "partyId") Long partyId,
                                         @Param(value = "type") Integer type);

    /**
     * 全部部门
     *
     * @param partyId partyId
     * @return list
     */
    List<Map<String, Object>> getAllDepList(@Param(value = "partyId") Long partyId);
}
