package com.liferay.sales.engineering.pulse.util;

import org.json.JSONException;
import org.json.JSONObject;

public class StringUtils {
    public static String convertToFirstLetterLowerCase(final String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        final char c[] = text.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    public static String convertToTitleCaseIteratingChars(final String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    public static String generateToken(final int length, final String validCharacters) {
        if (length < 1) {
            return "";
        }
        if (isBlank(validCharacters)) {
            throw new IllegalArgumentException("validCharacters cannot be blank");
        }
        final StringBuilder token = new StringBuilder(length);
        final int availableCharacters = validCharacters.length();
        int counter = 0;
        while (counter < length) {
            token.append(validCharacters.charAt((int) Math.floor(Math.random() * availableCharacters)));
            counter++;
        }
        return token.toString();
    }

    public static String generateToken(final int length) {
        return generateToken(length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
    }

    public static boolean isBlank(final String string) {
        return string == null || string.isBlank();
    }

    public static boolean isNotBlank(final String string) {
        return !isBlank(string);
    }

    public static <T> String toJson(final T obj) {
        try {
            JSONObject jsonObject = new JSONObject(obj);
            return jsonObject.toString(4);
        } catch (JSONException e) {
            return null;
        }
    }
}
