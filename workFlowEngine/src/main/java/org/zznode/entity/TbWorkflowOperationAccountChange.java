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
 * 运营账号修改工作流明细表
 *
 * @TableName tb_workflow_operation_account_change
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_workflow_operation_account_change")
public class TbWorkflowOperationAccountChange extends Model<TbWorkflowOperationAccountChange> {
    /**
     * 缺省主键
     */
    @TableId
    private Long id;

    /**
     * 申请运营账号的用户表主键id
     */
    private Long operateuserid;

    /**
     * 申请运营账号
     */
    private String operateaccount;

    /**
     * 申请运营账号的密码
     */
    private String operatepwd;

    /**
     * 申请运营账号所在部门
     */
    private Long operatedepartid;

    /**
     * 申请运营账号的用户姓名
     */
    private String username;

    /**
     * 申请运营账号的用户手机号码
     */
    private String phone;

    /**
     * 申请运营账号的用户邮箱
     */
    private String email;

    /**
     * 申请运营账号的性别
     */
    private String sex;

    /**
     * 申请运营账号的账号状态
     */
    private String accountstatus;

    /**
     * 工作流信息ID
     */
    private Long workflowinfoid;

    /**
     * 类型
     * 1：增加
     * 2：修改
     * 3：删除
     */
    private Integer type;

    /**
     * 删除理由
     */
    private String deletecontent;

    /**
     * 附件路径
     */
    private String filepath;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}