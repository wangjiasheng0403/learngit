package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;
import org.zznode.entity.TbRoles;

@Service
public interface TbRolesMapper extends BaseMapper<TbRoles> {
    int deleteByPrimaryKey(Long rolesid);

    int insertOne(TbRoles record);

    int insertSelective(TbRoles record);

    TbRoles selectByPrimaryKey(Long rolesid);

    int updateByPrimaryKeySelective(TbRoles record);

    int updateByPrimaryKey(TbRoles record);
}
