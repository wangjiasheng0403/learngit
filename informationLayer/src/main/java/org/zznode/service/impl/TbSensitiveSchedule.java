package org.zznode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.zznode.dao.*;
import org.zznode.entity.*;
import org.zznode.util.SensitiveWordUtil;
import org.zznode.util.SnowflakeIdGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Chris
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbSensitiveSchedule {


    private final TbSensitiveMapper tbSensitiveMapper;

    private final TbMakesMapper tbMakesMapper;

    private final TbChangeModelMapper tbChangeModelMapper;

    private final TbRoleLogsMapper tbRoleLogsMapper;

    private final TbSensitiveHistoryMapper tbSensitiveHistoryMapper;

    private final TbWorkflowModelChangeMapper tbWorkflowModelChangeMapper;

    private final TbWorkflowInfoMapper tbWorkflowInfoMapper;

    private final SnowflakeIdGenerator snowflakeIdGenerator;


    @Scheduled(cron = "${cron.sensitiveCron}")
    private void checkTbSensitive() {
        log.info("定时任务启动");
        Date currentDate = new Date();
        List<TbSensitive> checkList = tbSensitiveMapper.selectList(new QueryWrapper<>());
        Set<String> sensitiveWordSet = new HashSet<>();
        checkList.forEach((item) -> sensitiveWordSet.add(item.getSensitivity()));
        SensitiveWordUtil.init(sensitiveWordSet);
        checkMakes(currentDate);
        checkChangeModel(currentDate);
    }

    private void checkMakes(Date currentDate) {
        TbSensitiveHistory tbSensitiveHistory;
        Set<String> sensitiveSet;
        List<TbMakes> makesList = tbMakesMapper.selectList(new QueryWrapper<>());
        for (TbMakes tbMakes : makesList) {
            if (!StringUtils.isEmpty(tbMakes.getMakeInfo())) {
                sensitiveSet = SensitiveWordUtil.getSensitiveWord(tbMakes.getMakeInfo(), SensitiveWordUtil.MINMATCHTYPE);
                if (!CollectionUtils.isEmpty(sensitiveSet)) {
                    TbRoleLogs tbRoleLogs = tbRoleLogsMapper.selectOne(new QueryWrapper<TbRoleLogs>().lambda().eq(TbRoleLogs::getId, tbMakes.getLogsid()));
                    List<TbSensitiveHistory> histories = tbSensitiveHistoryMapper.selectList(new QueryWrapper<TbSensitiveHistory>().lambda().eq(TbSensitiveHistory::getScenesource, tbRoleLogs.getDataname())
                            .eq(TbSensitiveHistory::getInformation, tbMakes.getMakeInfo()).eq(TbSensitiveHistory::getUserid, tbRoleLogs.getUserid()));
                    if (CollectionUtils.isEmpty(histories)) {
                        tbSensitiveHistory = new TbSensitiveHistory();
                        tbSensitiveHistory.setId(snowflakeIdGenerator.nextId());
                        tbSensitiveHistory.setChecktime(currentDate);
                        tbSensitiveHistory.setChecktype(Short.valueOf("2"));
                        tbSensitiveHistory.setInformation(tbMakes.getMakeInfo());
                        tbSensitiveHistory.setUserid(tbRoleLogs.getUserid());
                        tbSensitiveHistory.setScenesource(tbRoleLogs.getDataname());
                        tbSensitiveHistory.setSensitivecontent(String.join(",", sensitiveSet));
                        tbSensitiveHistoryMapper.insert(tbSensitiveHistory);
                    }
                }
            }
        }
    }

    private void checkChangeModel(Date currentDate) {
        TbSensitiveHistory tbSensitiveHistory;
        Set<String> sensitiveSet;
        List<TbChangeModel> tbChangeModelList = tbChangeModelMapper.selectList(new QueryWrapper<TbChangeModel>().lambda().eq(TbChangeModel::getMatetype, 1));
        for (TbChangeModel tbChangeModel : tbChangeModelList) {
            if (!StringUtils.isEmpty(tbChangeModel.getMatepath())) {
                sensitiveSet = SensitiveWordUtil.getSensitiveWord(tbChangeModel.getMatepath(), SensitiveWordUtil.MINMATCHTYPE);
                if (!CollectionUtils.isEmpty(sensitiveSet)) {
                    TbWorkflowModelChange tbWorkflowModelChange = tbWorkflowModelChangeMapper.selectOne(new QueryWrapper<TbWorkflowModelChange>()
                            .lambda().eq(TbWorkflowModelChange::getWorkflowinfoid, tbChangeModel.getWorkflowmodelchangeid()));
                    if (ObjectUtils.isEmpty(tbWorkflowModelChange)) {
                        log.error("发生错误，未找到对应的模型修改工作流明细表信息:{}", tbChangeModel);
                        return;
                    }
                    TbWorkflowInfo tbWorkflowInfo = tbWorkflowInfoMapper.selectOne(new QueryWrapper<TbWorkflowInfo>().lambda().eq(TbWorkflowInfo::getId, tbChangeModel.getWorkflowmodelchangeid()));
                    List<TbSensitiveHistory> histories = tbSensitiveHistoryMapper.selectList(new QueryWrapper<TbSensitiveHistory>().lambda().eq(TbSensitiveHistory::getScenesource, tbWorkflowModelChange.getDataname())
                            .eq(TbSensitiveHistory::getInformation, tbChangeModel.getMatepath()).eq(TbSensitiveHistory::getUserid, tbWorkflowInfo.getOriginator()));
                    if (CollectionUtils.isEmpty(histories)) {
                        tbSensitiveHistory = new TbSensitiveHistory();
                        tbSensitiveHistory.setId(snowflakeIdGenerator.nextId());
                        tbSensitiveHistory.setChecktime(currentDate);
                        tbSensitiveHistory.setChecktype(Short.valueOf("1"));
                        tbSensitiveHistory.setInformation(tbChangeModel.getMatepath());
                        tbSensitiveHistory.setScenesource(tbWorkflowModelChange.getDataname());
                        tbSensitiveHistory.setUserid(tbWorkflowInfo.getOriginator());
                        tbSensitiveHistory.setSensitivecontent(String.join(",", sensitiveSet));
                        tbSensitiveHistoryMapper.insert(tbSensitiveHistory);
                    }
                }
            }
        }
    }
}
