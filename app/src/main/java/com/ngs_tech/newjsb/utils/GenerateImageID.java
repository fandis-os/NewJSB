package com.ngs_tech.newjsb.utils;

import java.util.Random;

public class GenerateImageID {
    /**
     * Genera una password RANDOM
     */

    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
    public static Random RANDOM = new Random();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }
}
