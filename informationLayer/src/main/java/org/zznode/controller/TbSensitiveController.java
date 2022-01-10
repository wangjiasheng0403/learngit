package org.zznode.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zznode.common.CommonResult;
import org.zznode.dto.TbSensitiveContentParam;
import org.zznode.dto.TbSensitiveParam;
import org.zznode.service.impl.TbSensitiveService;

import javax.validation.Valid;

/**
 * @author bh
 */
@Slf4j
@RestController
@RequestMapping("sensitive")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbSensitiveController {

    private final TbSensitiveService tbSensitiveService;

    /**
     * @return
     */
    @PostMapping("/save")
    public CommonResult<?> save(@RequestBody TbSensitiveParam tbSensitiveParam) {
        return tbSensitiveService.save(tbSensitiveParam);
    }

    @PostMapping("/check")
    public CommonResult<?> check(@Valid @RequestBody TbSensitiveContentParam tbSensitiveContent) {
        return tbSensitiveService.check(tbSensitiveContent);
    }
}
