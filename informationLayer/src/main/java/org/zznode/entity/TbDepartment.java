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
 * tb_department
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_department")
public class TbDepartment extends Model<TbDepartment> {
    /**
     * 部门ID
     */
    @TableId("departID")
    private Long departid;

    /**
     * 父部门ID
     */
    private Long fatherid;

    /**
     * 逻辑层级;1-n,1是最顶层部门
     */
    private Integer lv;

    /**
     * 部门名称
     */
    private String departname;

    /**
     * 更新时间
     */
    private Date createtime;

    /**
     * 所属党组织机构ID
     */
    private Long partid;

    /**
     * 所属党组织机构名称
     */
    private String partname;

    /**
     * 所属二级部门ID
     */
    private Long seconddepartid;

    public Long getDepartid() {
        return departid;
    }

    public void setDepartid(Long departid) {
        this.departid = departid;
    }

    public Long getFatherid() {
        return fatherid;
    }

    public void setFatherid(Long fatherid) {
        this.fatherid = fatherid;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public String getDepartname() {
        return departname;
    }

    public void setDepartname(String departname) {
        this.departname = departname;
    }

    public String getCreatetime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(this.createtime);
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getPartid() {
        return partid;
    }

    public void setPartid(Long partid) {
        this.partid = partid;
    }

    public String getPartname() {
        return partname;
    }

    public void setPartname(String partname) {
        this.partname = partname;
    }

    public Long getSeconddepartid() {
        return seconddepartid;
    }

    public void setSeconddepartid(Long seconddepartid) {
        this.seconddepartid = seconddepartid;
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return this.departid;
    }
}
