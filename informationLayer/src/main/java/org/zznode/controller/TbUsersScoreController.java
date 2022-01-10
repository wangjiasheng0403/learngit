package org.zznode.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zznode.common.CommonResult;
import org.zznode.dto.UsersScoreDetailsParam;
import org.zznode.dto.UsersScoreParam;
import org.zznode.service.UsersScoreService;

/**
 * @author Chris
 */
@Slf4j
@RestController
@RequestMapping("userScore")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbUsersScoreController {

    private final UsersScoreService usersScoreService;

    @PostMapping("/list")
    public CommonResult<?> list(@RequestBody UsersScoreParam usersScoreParam) {
        return CommonResult.success(usersScoreService.list(usersScoreParam));
    }

    @PostMapping("/details")
    public CommonResult<?> details(@RequestBody UsersScoreDetailsParam detailsParam) {
        return CommonResult.success(usersScoreService.details(detailsParam));
    }
}
