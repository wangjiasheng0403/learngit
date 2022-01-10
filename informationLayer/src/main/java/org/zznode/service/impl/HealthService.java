package org.zznode.service.impl;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zznode.dao.TbSensitiveMapper;
import org.zznode.dto.HealthReviewParam;
import org.zznode.util.DFAUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HealthService {

    private final TbSensitiveMapper tbSensitiveMapper;
    private final DFAUtil dfaUtil;

    public Map<String, Object> review(HealthReviewParam param) {
        Map<String, Object> result = Maps.newHashMap();

        List<String> sensitivityList = tbSensitiveMapper.getSensitivity();
        Set<String> set = new HashSet<>(sensitivityList);
        //将集合放到算法里
        dfaUtil.createDFAHashMap(set);

        Set<String> sensitiveWordByDFAMap = dfaUtil.getSensitiveWordByDFAMap(param.getReviewDetails(), 1);
        if (sensitiveWordByDFAMap.size() >= 1) {
            result.put("authType", false);
            result.put("authInfo", StringUtils.join(sensitiveWordByDFAMap, ","));
        } else {
            result.put("authType", true);
        }

        return result;
    }
}
