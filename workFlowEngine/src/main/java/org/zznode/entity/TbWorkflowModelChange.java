package org.zznode.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 模型修改工作流明细表
 *
 * @TableName tb_workflow_model_change
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_workflow_model_change")
public class TbWorkflowModelChange extends Model<TbWorkflowModelChange> {
    /**
     * 缺省主键
     */
    @TableId
    private Long id;

    /**
     *
     */
    private Long workflowinfoid;

    /**
     * 场景 url
     */
    private String datapath;

    /**
     * 1: 新建；
     * 2：修改
     */
    private Integer type;

    /**
     * 资源名称
     */
    private String dataname;

    /**
     * 1：公开；
     * 2：私有；
     */
    private Integer datatype;

    /**
     * 附件存放路径
     */
    private String appendixpath;

    /**
     * 资源ID（第三方提供）
     */
    private String dataid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}