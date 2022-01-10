package org.zznode.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户访问日志表
 *
 * @TableName tb_role_logs
 */
@TableName(value = "tb_role_logs")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class TbRoleLogs extends Model<TbRoleLogs> {
    /**
     * 缺省主键
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long userid;

    /**
     * 资源编号
     */
    private String dataid;

    /**
     * 资源名称
     */
    private String dataname;

    /**
     * 访问时间
     */
    private Date ctime;

    /**
     *
     */
    private String liked;

    /**
     * 打分
     */
    private BigDecimal collect;

    /**
     *
     */
    private String terminaltype;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}