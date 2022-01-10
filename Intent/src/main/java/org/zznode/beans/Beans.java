package org.zznode.beans;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Beans {

    @Bean
    @LoadBalanced // 标注此注解后，RestTemplate就具有了客户端负载均衡能力
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
