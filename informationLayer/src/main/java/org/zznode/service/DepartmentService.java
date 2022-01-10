package org.zznode.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zznode.dto.ExportDepartmentParam;
import org.zznode.dto.InsertDepartmentParam;
import org.zznode.dto.UpdateDepartmentParam;
import org.zznode.entity.TbDepartment;

import java.util.List;

public interface DepartmentService extends IService<TbDepartment> {
    public JSONArray serchDepartmentByNameAndTime(ExportDepartmentParam param);
    public JSONObject insertDepartment(InsertDepartmentParam json);
    public JSONObject updateDepartment(UpdateDepartmentParam json);
    public JSONObject deleteDepartment(Long departid);
}
