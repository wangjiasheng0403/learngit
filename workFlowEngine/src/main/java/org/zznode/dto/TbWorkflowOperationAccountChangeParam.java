package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TbWorkflowOperationAccountChangeParam")
public class TbWorkflowOperationAccountChangeParam {
    @ApiModelProperty(value = "流程单号")
    private String orderNo;//流程单号
    @ApiModelProperty(value = "流程名称")
    private String workflowName;//流程名称
    @ApiModelProperty(value = "申请人ID")
    private String token;//申请人ID
    @ApiModelProperty(value = "申请人角色ID")
    private Long roleId;//申请人角色ID
    @ApiModelProperty(value = "申请人所属机构")
    private Long paryId;//申请人所属机构
    @ApiModelProperty(value = "删除账号ID")
    private Long deleteUserId;//删除账号ID
    @ApiModelProperty(value = "删除理由")
    private String deleteContent;//删除理由
    @ApiModelProperty(value = "0保存 1进行中 2结束")
    private Integer status;//0保存 1进行中 2结束
    @ApiModelProperty(value = "文件地址")
    private String uploadFilePath;//文件地址
    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    private Long approverUserID;
    /**
     * 审核人组织ID
     */
    @ApiModelProperty(value = "审核人组织ID")
    private Long approverParyId;
}