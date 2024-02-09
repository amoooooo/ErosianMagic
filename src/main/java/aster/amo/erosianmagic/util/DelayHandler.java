package aster.amo.erosianmagic.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DelayHandler {
    private static Map<Integer, Runnable> delays = new HashMap<>();
    private static Map<Integer, Runnable> toAdd = new HashMap<>();

    public static void addDelay(int delay, Runnable runnable) {
        toAdd.put(delay, runnable);
    }

    public static void tick() {
        Iterator<Map.Entry<Integer, Runnable>> iterator = delays.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Runnable> entry = iterator.next();
            int delay = entry.getKey();
            Runnable runnable = entry.getValue();

            if (delay <= 0) {
                runnable.run();
                iterator.remove(); // Safely remove the entry during iteration
            } else {
                iterator.remove(); // Remove the current entry
                toAdd.put(delay - 1, runnable); // Add the updated entry
            }
        }
        delays.putAll(toAdd);
        toAdd.clear();
    }
}
