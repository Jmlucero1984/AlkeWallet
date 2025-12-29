package Helpers;

import java.util.Random;

public class RandomStringGenerators {
    public static String getRandomString(int length){
        Random random = new Random();
        char[] chainOfChars = new char[length];
        for (int i = 0; i < length; i++) {
            // 'a' = 97, 'z' = 122
            chainOfChars[i] = (char) ('a' + random.nextInt(26));
        }

        return new String(chainOfChars);
    }

}
