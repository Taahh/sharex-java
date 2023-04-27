package dev.taah.sharex.util;

import com.github.kwhat.jnativehook.NativeInputEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import dev.taah.sharex.manager.KeyManager;

import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Iterator;

public class KeyUtils {

    public static String keyCodesToString(Collection<Integer> collection) {
        final StringBuilder res = new StringBuilder();
        final Iterator<Integer> iterator = collection.iterator();
        while (iterator.hasNext()) {
            res.append(NativeKeyEvent.getKeyText(iterator.next()));
            if (iterator.hasNext()) {
                res.append("+");
            }
        }
        return res.toString();
    }

    public static String clickedKeysToString(Collection<KeyManager.ClickedKey> collection) {
        final StringBuilder res = new StringBuilder();
        final Iterator<KeyManager.ClickedKey> iterator = collection.iterator();
        while (iterator.hasNext()) {
            KeyManager.ClickedKey key = iterator.next();
            if (key.location() != NativeKeyEvent.KEY_LOCATION_UNKNOWN) {
                switch (key.location()) {
                    case NativeKeyEvent.KEY_LOCATION_LEFT -> res.append('L');
                    case NativeKeyEvent.KEY_LOCATION_RIGHT -> res.append('R');
                    case NativeKeyEvent.KEY_LOCATION_NUMPAD -> res.append('N');
                }
            }
            String keyText = NativeKeyEvent.getKeyText(key.keyCode());
            res.append(keyText);
            if (iterator.hasNext()) {
                res.append("+");
            }
        }
        return res.toString();
    }

}
