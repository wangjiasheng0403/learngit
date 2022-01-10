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
 * 党组织关系表
 *
 * @TableName tb_pary
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_pary")
public class TbPary extends Model<TbPary> {
    /**
     * 党组织机构ID
     */
    @TableId
    private Long partid;

    /**
     * 上级党组织机构ID
     */
    private Long fatherid;

    /**
     * 树的层级;1-n,1是最顶层部门
     */
    private Integer treelv;

    /**
     * 更新时间
     */
    private Date createtime;

    /**
     * 证明材料存放地址
     */
    private String provepath;

    /**
     * 党组织机构名称
     */
    private String partname;

    /**
     * 所属二级组织ID
     */
    private Long secondpartid;

    /**
     * 真实层级
     */
    private String reallv;

    /**
     * 所属二级组织ID
     */
    private Long secondlv;

    /**
     * 是否删除1：删除 0未删除
     */
    private Integer isdeleted;

    /**
     * userId
     */
    private Long createby;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}