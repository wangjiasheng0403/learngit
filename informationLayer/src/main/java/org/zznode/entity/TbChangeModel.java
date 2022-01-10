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
 * @TableName tb_change_model
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_change_model")
public class TbChangeModel extends Model<TbChangeModel> {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 资源点位修改展示图片序号
     */
    private Integer pictureid;

    /**
     * 素材id
     */
    private Integer mateid;

    /**
     * 素材类型
     */
    private Integer matetype;

    /**
     * 素材存放地址
     */
    private String matepath;

    /**
     * workflow_model_change表主键id
     */
    private Long workflowmodelchangeid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}