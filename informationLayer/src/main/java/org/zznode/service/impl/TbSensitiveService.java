package org.zznode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.zznode.common.CommonResult;
import org.zznode.common.CustomException;
import org.zznode.dao.TbSensitiveMapper;
import org.zznode.dao.TbUsersMapper;
import org.zznode.dto.TbSensitiveContentParam;
import org.zznode.dto.TbSensitiveParam;
import org.zznode.entity.TbSensitive;
import org.zznode.entity.TbUsers;
import org.zznode.redis.utils.RedisUtils;
import org.zznode.util.SensitiveWordUtil;
import org.zznode.util.SnowflakeIdGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author bh
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbSensitiveService {

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    private static final String PAUSE;

    static {
        PAUSE = "、";
    }


    private final RedisUtils redisUtils;
    private final TbSensitiveMapper tbSensitiveMapper;
    private final TbUsersMapper tbUsersMapper;

    public CommonResult<?> save(TbSensitiveParam param) {
        JSONObject userJson = redisUtils.getToken(param.getToken());
        TbUsers tbUsers = tbUsersMapper.selectOne(new QueryWrapper<TbUsers>().lambda().eq(TbUsers::getAccount, userJson.getString("userAccount")));
        if (ObjectUtils.isEmpty(param)) {
            throw new CustomException("100000", null, "参数不能为空!");
        }
        String sensitivity = param.getSensitivity();
        if (StringUtils.isEmpty(sensitivity)) {
            throw new CustomException("100001", null, "请输入健康检查关键字!");
        }
        if (sensitivity.contains(PAUSE)) {
            String[] content = sensitivity.split(PAUSE);
            for (String ele : content) {
                try {
                    saveTb(ele, tbUsers.getUserid());
                } catch (Exception e) {
                    log.error("{}", e.getMessage());
                }
            }
        } else {
            try {
                saveTb(sensitivity, tbUsers.getUserid());
            } catch (Exception e) {
                log.error("{}", e.getMessage());
            }
        }
        return CommonResult.success();
    }

    private void saveTb(String ele, Long userId) {
        TbSensitive tbSensitive = new TbSensitive();
        tbSensitive.setId(snowflakeIdGenerator.nextId());
        tbSensitive.setSensitivity(ele);
        tbSensitive.setCreateby(String.valueOf(userId));
        tbSensitive.setCreatetime(new Date());
        tbSensitiveMapper.insert(tbSensitive);
    }

    /**
     * 内容健康度检查
     *
     * @return 检查结果
     */
    public CommonResult<?> check(TbSensitiveContentParam tbSensitiveContent) {
        if(StringUtils.isEmpty(tbSensitiveContent.getContent())){
            throw new CustomException("100000", null, "参数不能为空!");
        }
        List<TbSensitive> checkList = tbSensitiveMapper.selectList(new QueryWrapper<>());
        Set<String> sensitiveWordSet = new HashSet<>();
        checkList.forEach((item) -> sensitiveWordSet.add(item.getSensitivity()));
        SensitiveWordUtil.init(sensitiveWordSet);
        boolean result = SensitiveWordUtil.contains(tbSensitiveContent.getContent(), SensitiveWordUtil.MINMATCHTYPE);
        if (result) {
            Set<String> set = SensitiveWordUtil.getSensitiveWord(tbSensitiveContent.getContent(), SensitiveWordUtil.MINMATCHTYPE);
            return CommonResult.error(new CustomException("100403", StringUtils.join(set.toArray(), ","), "内容包含非法字符！"));
        } else {
            return CommonResult.success();
        }
    }
}
