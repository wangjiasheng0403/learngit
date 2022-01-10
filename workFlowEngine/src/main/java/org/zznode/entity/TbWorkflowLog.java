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
 * 流程记录表
 *
 * @TableName tb_workflow_log
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_workflow_log")
public class TbWorkflowLog extends Model<TbWorkflowLog> {
    /**
     * 缺省主键
     */
    @TableId
    private Long id;

    /**
     * 角色
     */
    private Long roleid;

    /**
     * 对应操作人
     */
    private Long userid;

    /**
     * 操作人所在部门
     */
    private Long departid;

    /**
     * 处理结果
     * 0:未处理
     * 1：通过
     * 2：驳回
     */
    private Integer status;

    /**
     * 处理评语
     */
    private String make;

    /**
     * 操作时间
     */
    private Date optime;

    /**
     * 工作流场景编号
     */
    private Integer workflowno;

    /**
     * 记录序号
     */
    private Integer workfloworder;

    /**
     * 工作流信息id
     */
    private Long workflowinfoid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}