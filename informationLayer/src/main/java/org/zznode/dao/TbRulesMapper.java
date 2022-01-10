package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;
import org.zznode.entity.TbRules;

@Service
public interface TbRulesMapper extends BaseMapper<TbRules> {
    int deleteByPrimaryKey(Long rulesid);

    int insertOne(TbRules record);

    int insertSelective(TbRules record);

    TbRules selectByPrimaryKey(Long rulesid);

    int updateByPrimaryKeySelective(TbRules record);

    int updateByPrimaryKey(TbRules record);
}
