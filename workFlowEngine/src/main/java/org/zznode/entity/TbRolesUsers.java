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
 * 角色用户中间表
 *
 * @TableName tb_roles_users
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_roles_users")
public class TbRolesUsers extends Model<TbRolesUsers> {
    /**
     * 缺省主键
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long userid;

    /**
     * 角色ID
     */
    private Long rolesid;

    /**
     * 创建时间
     */
    private Date createtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}