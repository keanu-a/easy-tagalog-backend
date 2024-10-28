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

    public static List<Integer> convertStringToAccentArray(String string) {
        String[] accentStrings = string.split(",");

        List<Integer> accentIntegers = new ArrayList<>();
        for (String accentString : accentStrings) {
            accentIntegers.add(Integer.parseInt(accentString.trim()));
        }

        return accentIntegers;
    }

    public static boolean isNumber(String numberString) {
        return numberString.matches("-?\\d+(\\.\\d+)?");
    }

    public static String createWordAudioString(String tagalog) {
        return tagalog.concat(".mp3");
    }
}
