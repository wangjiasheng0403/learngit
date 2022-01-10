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
 * 部门表
 *
 * @TableName tb_department
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_department")
public class TbDepartment extends Model<TbDepartment> {
    /**
     * 部门ID
     */
    @TableId
    private Long departid;

    /**
     * 父部门ID
     */
    private Long fatherid;

    /**
     * 逻辑层级;1-n,1是最顶层部门
     */
    private Integer lv;

    /**
     * 部门名称
     */
    private String departname;

    /**
     *
     */
    private Date createtime;

    /**
     * 所属党组织机构ID
     */
    private Long partid;

    /**
     * 所属党组织机构名称
     */
    private String partname;

    /**
     * 所属二级部门ID
     */
    private Long seconddepartid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}