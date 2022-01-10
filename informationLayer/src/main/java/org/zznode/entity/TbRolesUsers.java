package org.zznode.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * tb_roles_users
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_roles_users")
public class TbRolesUsers extends Model<TbRolesUsers> {
    /**
     * 缺省主键
     */
    @TableId("id")
    private Long id;

    /**
     * 用户ID
     */
    private Long userid;

    /**
     * 角色ID
     */
    private Long rolesid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getRolesid() {
        return rolesid;
    }

    public void setRolesid(Long rolesid) {
        this.rolesid = rolesid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 创建时间
     */
    private Date createtime;

    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
