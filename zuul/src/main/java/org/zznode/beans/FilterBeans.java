package org.zznode.beans;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zznode.filter.MyFilter;

@Configuration
public class FilterBeans {

    @Bean
    public MyFilter myFilter(){
        return new MyFilter();
    }
}
