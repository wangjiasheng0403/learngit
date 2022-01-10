package org.zznode.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateUUID {

    public void generateUUID(){
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        long timestamp =  System.currentTimeMillis();
        System.out.println(timestamp);
    }
}
