package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;
import org.zznode.entity.TbDict;

import java.util.List;

@Service
public interface TbDictMapper extends BaseMapper<TbDict> {
    int deleteByPrimaryKey(Long id);

    int insertOne(TbDict record);

    int insertSelective(TbDict record);

    TbDict selectByPrimaryKey(Long id);

    List<TbDict> selectByArea(int id);

    int updateByPrimaryKeySelective(TbDict record);

    int updateByPrimaryKey(TbDict record);
}
