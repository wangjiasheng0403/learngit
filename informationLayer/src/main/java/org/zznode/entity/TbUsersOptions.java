package org.zznode.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户操作日志表
 *
 * @TableName tb_users_options
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_users_options")
public class TbUsersOptions extends Model<TbUsersOptions> {
    /**
     * 缺省主键
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userid;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 被修改用户ID'
     */
    private Long modifuserid;

    /**
     * 被修改用户账号
     */
    private String modifaccount;

    /**
     * 操作类型
     */
    private String optiontype;

    /**
     * 操作发生在后台具体菜单名
     */
    private String optionbymenu;

    /**
     * 操作发生在菜单的字段
     */
    private String optionbyfield;

    /**
     * 操作内容
     */
    private String optioninfo;

    /**
     * 创建时间
     */
    private Date createtime;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TbUsersOptions other = (TbUsersOptions) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
                && (this.getAccount() == null ? other.getAccount() == null : this.getAccount().equals(other.getAccount()))
                && (this.getOptiontype() == null ? other.getOptiontype() == null : this.getOptiontype().equals(other.getOptiontype()))
                && (this.getOptioninfo() == null ? other.getOptioninfo() == null : this.getOptioninfo().equals(other.getOptioninfo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getAccount() == null) ? 0 : getAccount().hashCode());
        result = prime * result + ((getOptiontype() == null) ? 0 : getOptiontype().hashCode());
        result = prime * result + ((getOptioninfo() == null) ? 0 : getOptioninfo().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userid=").append(userid);
        sb.append(", account=").append(account);
        sb.append(", optiontype=").append(optiontype);
        sb.append(", optioninfo=").append(optioninfo);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}