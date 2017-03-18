package com.khoa.ultils;

import java.util.Random;

public class Ultils {

    public static final int lengthFileName = 30;
    
    public static String formatFileName(String inputName) {
        String outputName;
        String stringRandom = randomString(lengthFileName);
        String typeFile = inputName.substring(inputName.lastIndexOf("."));
        outputName = stringRandom + typeFile;
        /*
         * if (inputName.length() < 220) { outputName = stringRandom + "-" +
         * inputName; } else { outputName = stringRandom + "-" +
         * inputName.substring(0, 220); }
         */
        return outputName;
    }
    
    private static String randomString(int lenght) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = lenght;
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

    public static String getName(String fullName){
        return fullName.substring(0,fullName.indexOf("."));
    }
}
