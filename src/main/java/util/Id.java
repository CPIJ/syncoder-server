package util;

import java.util.Random;

public class Id {

    private static Random random = new Random();

    public static String newId() {
        char[] options = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();

        StringBuilder id = new StringBuilder();

        for(int i = 0; i < 8; i++) {
            id.append(options[random.nextInt(options.length)]);
        }

        return id.toString();
    }
}
