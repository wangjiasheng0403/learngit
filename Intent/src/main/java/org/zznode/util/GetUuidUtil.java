package org.zznode.util;

import java.util.Random;

public class GetUuidUtil {
    private static  char[] UUID_CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
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


    public String GetUUID() {
        String uuid = this.randomUUID(16);
        return uuid;
    }

}
