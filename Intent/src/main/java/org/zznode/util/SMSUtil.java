package org.zznode.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;

import org.springframework.stereotype.Service;
import org.zznode.beans.Md5Util;
import org.zznode.beans.ReqSMS;
import org.zznode.beans.ResSMS;

@Service
public class SMSUtil {

    private static String apId = "wxsz"; // 接口账号用户名

    private static String secretKey = "Shzy12@,";

    private static String ecName = "上海中移通信技术工程有限公司"; // 集团名称

    private static String sign = "fpgvlK8g3"; // 网关签名编码

    public static String url = "http://112.35.1.155:1992/sms/norsubmit"; // 请求url

    private static String addSerial = ""; // 拓展码 填空

    public static final String SIGN_SMS_NAME = "【中移党建】";


    public static int sendMsg(String mobiles, String content) throws IOException {

        ReqSMS reqSMS = new ReqSMS();
        reqSMS.setApId(apId);
        reqSMS.setEcName(ecName);
        reqSMS.setSecretKey(secretKey);
        reqSMS.setContent(content + SIGN_SMS_NAME);
        reqSMS.setMobiles(mobiles);
        reqSMS.setAddSerial(addSerial);
        reqSMS.setSign(sign);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(reqSMS.getEcName());
        stringBuffer.append(reqSMS.getApId());
        stringBuffer.append(reqSMS.getSecretKey());
        stringBuffer.append(reqSMS.getMobiles());
        stringBuffer.append(reqSMS.getContent());
        stringBuffer.append(reqSMS.getSign());
        stringBuffer.append(reqSMS.getAddSerial());

        reqSMS.setMac(Md5Util.MD5(stringBuffer.toString()).toLowerCase());
        String reqSMSText = JSON.toJSONString(reqSMS);
        String encode = Base64.encodeBase64String(reqSMSText.getBytes());

        String resStr = sendPost(url, encode);
        ResSMS resSMS = JSON.parseObject(resStr, ResSMS.class);
        if (resSMS.isSuccess() && !"".equals(resSMS.getMsgGroup()) && "success".equals(resSMS.getRspcod())) {
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * main方法测试发送短信，返回1表示成功，0表示失败
     */
//    public static void main(String[] args) throws IOException {
//        int result = sendMsg("18516101039","这周六是阴历15，一起钓鱼去");
//        System.out.println(result);
//    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数
     * @return 所代表远程资源的响应结果
     */
    private static String sendPost(String url, String param) {
        OutputStreamWriter out = null;

        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(param);
            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}
