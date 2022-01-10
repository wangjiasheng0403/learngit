package org.zznode.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zznode.dao.TbDictMapper;
import org.zznode.entity.TbDict;
import org.zznode.service.DictService;

import java.util.List;


@Service
public class DictServiceImpl extends ServiceImpl<TbDictMapper, TbDict> implements DictService{
    @Autowired
    private TbDictMapper tbDictMapper;

    @Override
    public JSONArray serchDictByArea(int area){
       List<TbDict> tbDictList =  tbDictMapper.selectByArea(area);

       JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(tbDictList));
       return jsonArray;
    }
}
