package org.zznode.util;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GetUuidUtil {
    private static  char[] UUID_CHARSET = "0123456789"
            .toCharArray();

    private String randomUUID(int length) {

        char[] result = new char[length];
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            int n = rand.nextInt(UUID_CHARSET.length);
            result[i] = UUID_CHARSET[n];
        }
        return new String(result);
    }


    public String getVerificationCode() {
        String uuid = this.randomUUID(6);
        return uuid;
    }

}
