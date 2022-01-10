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
 * 数据域
 *
 * @TableName tb_data_resource
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_data_resource")
public class TbDataResource extends Model<TbDataResource> {
    /**
     * 资源编号;从睿悦同步来的资源编号
     */
    @TableId
    private String dataid;

    /**
     * 更新时间
     */
    private Date createtime;

    /**
     * 资源描述
     */
    private String datainfo;

    /**
     * 资源所属部门;存储部门ID的为部门资源
     */
    private Long databelong;

    /**
     * 资源类型;0为模版，1为公开资源，2为红色资源，3为私有资源
     */
    private Integer datatype;

    /**
     * 资源名称
     */
    private String dataname;

    /**
     * 是否为部门缺省展示资源
     */
    private String showtype;

    /**
     * 资源存放路径
     */
    private String datapath;

    /**
     * 资源所依赖的模型ID
     */
    private String basemodelid;

    /**
     * 流程编号
     */
    private String workflowcode;

    /**
     * 场景缩略图存放路径
     */
    private String imgpath;

    /**
     * 推荐模版排序
     */
    private Integer apriveorder;

    /**
     * 场景状态;1为预发布，2为发布
     */
    private Integer datastatus;

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * 编辑人
     */
    private String editor;

    public String getDataid() {
        return dataid;
    }

    public void setDataid(String dataid) {
        this.dataid = dataid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getDatainfo() {
        return datainfo;
    }

    public void setDatainfo(String datainfo) {
        this.datainfo = datainfo;
    }

    public String getDatabelong() {
        if(databelong == null) {
            return null;
        }
        return databelong+"";
    }

    public void setDatabelong(Long databelong) {
        this.databelong = databelong;
    }

    public Integer getDatatype() {
        return datatype;
    }

    public void setDatatype(Integer datatype) {
        this.datatype = datatype;
    }

    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    public String getShowtype() {
        return showtype;
    }

    public void setShowtype(String showtype) {
        this.showtype = showtype;
    }

    public String getDatapath() {
        return datapath;
    }

    public void setDatapath(String datapath) {
        this.datapath = datapath;
    }

    public String getBasemodelid() {
        return basemodelid;
    }

    public void setBasemodelid(String basemodelid) {
        this.basemodelid = basemodelid;
    }

    public String getWorkflowcode() {
        return workflowcode;
    }

    public void setWorkflowcode(String workflowcode) {
        this.workflowcode = workflowcode;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public Integer getApriveorder() {
        return apriveorder;
    }

    public void setApriveorder(Integer apriveorder) {
        this.apriveorder = apriveorder;
    }

    public Integer getDatastatus() {
        return datastatus;
    }

    public void setDatastatus(Integer datastatus) {
        this.datastatus = datastatus;
    }
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return this.dataid;
    }
}
