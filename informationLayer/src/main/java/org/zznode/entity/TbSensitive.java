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
 * 敏感字段表
 *
 * @TableName tb_sensitive
 */
@TableName(value = "tb_sensitive")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class TbSensitive extends Model<TbSensitive> {
    /**
     * 敏感字段编号
     */
    @TableId
    private Long id;

    /**
     * 敏感字段
     */
    private String sensitivity;

    /**
     * 涉敏原因
     */
    private String sensitivitycause;

    /**
     * 创建人
     */
    private String createby;

    /**
     * 创建时间
     */
    private Date createtime;

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
        TbSensitive other = (TbSensitive) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSensitivity() == null ? other.getSensitivity() == null : this.getSensitivity().equals(other.getSensitivity()))
                && (this.getSensitivitycause() == null ? other.getSensitivitycause() == null : this.getSensitivitycause().equals(other.getSensitivitycause()))
                && (this.getCreateby() == null ? other.getCreateby() == null : this.getCreateby().equals(other.getCreateby()))
                && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSensitivity() == null) ? 0 : getSensitivity().hashCode());
        result = prime * result + ((getSensitivitycause() == null) ? 0 : getSensitivitycause().hashCode());
        result = prime * result + ((getCreateby() == null) ? 0 : getCreateby().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sensitivity=").append(sensitivity);
        sb.append(", sensitivitycause=").append(sensitivitycause);
        sb.append(", createby=").append(createby);
        sb.append(", createtime=").append(createtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}