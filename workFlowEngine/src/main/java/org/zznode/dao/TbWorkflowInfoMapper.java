package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zznode.entity.TbWorkflowInfo;

import java.util.Map;

/**
 * @description 针对表【tb_workflow_info(工作流信息主表)】的数据库操作Mapper
 * @Entity generator.domain.TbWorkflowInfo
 */
public interface TbWorkflowInfoMapper extends BaseMapper<TbWorkflowInfo> {
    /**
     * 发起列表
     */
    @MapKey("id")
    Page<Map<String, Object>> getPageListForOriginate(Page page, @Param("userId") long userId);

    /**
     * 待办列表
     */
    @MapKey("id")
    Page<Map<String, Object>> getPageListForBacklog(Page page, @Param("userId") long userId);

    /**
     * 已办列表
     */
    @MapKey("id")
    Page<Map<String, Object>> getPageListForDone(Page page, @Param("userId") long userId);

    /**
     * 完成列表
     */
    @MapKey("id")
    Page<Map<String, Object>> getPageListForFinished(Page page, @Param("userId") long userId);


    /**
     * 流程基本信息
     */
    Map<String, Object> getWorkflowInfoById(@Param("id") long id);

    /**
     * 获取保存的场景流程修改信息
     *
     * @param dataId dataId
     * @return entity
     */
    TbWorkflowInfo getSaveScenesWorkFlowInfoByDataId(@Param(value = "dataId") String dataId);

    /**
     * 获取保存的组织机构流程信息
     *
     * @return entity
     */
    @Select("select * from tb_workflow_info where workflowNo = 1003 and status = 0 limit 1")
    TbWorkflowInfo getSaveOrgWorkFlowInfo();
}




