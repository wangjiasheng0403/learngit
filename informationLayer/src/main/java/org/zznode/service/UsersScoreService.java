package org.zznode.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zznode.dto.UsersScoreDetailsParam;
import org.zznode.dto.UsersScoreParam;
import org.zznode.entity.TbUsersScore;

import java.util.Map;

/**
 * @author Chris
 */
public interface UsersScoreService extends IService<TbUsersScore> {

    /**
     * 用户积分学习
     *
     * @param usersScoreParam parameter
     * @return users score list
     */
    IPage<Map<String, Object>> list(UsersScoreParam usersScoreParam);

    /**
     * 组织积分详细
     *
     * @param detailsParam 组织积分详细
     * @return detail list
     */
    IPage<Map<String, Object>> details(UsersScoreDetailsParam detailsParam);
}
