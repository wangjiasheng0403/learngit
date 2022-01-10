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
 * tb_rules
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_rules")
public class TbRules extends Model<TbRules> {
    public Long getRulesid() {
        return rulesid;
    }

    public void setRulesid(Long rulesid) {
        this.rulesid = rulesid;
    }

    public Date getCreatetime() {
        return createtime;
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

    public String getRulesname() {
        return rulesname;
    }

    public void setRulesname(String rulesname) {
        this.rulesname = rulesname;
    }

    /**
     * 权限ID
     */
    @TableId("rulesID")
    private Long rulesid;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 描述信息
     */
    private String descriptor;

    /**
     * 权限名称
     */
    private String rulesname;

    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
