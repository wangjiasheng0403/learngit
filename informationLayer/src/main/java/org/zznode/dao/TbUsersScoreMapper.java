package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.zznode.dto.UsersScoreParam;
import org.zznode.entity.TbUsersScore;

import java.util.List;
import java.util.Map;

@Service
public interface TbUsersScoreMapper extends BaseMapper<TbUsersScore> {
    int deleteByPrimaryKey(Long id);

    int insertOne(TbUsersScore record);

    int insertSelective(TbUsersScore record);

    TbUsersScore selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbUsersScore record);

    int updateByPrimaryKey(TbUsersScore record);

    @Select("select * from tb_users_score where userID =  #{userid} and timeQuantum = #{timequantum}")
    TbUsersScore serchuserscore(@Param("userid") Long userid, @Param("timequantum") String timequantum);

    @Select("select * from (select sum(score) as score,min(userName) as userName from tb_users_score \n" +
            "where paryid = #{paryid} and timeQuantum =  #{timequantum} group by userID) a order by a.score desc;")
    List<Map<String, Object>> serchorder(@Param("paryid") Long paryid, @Param("timequantum") String timequantum);

    IPage<Map<String, Object>> getTbUsersScoreMaps(IPage<TbUsersScore> page, @Param("condition") UsersScoreParam usersScoreParam);
}
