package org.zznode.service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zznode.dto.InsertDataResourceInfoParam;
import org.zznode.dto.SerchDataResourceByNameParam;
import org.zznode.entity.TbDataResource;

public interface TbDataResourceService extends IService<TbDataResource> {

    public JSONArray serchdataresource(SerchDataResourceByNameParam param);
    public JSONObject insertTbDataResource(InsertDataResourceInfoParam param);
    public JSONObject updateDataResourceInfo(InsertDataResourceInfoParam json);
    public JSONObject deleteDataResource(String dataid);

}
