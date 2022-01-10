package org.zznode.beans;

import lombok.Getter;
import lombok.Setter;

/**
 * 发送短信响应实体
 */
@Getter
@Setter
public class ResSMS {

    private String rspcod;// 响应码
    private String msgGroup;// 消息批次号，由云MAS平台生成，用于验证短信提交报告和状态报告的一致性（取值msgGroup）注:如果数据验证不通过msgGroup为空
    private boolean success;

}
