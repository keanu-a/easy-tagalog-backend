package org.alouastudios.easytagalogbackend.util;

import lombok.experimental.UtilityClass;
import org.alouastudios.easytagalogbackend.enums.Tense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ServiceUtil {

    public static String convertAccentArrayToString(List<Integer> accents) {
        List<String> accentStrings = new ArrayList<>();

        for (Integer i : accents) {
            accentStrings.add(String.valueOf(i));
        }

        return String.join(",", accentStrings);
    }

    public static String convertOrderArrayToString(List<String> wordOrder) {
        return String.join(",", wordOrder);
    }

    public static List<String> convertOrderStringToArray(String wordOrder) {
        return Arrays.asList(wordOrder.split(","));
    }

    public static boolean isNumber(String numberString) {
        return numberString.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isTense(String tenseString) {
        for (Tense tense : Tense.values()) {
            if (tenseString.equals(tense.name())) {
                return true;
            }
        }

        return false;
    }

    public static String createAudioString(String tagalog) {
        return tagalog.concat(".mp3");
    }
}
