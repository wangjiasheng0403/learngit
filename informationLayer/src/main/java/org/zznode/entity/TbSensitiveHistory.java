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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_sensitive_history")
public class TbSensitiveHistory extends Model<TbSensitiveHistory> {

    @TableId
    private Long id;

    /**
     * 检查类型: 1场景素材，2评论内容
     */
    private Short checktype;

    /**
     * 场景资源
     */
    private String scenesource;

    /**
     * 素材点
     */
    private String information;

    /**
     * 涉敏内容
     */
    private String sensitivecontent;

    /**
     * 用户账号
     */
    private Long userid;

    /**
     * 检查时间
     */
    private Date checktime;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
