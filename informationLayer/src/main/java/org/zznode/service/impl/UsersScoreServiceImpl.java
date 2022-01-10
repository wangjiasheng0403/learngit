package org.zznode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zznode.dao.TbUsersScoreMapper;
import org.zznode.dto.UsersScoreDetailsParam;
import org.zznode.dto.UsersScoreParam;
import org.zznode.entity.TbUsersScore;
import org.zznode.service.UsersScoreService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Chris
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersScoreServiceImpl extends ServiceImpl<TbUsersScoreMapper, TbUsersScore> implements UsersScoreService {

    private final TbUsersScoreMapper tbUsersScoreMapper;

    /**
     * 用户积分学习
     *
     * @param usersScoreParam parameter
     * @return users score list
     */
    @Override
    public IPage<Map<String, Object>> list(UsersScoreParam usersScoreParam) {
        // title 统计时间，党组织机构（名称），组织人数，组织总积分，平均积分，排名
        IPage<TbUsersScore> page = new Page<>(usersScoreParam.getPageNo(), usersScoreParam.getPageSize());
        IPage<Map<String, Object>> pageList = tbUsersScoreMapper.getTbUsersScoreMaps(page, usersScoreParam);
        List<Map.Entry<Double, List<Map<String, Object>>>> list = pageList.getRecords().stream().collect(Collectors.groupingBy(e -> Double.parseDouble(e.get("ageScore").toString()))).entrySet()
                .stream()
                .sorted((s1, s2) -> -Double.compare(Double.parseDouble(s1.getKey().toString()), Double.parseDouble(s2.getKey().toString()))).collect(Collectors.toList());
        int count = 1;
        double lastScore = -1;
        for (Map.Entry<Double, List<Map<String, Object>>> doubleListEntry : list) {
            for (int j = 0; j < doubleListEntry.getValue().size(); j++) {
                Map<String, Object> eleMap = doubleListEntry.getValue().get(j);
                double ageScore = Double.parseDouble(eleMap.get("ageScore").toString());
                if (Double.compare(lastScore, ageScore) != 0) {
                    lastScore = ageScore;
                    eleMap.put("rank", count++);
                } else {
                    eleMap.put("rank", count - 1);
                }
            }
        }
        return pageList;
    }

    /**
     * 组织积分详细
     *
     * @param detailsParam 组织积分详细
     * @return detail list
     */
    @Override
    public IPage<Map<String, Object>> details(UsersScoreDetailsParam detailsParam) {
        IPage<TbUsersScore> page = new Page<>(detailsParam.getPageNo(), detailsParam.getPageSize());
        IPage<Map<String, Object>> detailsList = tbUsersScoreMapper.selectMapsPage(page, new QueryWrapper<TbUsersScore>().lambda()
                .eq(TbUsersScore::getParyid, detailsParam.getParyid()).eq(TbUsersScore::getTimequantum, detailsParam.getTimeQuantum()));
        List<Map.Entry<Double, List<Map<String, Object>>>> list = detailsList.getRecords().stream().collect(Collectors.groupingBy(e -> Double.parseDouble(e.get("score").toString()))).entrySet()
                .stream()
                .sorted((s1, s2) -> -Double.compare(Double.parseDouble(s1.getKey().toString()), Double.parseDouble(s2.getKey().toString()))).collect(Collectors.toList());
        int count = 1;
        double lastScore = -1;
        for (Map.Entry<Double, List<Map<String, Object>>> doubleListEntry : list) {
            for (int j = 0; j < doubleListEntry.getValue().size(); j++) {
                Map<String, Object> eleMap = doubleListEntry.getValue().get(j);
                double ageScore = Double.parseDouble(eleMap.get("score").toString());
                if (Double.compare(lastScore, ageScore) != 0) {
                    lastScore = ageScore;
                    eleMap.put("rank", count++);
                } else {
                    eleMap.put("rank", count - 1);
                }
            }
        }
        return detailsList;
    }


}
