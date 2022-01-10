package org.zznode.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程配置表
 *
 * @TableName tb_workflow_config
 */
@Data
@TableName(value = "tb_workflow_config")
public class TbWorkflowConfig extends Model<TbWorkflowConfig> {
    /**
     * 缺省主键
     */
    @TableId
    private Long id;

    /**
     * 工作流场景编号
     */
    private Integer workflowno;

    /**
     * 场景序号;最终一个节点序号，默认99，表示最终。
     */
    private Integer workfloworder;

    /**
     * 场景角色
     */
    private Long rolesid;

    /**
     * 回退到的场景序号
     */
    private Integer backworkfloworder;

    /**
     * 1 行政机构
     * 2 党组织
     */
    private Integer orgtype;

    /**
     * 是否是上一级
     */
    private Integer upperlevel;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}