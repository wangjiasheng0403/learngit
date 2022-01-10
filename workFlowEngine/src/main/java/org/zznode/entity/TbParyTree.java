package org.zznode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_pary")
@JsonIgnoreProperties(value = "handler")
public class TbParyTree {
    @TableId(value = "partID", type = IdType.INPUT)
    private Long partId;
    @TableField(value = "fatherID")
    private Long fatherId;
    @TableField(value = "treeLV")
    private Integer treeLV;
    @TableField(value = "partName")
    private String partName;
    @TableField(exist = false)
    private List<TbParyTree> childNode;
}
