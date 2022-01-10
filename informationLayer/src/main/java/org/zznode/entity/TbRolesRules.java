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
 * tb_roles_rules
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_roles_rules")
public class TbRolesRules extends Model<TbRolesRules> {
    /**
     * 缺省主键
     */
    @TableId("id")
    private Long id;

    /**
     * 角色ID
     */
    private Long rolesid;

    /**
     * 权限ID
     */
    private Long rulesid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRolesid() {
        return rolesid;
    }

    public void setRolesid(Long rolesid) {
        this.rolesid = rolesid;
    }

    public Long getRulesid() {
        return rulesid;
    }

    public void setRulesid(Long rulesid) {
        this.rulesid = rulesid;
    }

    public String getCreatetime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(this.createtime);
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
