package dev.taah.sharex.manager;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Accessors(fluent = true)
@Getter
public class KeyManager {
    private final SortedSet<ClickedKey> screenshotKeybind = new TreeSet<>();
    private final Integer cancelKeybind = NativeKeyEvent.VC_ESCAPE;

    public KeyManager() {
        screenshotKeybind.add(new ClickedKey(NativeKeyEvent.VC_CONTROL, NativeKeyEvent.KEY_LOCATION_LEFT));
        screenshotKeybind.add(new ClickedKey(NativeKeyEvent.VC_SHIFT, NativeKeyEvent.KEY_LOCATION_LEFT));
        screenshotKeybind.add(new ClickedKey(NativeKeyEvent.VC_V, NativeKeyEvent.KEY_LOCATION_STANDARD));
    }

    public record ClickedKey(int keyCode, int location) implements Comparable<ClickedKey> {

        @Override
        public int compareTo(ClickedKey clickedKey) {
            return Integer.compare(clickedKey.keyCode + clickedKey.location, this.keyCode + this.location);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ClickedKey clickedKey)) return false;

            return clickedKey.keyCode == this.keyCode && clickedKey.location == this.location;
        }
    }
}
