package org.zznode.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 模型修改工作流明细表
 *
 * @TableName tb_workflow_model_change
 */
@Data
@TableName(value = "tb_workflow_model_change")
public class TbWorkflowModelChange implements Serializable {
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
}