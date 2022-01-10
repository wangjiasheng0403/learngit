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
 * 组织架构修改工作流明细表
 *
 * @TableName tb_workflow_organization_change
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_workflow_organization_change")
public class TbWorkflowOrganizationChange extends Model<TbWorkflowOrganizationChange> {
    /**
     * 缺省主键
     */
    @TableId
    private Long id;

    /**
     * 要修改的组织架构id（增加操作是该字段为null）
     */
    private Long operationdepartid;

    /**
     * 申请创建部门所挂上级部门ID
     */
    private Long parentdepartid;

    /**
     * 申请创建部门名称
     */
    private String departname;

    /**
     * 类型 1：增加 2：修改 3：删除
     */
    private Integer type;

    /**
     * 附件，证明材料
     */
    private String attachment;

    /**
     *
     */
    private Long workflowinfoid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}