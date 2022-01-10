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
 * 角色表
 *
 * @TableName tb_roles
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_roles")
public class TbRoles extends Model<TbRoles> {
    /**
     * 角色ID
     */
    @TableId
    private Long rolesid;

    /**
     * 角色名称
     */
    private String rolesname;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 描述信息
     */
    private String descriptor;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}