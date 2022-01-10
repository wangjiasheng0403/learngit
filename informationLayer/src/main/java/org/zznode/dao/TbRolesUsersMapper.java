package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;
import org.zznode.entity.TbRolesUsers;

@Service
public interface TbRolesUsersMapper extends BaseMapper<TbRolesUsers> {
    int deleteByPrimaryKey(Long id);

    int insertOne(TbRolesUsers record);

    int insertSelective(TbRolesUsers record);

    TbRolesUsers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbRolesUsers record);

    int updateByPrimaryKey(TbRolesUsers record);

    int deleteByuserID(Long userid);

    int deleteByrolesID(Long rolesid);
}
