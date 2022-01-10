package org.zznode.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zznode.entity.TbDict;

public interface DictService extends IService<TbDict> {
    public JSONArray serchDictByArea(int area);
}
