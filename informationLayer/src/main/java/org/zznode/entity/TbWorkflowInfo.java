package org.zznode.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作流信息主表
 *
 * @TableName tb_workflow_info
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_workflow_info")
public class TbWorkflowInfo extends Model<TbWorkflowInfo> {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 流程工单号
     */
    private String orderno;

    /**
     * 流程名称
     */
    private String workflowname;

    /**
     * 发起者
     */
    private Long originator;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 工作流场景编号
     */
    private Integer workflowno;

    /**
     * 当前流程场景序号
     */
    private Integer currentworkfloworder;

    /**
     * 状态
     * 0保存
     * 1进行中
     * 2结束
     */
    private Integer status;

    /**
     * 流程类型：
     * 1模型修改 (model_change)
     * 2运营方账号修改 (operation_account_change)
     * 3组织修改 (organization_change)
     */
    private Integer workflowtype;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}