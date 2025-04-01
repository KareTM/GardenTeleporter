package kare.gardenteleporter.client.keybinds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeybindStorage {
    private static final Logger log = LoggerFactory.getLogger(KeybindStorage.class);
    private static final File CONFIG_FILE = new File("config/gardenteleporter_keybinds.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void saveKeybinds(List<KeybindHolder> keybinds) {
        Map<String, Integer> keybindMap = new HashMap<>();
        for (KeybindHolder keybind : keybinds) {
            keybindMap.put(keybind.getTranslationKey(), keybind.getKeyCode());
        }

        try (Writer writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(keybindMap, writer);
        } catch (IOException e) {
            log.error("Failed to save keybinds", e);
        }
    }

    public static void loadKeybinds(List<KeybindHolder> keybinds) {
        if (!CONFIG_FILE.exists()) return;

        try (Reader reader = new FileReader(CONFIG_FILE)) {
            Type type = new TypeToken<Map<String, Integer>>() {
            }.getType();
            Map<String, Integer> keybindMap = GSON.fromJson(reader, type);

            for (KeybindHolder keybind : keybinds) {
                if (keybindMap.containsKey(keybind.getTranslationKey())) {
                    keybind.setKeyCode(keybindMap.get(keybind.getTranslationKey()));
                }
            }
        } catch (IOException e) {
            log.error("Failed to load keybinds", e);
        } catch (JsonSyntaxException e) {
            CONFIG_FILE.delete();
            log.warn("Failed to load keybinds, malformed JSON", e);
        }
    }
}