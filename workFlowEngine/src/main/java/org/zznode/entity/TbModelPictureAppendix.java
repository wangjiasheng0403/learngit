package org.zznode.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @TableName tb_model_picture_appendix
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_model_picture_appendix")
public class TbModelPictureAppendix extends Model<TbModelPictureAppendix> {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * tb_model_picture 表dataId关联
     */
    private Integer dataid;

    /**
     * tb_model_picture 中 dataPictureId
     */
    private Integer pictureid;

    /**
     * 编号 1-n
     */
    private Integer appendixid;

    /**
     * 1 文字
     * 2 音频
     * 3 视频
     * 4 图片
     */
    private Integer type;

    /**
     *
     */
    private String describe;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}