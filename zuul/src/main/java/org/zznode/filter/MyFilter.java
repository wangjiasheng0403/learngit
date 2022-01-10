package org.zznode.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MyFilter extends ZuulFilter {

    @Override
    public String filterType(){
        return "pre";
    }

    @Override
    public int filterOrder(){
        return 0;
    }

    @Override
    public boolean shouldFilter(){
        return true;
    }

    @Value("${white.ip}")
    private String whiteipList;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Object run(){
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        System.out.println(String.format("%s >>> %s",request.getMethod(),request.getRequestURL().toString()));

        //头部攻击
        String[] whiteList = whiteipList.split(";");
        String adress = request.getHeader("host");
        String[] Hosts = adress.split(":");
        Boolean t =false;
        for(String e:whiteList){
            if(e.equals(Hosts[0])){
                t=true;
            }
        }
        if(!t){
            System.out.println("Host is illegality");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);

            try {
                ctx.getResponse().getWriter().write("Host is illegality");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        //免校验
        if (request.getRequestURI().contains("v2/api-docs") || request.getRequestURI().contains("swagger") ) {
            return null;
        }
        //免校验
        if(request.getRequestURI().contains("Login") || request.getRequestURI().contains("RedData") ){
            return null;
        }


        System.out.println("request.getRequestURI().：" + request.getRequestURI());
        String uri = request.getRequestURI();
        String intfname = uri.substring(uri.lastIndexOf("/")+1,uri.length());
        System.out.println("intfname:" +  intfname);

        String accessToken = request.getHeader ("Authorization");
        if(accessToken == null){
            System.out.println("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);

            try {
                ctx.getResponse().getWriter().write("token is empty write");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }else{
            Map<String,String> params = new HashMap<>();
            params.put("token",accessToken);

            ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/getAuth?token={token}",JSONObject.class,params);
            System.out.println("response.getBody():" + response.getBody());
            JSONObject json =  response.getBody();
            System.out.println("json:" + json);
            JSONObject jb = JSON.parseObject(json.getString("auth"));
            String authlist = jb.getString("authoritylist");
            System.out.println("authlist:" + authlist);

            String[] redisAuthlist = authlist.substring(1,authlist.length()-1).split(",");


            Map<String, String> m = new HashMap<>();
            m.put("intfname", intfname);
            ResponseEntity<JSONObject> jsonAlist = restTemplate.getForEntity("http://informationLayer/master/serchAuth?intfname={intfname}", JSONObject.class, m);
            System.out.println("jsonAlist:" + jsonAlist);
            String dbAuthlist = jsonAlist.getBody().getString("authlist");
            String[] dbAuth = dbAuthlist.split(",");
            System.out.println("dbAuthlist:" + dbAuthlist);

            for(String e: redisAuthlist){
                if(Arrays.asList(dbAuth).contains(e)){
                    return null;
                }
            }

            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);

            try {
                ctx.getResponse().getWriter().write("no permission");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

}
