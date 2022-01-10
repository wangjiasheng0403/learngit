package org.zznode.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.zznode.common.CustomException;
import org.zznode.dao.TbUsersMapper;
import org.zznode.entity.TbUsers;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenService {

    private final RestTemplate restTemplate;
    private final TbUsersMapper tbUsersMapper;

    public Long getUserId(String token) {
        return this.getUser(token).getUserid();
    }

    public TbUsers getUser(String token) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/getToken?token={token}", JSONObject.class, params);
        JSONObject body = response.getBody();
        log.info("getToken返回结果:{}", body);
        String userAccount = body.getString("userAccount");
        if (userAccount == null) {
            throw new CustomException("100000", "授权错误", "token错误");
        }
        TbUsers tbUsers = tbUsersMapper.selectOne(new QueryWrapper<TbUsers>().lambda()
                .eq(TbUsers::getAccount, userAccount));
        if (tbUsers == null) {
            throw new CustomException("100000", "授权错误", "token错误");
        }
        return tbUsers;

    }
}
