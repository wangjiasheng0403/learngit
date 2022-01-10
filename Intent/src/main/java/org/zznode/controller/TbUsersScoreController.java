package org.zznode.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.zznode.dto.UsersScoreDetailsParam;
import org.zznode.dto.UsersScoreParam;

/**
 * @author Chris
 */
@Slf4j
@RestController
@RequestMapping("userScore")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbUsersScoreController {

    private final RestTemplate restTemplate;

    @PostMapping("/list")
    public JSONObject list(@RequestBody UsersScoreParam usersScoreParam) {
        return restTemplate.postForEntity("http://informationLayer/userScore/list", usersScoreParam, JSONObject.class).getBody();
    }

    @PostMapping("/details")
    public JSONObject details(@RequestBody UsersScoreDetailsParam detailsParam) {
        return restTemplate.postForEntity("http://informationLayer/userScore/details", detailsParam, JSONObject.class).getBody();
    }
}
