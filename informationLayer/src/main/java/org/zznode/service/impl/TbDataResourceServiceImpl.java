package org.zznode.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zznode.dao.TbDataResourceMapper;
import org.zznode.dao.TbDepartmentMapper;
import org.zznode.dao.TbParyMapper;
import org.zznode.dto.InsertDataResourceInfoParam;
import org.zznode.dto.SerchDataResourceByNameParam;
import org.zznode.entity.TbDataResource;
import org.zznode.entity.TbDepartment;
import org.zznode.entity.TbPary;
import org.zznode.service.TbDataResourceService;
import org.zznode.util.SnowflakeIdGenerator;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TbDataResourceServiceImpl extends ServiceImpl<TbDataResourceMapper, TbDataResource> implements TbDataResourceService {


    @Resource
    private TbDataResourceMapper tbDataResourceMapper;

    @Autowired
    private TbParyMapper tbParyMapper;

    @Override
    public JSONArray serchdataresource(SerchDataResourceByNameParam param) {
        String dname = null;
        if(null != param.getDataname()){
            dname = param.getDataname().replace("%","\\%");
        }
        QueryWrapper<TbDataResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dname),"dataName",dname)
                .ge(StringUtils.isNotBlank(param.getStarttime()),"createTime",param.getStarttime())
                .le(StringUtils.isNotBlank(param.getEndtime()),"createTime",param.getEndtime())
                .eq("dataType",param.getDatatype());
        System.out.println("dataType:" + param.getDatatype());
        List<TbDataResource> tbDataResourceList = tbDataResourceMapper.selectList(queryWrapper);

        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(tbDataResourceList));

        JSONArray jsonArraypary = new JSONArray();
        for(int i =0; i< jsonArray.size(); i++){
            QueryWrapper<TbPary> querytbPartWrapper = new QueryWrapper<>();
            JSONObject json = (JSONObject)jsonArray.get(i);

            querytbPartWrapper.eq("partID", json.getLong("databelong"));
            System.out.println("databelong==>" + json.getLong("databelong"));
            List<TbPary> tbParyList = tbParyMapper.selectList(querytbPartWrapper);
            if (tbParyList.size() > 0) {
                json.put("partname", tbParyList.get(0).getPartname());
                System.out.println("partname==>" + tbParyList.get(0).getPartname());
            }

            jsonArraypary.add(json);
        }

        return jsonArraypary;
    }

    @Override
    public JSONObject insertTbDataResource(InsertDataResourceInfoParam param) {

        JSONObject json = new JSONObject();

        QueryWrapper<TbDataResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(param.getDataid()),"dataID",param.getDataid());
        List<TbDataResource> dataResourceList = tbDataResourceMapper.selectList(queryWrapper);
        if(dataResourceList.size() > 0){
            json.put("description","资源编号已占用");
            json.put("code","000002");
            json.put("result","falt");
            return json;
        }

        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(param.getBasemodelid()),"dataID",param.getBasemodelid())
                .eq("dataType",0);
        List<TbDataResource> dataResources = tbDataResourceMapper.selectList(queryWrapper);
        if(dataResources.size() == 0){
            json.put("description","输入的模型ID不存在，请核实");
            json.put("code","000002");
            json.put("result","falt");
            return json;
        }


        TbPary tbPary =  tbParyMapper.selectByPrimaryKey(Long.parseLong(param.getDatabelong()));
        if(null == tbPary){
            json.put("description","党组织不存在");
            json.put("code","000002");
            json.put("result","falt");
            return json;
        }

        TbDataResource tbDataResource =new TbDataResource();
        Date date = new Date();
        tbDataResource.setCreatetime(date);
        tbDataResource.setDataid(param.getDataid());
        tbDataResource.setDatainfo(param.getDatainfo());
        tbDataResource.setDatabelong(Long.parseLong(param.getDatabelong()));
        tbDataResource.setDatatype(param.getDatatype());
        tbDataResource.setDataname(param.getDataname());
        tbDataResource.setShowtype(param.getShowtype());
        tbDataResource.setDatapath(param.getDatapath());
        tbDataResource.setBasemodelid(param.getBasemodelid());
        tbDataResource.setImgpath(param.getImgpath());
		tbDataResource.setEditor(param.getEditor());
        tbDataResourceMapper.insertSelective(tbDataResource);
        json.put("description","成功");
        json.put("code","000000");
        json.put("result","success");
        return json;
    }

    @Override
    public JSONObject updateDataResourceInfo(InsertDataResourceInfoParam param) {
        JSONObject json = new JSONObject();
        QueryWrapper<TbDataResource> queryWrapper;
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(param.getDataid()),"dataID",param.getDataid());
        List<TbDataResource> dataResourceList = tbDataResourceMapper.selectList(queryWrapper);
        if(dataResourceList.size() == 0){
            json.put("description","该资源编号不存在");
            json.put("code","000002");
            json.put("result","falt");
            return json;
        }

        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(param.getBasemodelid()),"dataID",param.getBasemodelid())
                .eq("dataType",0);
        List<TbDataResource> dataResources = tbDataResourceMapper.selectList(queryWrapper);
        if(dataResources.size() == 0){
            json.put("description","输入的模型ID不存在，请核实");
            json.put("code","000002");
            json.put("result","falt");
            return json;
        }


        TbPary tbPary =  tbParyMapper.selectByPrimaryKey( Long.parseLong(param.getDatabelong()) );
        if(null == tbPary){
            json.put("description","党组织不存在");
            json.put("code","000002");
            json.put("result","falt");
            return json;
        }

        TbDataResource tbDataResource =new TbDataResource();
        Date date = new Date();
        tbDataResource.setCreatetime(date);
        tbDataResource.setDataid(param.getDataid());
        tbDataResource.setDatainfo(param.getDatainfo());
        tbDataResource.setDatabelong(Long.parseLong(param.getDatabelong()));
        tbDataResource.setDatatype(param.getDatatype());
        tbDataResource.setDataname(param.getDataname());
        tbDataResource.setShowtype(param.getShowtype());
        tbDataResource.setDatapath(param.getDatapath());
        tbDataResource.setBasemodelid(param.getBasemodelid());
        tbDataResource.setImgpath(param.getImgpath());
		tbDataResource.setEditor(param.getEditor());
        tbDataResourceMapper.updateByPrimaryKey(tbDataResource);
        json.put("description","成功");
        json.put("code","000000");
        json.put("result","success");

        return json;
    }

    @Override
    public JSONObject deleteDataResource(String dataid) {
        JSONObject json = new JSONObject();
        QueryWrapper<TbDataResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dataID",dataid);
        List<TbDataResource> departments = tbDataResourceMapper.selectList(queryWrapper);
        if(departments.size() == 0){
            json.put("description","该资源名称不存在");
            json.put("code","000002");
            json.put("result","falt");
            return json;
        }
        tbDataResourceMapper.deleteByPrimaryKey(dataid);
        json.put("description","成功");
        json.put("code","000000");
        json.put("result","success");
        return json;
    }


}
