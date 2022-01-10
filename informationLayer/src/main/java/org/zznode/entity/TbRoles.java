package org.zznode.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * tb_roles
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_roles")
public class TbRoles extends Model<TbRoles> {
    /**
     * 角色ID
     */
    @TableId("rolesID")
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

    public Long getRolesid() {
        return rolesid;
    }

    public void setRolesid(Long rolesid) {
        this.rolesid = rolesid;
    }

    public String getRolesname() {
        return rolesname;
    }

    public void setRolesname(String rolesname) {
        this.rolesname = rolesname;
    }

    public String getCreatetime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(this.createtime);
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
