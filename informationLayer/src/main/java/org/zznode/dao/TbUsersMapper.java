package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;
import org.zznode.entity.TbUsers;

import java.util.Map;

@Service
public interface TbUsersMapper extends BaseMapper<TbUsers> {
    int deleteByPrimaryKey(Long userid);

    int insertOne(TbUsers record);

//    int insertSelective(TbUsers record);

    TbUsers selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(TbUsers record);

    int updateByPrimaryKey(TbUsers record);

    @Update("update tb_users set nickname = #{nickname}  where userID = #{userid}" )
    int updataNickname(@Param(value = "nickname")String nickname, @Param(value = "userid")Long userid);


    /**
     * 所属二级部门ID, 党组织id
     *
     * @param userId userId
     * @return map
     */
    Map<String, Long> getUserPartIdAndSecondDepartId(@Param(value = "userId") Long userId);
}
