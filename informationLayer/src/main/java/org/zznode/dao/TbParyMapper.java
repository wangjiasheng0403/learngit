package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;
import org.zznode.entity.TbPary;

@Service
public interface TbParyMapper extends BaseMapper<TbPary> {
    int deleteByPrimaryKey(Long partid);

    int insertOne(TbPary record);

    int insertSelective(TbPary record);

    TbPary selectByPrimaryKey(Long partid);

    int updateByPrimaryKeySelective(TbPary record);

    int updateByPrimaryKey(TbPary record);
}
