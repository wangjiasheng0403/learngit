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
 * tb_users_score
 * @author
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_users_score")
public class TbUsersScore extends Model<TbUsersScore> {
    /**
     * 缺省主键
     */
    @TableId("id")
    private Long id;

    /**
     * 用户ID
     */
    private Long userid;

    /**
     * 用户名称
     */
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimequantum() {
        return timequantum;
    }

    public void setTimequantum(String timequantum) {
        this.timequantum = timequantum;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getParyid() {
        return paryid;
    }

    public void setParyid(Long paryid) {
        this.paryid = paryid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 统计时段
     */
    private String timequantum;

    /**
     * 分值
     */
    private Integer score;

    /**
     * 用户所属部门
     */
    private Long paryid;

    /**
     * 更新时间
     */
    private Date createtime;

    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
