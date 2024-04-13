package aster.amo.erosianmagic.util;

import aster.amo.erosianmagic.client.ErosianMagicClient;

import java.util.Arrays;

public class ClientClassUtils {
    public static boolean isOneOfClasses(String... classes) {
        return Arrays.stream(classes).anyMatch(r -> r.equals(ErosianMagicClient.CLASS));
    }

    public static String getChosenClass() {
        return ErosianMagicClient.CLASS;
    }
}
