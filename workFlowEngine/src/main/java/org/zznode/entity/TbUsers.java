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
 * 用户表
 *
 * @TableName tb_users
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_users")
public class TbUsers extends Model<TbUsers> {
    /**
     * 用户ID
     */
    @TableId
    private Long userid;

    /**
     * 部门ID;所属行政组织部门
     */
    private Long departid;

    /**
     * 密码;MD5加密存放
     */
    private String password;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 性别
     */
    private String sex;

    /**
     * 账号状态
     */
    private String accountstatus;

    /**
     * 所属党组织ID;所属党组织ID
     */
    private Long paryid;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     *
     */
    private String nickname;

    /**
     *
     */
    private Long departdutyid;

    /**
     *
     */
    private Long parydutyid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}