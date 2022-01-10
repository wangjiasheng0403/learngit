package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.zznode.dto.UsersOptionSearch;
import org.zznode.entity.TbUsersOptions;

import java.util.List;
import java.util.Map;

/**
 * @description 针对表【tb_users_options(用户操作日志表)】的数据库操作Mapper
 * @Entity generator.domain.TbUsersOptions
 */
@Service
public interface TbUsersOptionsMapper extends BaseMapper<TbUsersOptions> {
    Page<Map<String, Object>> getPageList(Page page, @Param(value = "param") UsersOptionSearch param);

    @Select("select userID from tb_users_options where DATE_FORMAT(SYSDATE()- INTERVAL 1 DAY,'%Y-%m-%d') = DATE_FORMAT(createtime,'%Y-%m-%d')  GROUP BY userID")
    List<Long> serchUseridList();
}
