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
 * tb_users
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_users")
public class TbUsers extends Model<TbUsers> {
    /**
     * 用户ID
     */
    @TableId("userID")
    private Long userid;

    public String getPortraitpath() {
        return portraitpath;
    }

    public void setPortraitpath(String portraitpath) {
        this.portraitpath = portraitpath;
    }

    private String portraitpath;
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

    private String nickname;

    private Long departdutyid;

    private Long parydutyid;

    private static final long serialVersionUID = 1L;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getDepartid() {
        return departid;
    }

    public void setDepartid(Long departid) {
        this.departid = departid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAccountstatus() {
        return accountstatus;
    }

    public void setAccountstatus(String accountstatus) {
        this.accountstatus = accountstatus;
    }

    public Long getParyid() {
        return paryid;
    }

    public void setParyid(Long paryid) {
        this.paryid = paryid;
    }

    public String getCreatetime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (this.createtime == null ){
            return null;
        }
        return format.format(this.createtime);
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getDepartdutyid() {
        return departdutyid;
    }

    public void setDepartdutyid(Long departdutyid) {
        this.departdutyid = departdutyid;
    }

    public Long getParydutyid() {
        return parydutyid;
    }

    public void setParydutyid(Long parydutyid) {
        this.parydutyid = parydutyid;
    }

    @Override
    protected Serializable pkVal() {
        return this.userid;
    }
}
