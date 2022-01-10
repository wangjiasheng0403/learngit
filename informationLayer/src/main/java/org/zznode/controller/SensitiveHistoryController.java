package org.zznode.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zznode.common.CommonResult;
import org.zznode.dto.SensitiveHistoryParam;
import org.zznode.service.SensitiveHistoryService;

import java.util.List;

/**
 * @author Chris
 */
@Slf4j
@RestController
@RequestMapping("sensitiveHistory")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SensitiveHistoryController {

    private final SensitiveHistoryService sensitiveHistoryService;

    @PostMapping("/list")
    public CommonResult<?> list(@RequestBody SensitiveHistoryParam sensitiveHistoryParam) {
        return CommonResult.success(sensitiveHistoryService.list(sensitiveHistoryParam));
    }

    @PostMapping("/excel")
    public List<?> excel(@RequestBody SensitiveHistoryParam sensitiveHistoryParam) {
        return sensitiveHistoryService.list(sensitiveHistoryParam).getRecords();
    }

}
