package org.zznode.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论记录表
 *
 * @TableName tb_makes
 */
@TableName(value = "tb_makes")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class TbMakes extends Model<TbMakes> {
    /**
     * 缺省主键
     */
    private Long id;

    /**
     * 用户访问记录表主键
     */
    private Long logsid;

    /**
     * 评论内容
     */
    private String makeInfo;

    /**
     *
     */
    private String nickname;

    /**
     *
     */
    private Date ctime;

    @TableField(exist = false)
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
        TbMakes other = (TbMakes) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getLogsid() == null ? other.getLogsid() == null : this.getLogsid().equals(other.getLogsid()))
                && (this.getMakeInfo() == null ? other.getMakeInfo() == null : this.getMakeInfo().equals(other.getMakeInfo()))
                && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
                && (this.getCtime() == null ? other.getCtime() == null : this.getCtime().equals(other.getCtime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLogsid() == null) ? 0 : getLogsid().hashCode());
        result = prime * result + ((getMakeInfo() == null) ? 0 : getMakeInfo().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getCtime() == null) ? 0 : getCtime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", logsid=").append(logsid);
        sb.append(", makeInfo=").append(makeInfo);
        sb.append(", nickname=").append(nickname);
        sb.append(", ctime=").append(ctime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}