package org.zznode.dto;

import lombok.Data;

@Data
public class TbWorkflowLogParam {
    private Long roleId;
    private Long userId;
    private Long departId;
    private int workflowNo;
    private String workfolwOrder;
}