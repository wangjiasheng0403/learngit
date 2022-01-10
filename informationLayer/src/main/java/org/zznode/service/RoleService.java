package org.zznode.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zznode.dto.InsertRoleInfoparam;
import org.zznode.dto.SerchRoleInfoByRoleNameOrDescriptorOrCreateTimeParam;
import org.zznode.dto.UpdateRoleInfoParam;
import org.zznode.entity.TbRoles;

import java.util.List;

public interface RoleService extends IService<TbRoles> {

    public List<TbRoles> serchRoleInfoAll();
    public JSONObject insertRolest(InsertRoleInfoparam param);
    public JSONObject deleteRolestOne(JSONObject json);
    public JSONObject updateRoleInfo(UpdateRoleInfoParam json);
    public JSONObject serchRoleInfoByRoleNameOrDescriptorOrCreateTime(SerchRoleInfoByRoleNameOrDescriptorOrCreateTimeParam param);
    public JSONArray serchRuleInfo();
}
