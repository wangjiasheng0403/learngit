package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;
import org.zznode.entity.TbRolesRules;

import java.util.List;

@Service
public interface TbRolesRulesMapper extends BaseMapper<TbRolesRules> {
    int deleteByPrimaryKey(Long id);

    int deleteByRolesID(Long id);

    int insertOne(TbRolesRules record);

    int insertSelective(TbRolesRules record);

    TbRolesRules selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbRolesRules record);

    int updateByPrimaryKey(TbRolesRules record);

    List<TbRolesRules> selectByRolesAndRules (TbRolesRules record);

    List<TbRolesRules> selectByRolesID (Long rolesid);
}
