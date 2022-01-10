package org.zznode.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具，对密码进行处理
 *
 * @author
 */

public class MD5Utils {

    /**
     * MD5密码加密盐值
     */
    public static final String SALT = "zznode;@17#zydj";


    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        //MD5可以被枚举破解，对输入的密码加上盐值（Constant.SALT），防止被破解
        return Base64.encodeBase64String(md5.digest((strValue + SALT).getBytes()));
    }

}
