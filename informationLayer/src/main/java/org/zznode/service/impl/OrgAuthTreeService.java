package org.zznode.service.impl;

import com.google.common.base.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zznode.dao.TbDepartRelationMapper;
import org.zznode.dao.TbDepartmentMapper;
import org.zznode.dao.TbUsersMapper;
import org.zznode.dto.GetOrgAuthTreeParam;
import org.zznode.dto.ModifyAuthParam;
import org.zznode.entity.TbDepartRelation;
import org.zznode.util.SnowflakeIdGenerator;
import org.zznode.util.StaticUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrgAuthTreeService {

    private final TbDepartmentMapper departmentMapper;
    private final TbUsersMapper usersMapper;
    private final TbDepartRelationMapper departRelationMapper;
    private final TokenService tokenService;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    /**
     * 获取授权树
     *
     * @param param param
     * @return map
     */
    public Map<String, Object> getOrgAuthTree(GetOrgAuthTreeParam param) {

        Long userId = tokenService.getUserId(param.getToken());
        Map<String, Long> userInfo = usersMapper.getUserPartIdAndSecondDepartId(userId);

        Map<String, Object> result = new HashMap<>(2);
        result.put("currentPartyId", userInfo.get("partyId"));

        List<Map<String, Object>> orgAuthMap;
        if (userInfo.get("secondDepartId") == null || userInfo.get("secondDepartId") == 0) {
            orgAuthMap = departmentMapper.getAllDepList(userInfo.get("partyId"));
        } else {
            orgAuthMap = departmentMapper.getDepList(userInfo.get("secondDepartId"), userInfo.get("partyId"), param.getRelationType());
        }

        List<Map<String, Object>> newOrgAuthMap = new ArrayList<>();

        for (Map<String, Object> map : orgAuthMap) {
            Map<String, Object> mapCopy = new HashMap<>(map);
            mapCopy.put("fatherId", map.get("departId"));
            mapCopy.put("departId", "0");
            mapCopy.put("lv", "99");
            mapCopy.put("type", 1);
            mapCopy.remove("departName");
            newOrgAuthMap.add(mapCopy);

            map.remove("isAuth");
            map.remove("partId");
            map.remove("partName");
            map.put("type", 0);
        }
        newOrgAuthMap.addAll(orgAuthMap);

        result.put("orgTree", newOrgAuthMap.stream()
                .filter(i -> "1".equals(i.get("lv").toString()))
                .peek(i -> i.put("child", setChild(i, newOrgAuthMap)))
                .collect(Collectors.toList()));

        return result;
    }

    private List<Map<String, Object>> setChild(Map<String, Object> data, List<Map<String, Object>> result) {

        return result.stream()
                .filter(i -> Objects.equal(i.get("fatherId"), data.get("departId")))
                .peek(i -> i.put("child", setChild(i, result)))
                .collect(Collectors.toList());
    }


    /**
     * 修改授权组织
     *
     * @param param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyAuth(ModifyAuthParam param) {

        //删除授权关系
        departRelationMapper.deleteDepartRelationByParyIdAndRelationType(Long.parseLong(param.getParyId()), param.getRelationType());

        TbDepartRelation tbDepartRelation;

        List<Long> longs = StaticUtils.ConvetDataType(param.getVisitParyIds());
        for (Long orgIdFrom : longs) {
            //被授权组织和授权组织不想同时 保存数据
            if (!orgIdFrom.equals(param.getParyId())) {
                tbDepartRelation = new TbDepartRelation();
                tbDepartRelation.setId(snowflakeIdGenerator.nextId());
                tbDepartRelation.setParyid(Long.parseLong(param.getParyId()) );
                tbDepartRelation.setVisitparyid(orgIdFrom);
                tbDepartRelation.setRelationtype(param.getRelationType());
                departRelationMapper.insert(tbDepartRelation);
            }
        }
    }
}
