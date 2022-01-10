package org.zznode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zznode.dto.UsersOptionParam;
import org.zznode.dto.UsersOptionSearch;
import org.zznode.entity.TbUsersOptions;

import java.util.Map;

public interface TbUsersOptionsService extends IService<TbUsersOptions> {
    public void save(UsersOptionParam param);
    public Page<Map<String, Object>> getPageList(UsersOptionSearch param);
}
