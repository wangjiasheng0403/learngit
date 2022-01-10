package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.zznode.entity.TbDataResource;
import org.zznode.entity.TbDepartment;
import org.zznode.entity.TbUserCollect;

import java.util.List;
import java.util.Map;

@Service
public interface TbDataResourceMapper extends BaseMapper<TbDataResource> {
    int insertOne(TbDataResource record);

    int insertSelective(TbDataResource record);

    List<TbDataResource> selectByDataBelong(Long databelong);

//    /**
//     * 场景list
//     *
//     * @return list
//     */
//    @Select("select dataId,dataName from tb_data_resource where dataBelong is not null ")
//    Page<Map<String, Object>> getDataResource(Page page);

    @Select("select * from tb_data_resource where apriveOrder is not null order by apriveOrder limit 10")
    List<TbDataResource> getRecommendData();

    @Select("select * from tb_data_resource where dataBelong = #{partid} and dataType =2" )
    List<TbDataResource> getPartydSceneData(long partid);

    @Select("select count(*) from tb_user_collect where userid = #{userid} and datapath = #{datapath}")
    int getUserCollect(@Param(value = "userid")Long userid,@Param(value = "datapath") String datapath);

    @Select("select * from tb_data_resource where showType =1 and dataType =2")
    List<TbDataResource> getOpenData();

    @Select("select * from tb_data_resource where  dataType =1 and showType =1")
    List<TbDataResource> getRedData();

    @Select("select * from tb_data_resource where dataPath = #{datapath}")
    List<TbDataResource> getDateResource(String datapath);

    public List<TbDataResource> selectByDataBelongAndShowType(TbDataResource tdr);
    public void updateApriveOrder();
    public TbDataResource selectByPrimaryKey(String dataid);
    public int insertSelectiveinfo(TbDataResource tbDataResource);
    public int deleteByPrimaryKey(String dataid);
    public int updateByPrimaryKey(TbDataResource record);
}
