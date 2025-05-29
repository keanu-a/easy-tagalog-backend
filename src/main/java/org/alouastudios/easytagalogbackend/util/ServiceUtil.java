package org.alouastudios.easytagalogbackend.util;

import lombok.experimental.UtilityClass;
import org.alouastudios.easytagalogbackend.enums.Tense;

import java.text.Normalizer;
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
        if (string == null || string.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String[] accentStrings = string.split(",");

        List<Integer> accentIntegers = new ArrayList<>();
        for (String accentString : accentStrings) {
            accentIntegers.add(Integer.parseInt(accentString.trim()));
        }

        return accentIntegers;
    }

    public static String createWordAudioString(String tagalogWord) {
        return "audio/words/" + sanitizeForAudioFilename(tagalogWord) + ".mp3";
    }

    public static String createPhraseAudioString(String tagalogPhrase) {
        return "audio/phrases/" + sanitizeForAudioFilename(tagalogPhrase) + ".mp3";
    }

    private static String sanitizeForAudioFilename(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // Remove accents
                .replaceAll("[^a-zA-Z0-9 ]", "") // Remove punctuation
                .trim()
                .toLowerCase()
                .replace(" ", "-");
    }
}
