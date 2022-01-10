package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;
import org.zznode.entity.TbUserCollect;

import java.util.List;

@Service
public interface TbUserCollectMapper extends BaseMapper<TbUserCollect> {
    int deleteByPrimaryKey(Long id);

    int insertOne(TbUserCollect record);

    int insertSelective(TbUserCollect record);

    TbUserCollect selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbUserCollect record);

    int updateByPrimaryKey(TbUserCollect record);

    @Select("select * from tb_user_collect where userID = #{userid}")
    List<TbUserCollect> selectByuserID(Long userID);

    @Delete("delete from  tb_user_collect where userID = #{userid} and dataPath = #{datapath} ")
    int deleteByuseridAnddatapath(@Param("userid")Long userid, @Param("datapath")String datapath);

}
