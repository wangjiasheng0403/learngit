package org.zznode.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * tb_auth_rule
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_auth_rule")
public class TbAuthRule extends Model<TbAuthRule> {
    /**
     * 缺省ID
     */
    @TableId
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntfname() {
        return intfname;
    }

    public void setIntfname(String intfname) {
        this.intfname = intfname;
    }

    public String getAuthlist() {
        return authlist;
    }

    public void setAuthlist(String authlist) {
        this.authlist = authlist;
    }

    /**
     * 接口名称
     */
    private String intfname;

    /**
     * 授权列表
     */
    private String authlist;

    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
