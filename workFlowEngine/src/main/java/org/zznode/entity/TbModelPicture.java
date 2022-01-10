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
 * 模型图示记录表
 *
 * @TableName tb_model_picture
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_model_picture")
public class TbModelPicture extends Model<TbModelPicture> {
    /**
     * 缺省主键
     */
    @TableId
    private Integer id;

    /**
     * 资源编号
     */
    private String dataid;

    /**
     * 资源点位修改展示图片序号
     */
    private Integer datapictureid;

    /**
     * 资源点位修改展示图片路径
     */
    private String datapicturepath;

    /**
     * 创建人
     */
    private String createby;

    /**
     * 创建时间
     */
    private Date createtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}