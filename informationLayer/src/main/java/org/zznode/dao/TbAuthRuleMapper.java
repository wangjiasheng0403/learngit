package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.zznode.entity.TbAuthRule;

import java.util.List;

@Service
public interface TbAuthRuleMapper extends BaseMapper<TbAuthRule> {
    int deleteByPrimaryKey(Long id);

    int insertone(TbAuthRule record);

    int insertSelective(TbAuthRule record);

    TbAuthRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbAuthRule record);

    int updateByPrimaryKey(TbAuthRule record);

    @Select("select * from tb_auth_rule where intfName = #{intfname}")
    List<TbAuthRule> serchAuth(String intfname);
}
