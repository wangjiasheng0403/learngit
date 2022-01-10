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
 * 组织关系表
 *
 * @TableName tb_depart_relation
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_depart_relation")
public class TbDepartRelation extends Model<TbDepartRelation> {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 场景所属组织ID;部门ID代表里这个部门的所有数据权限
     */
    private Long paryid;

    /**
     * 被授权查看的组织ID
     */
    private Long visitparyid;

    /**
     * 组织关系类型;0为本系统，1为共建部门
     */
    private Integer relationtype;

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
        TbDepartRelation other = (TbDepartRelation) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getParyid() == null ? other.getParyid() == null : this.getParyid().equals(other.getParyid()))
                && (this.getVisitparyid() == null ? other.getVisitparyid() == null : this.getVisitparyid().equals(other.getVisitparyid()))
                && (this.getRelationtype() == null ? other.getRelationtype() == null : this.getRelationtype().equals(other.getRelationtype()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getParyid() == null) ? 0 : getParyid().hashCode());
        result = prime * result + ((getVisitparyid() == null) ? 0 : getVisitparyid().hashCode());
        result = prime * result + ((getRelationtype() == null) ? 0 : getRelationtype().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", paryid=").append(paryid);
        sb.append(", visitparyid=").append(visitparyid);
        sb.append(", relationtype=").append(relationtype);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}