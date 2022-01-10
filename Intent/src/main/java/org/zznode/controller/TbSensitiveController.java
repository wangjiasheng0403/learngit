package org.zznode.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.zznode.dto.TbSensitiveContentParam;
import org.zznode.dto.TbSensitiveParam;

import javax.validation.Valid;

/**
 * @author bh
 */
@Slf4j
@RestController
@RequestMapping("sensitive")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbSensitiveController {
    private final RestTemplate restTemplate;

    /**
     * @return
     */
    @PostMapping("/save")
    public JSONObject save(@RequestHeader("Authorization") String token, @RequestBody TbSensitiveParam tbSensitiveParam) {
        tbSensitiveParam.setToken(token);
        return restTemplate.postForEntity("http://informationLayer/sensitive/save", tbSensitiveParam, JSONObject.class).getBody();

    }

    @PostMapping("/check")
    public JSONObject check(@Valid @RequestBody TbSensitiveContentParam tbSensitiveContent) {
        return restTemplate.postForEntity("http://informationLayer/sensitive/check", tbSensitiveContent, JSONObject.class).getBody();

    }
}
