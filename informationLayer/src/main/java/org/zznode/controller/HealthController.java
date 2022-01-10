package org.zznode.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zznode.common.CommonResult;
import org.zznode.dto.HealthReviewParam;
import org.zznode.service.impl.HealthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/health")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "内容健康检查")
public class HealthController {

    private final HealthService healthService;

    @PostMapping("/review")
    public CommonResult<?> review(@Valid @RequestBody HealthReviewParam param) {
        return CommonResult.success(healthService.review(param));
    }
}
