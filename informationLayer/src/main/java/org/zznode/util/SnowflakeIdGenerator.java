package org.zznode.util;

import org.springframework.stereotype.Service;

@Service
public class SnowflakeIdGenerator {

    public synchronized long nextId() {
        return IdGenerator.generateId();
    }
}
