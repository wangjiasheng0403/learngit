package org.zznode.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.zznode.dto.HealthReviewParam;

import javax.validation.Valid;

@RestController
@RequestMapping("/health")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "内容健康检查")
public class HealthController {

    private final RestTemplate restTemplate;

    @PostMapping("/review")
    public JSONObject review(@Valid @RequestBody HealthReviewParam param) {

        return restTemplate.postForEntity("http://informationLayer/health/review", param, JSONObject.class).getBody();
    }
}
