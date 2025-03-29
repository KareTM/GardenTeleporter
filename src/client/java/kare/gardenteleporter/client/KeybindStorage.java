package kare.gardenteleporter.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
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

    public static void saveKeybinds(List<KeyBinding> keybinds) {
        Map<String, String> keybindMap = new HashMap<>();
        for (KeyBinding keybind : keybinds) {
            keybindMap.put(keybind.getTranslationKey(), keybind.getBoundKeyTranslationKey());
        }

        try (Writer writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(keybindMap, writer);
        } catch (IOException e) {
            log.error("Failed to save keybinds", e);
        }
    }

    public static void loadKeybinds(List<KeyBinding> keybinds) {
        if (!CONFIG_FILE.exists()) return;

        try (Reader reader = new FileReader(CONFIG_FILE)) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> keybindMap = GSON.fromJson(reader, type);

            for (KeyBinding keybind : keybinds) {
                if (keybindMap.containsKey(keybind.getTranslationKey())) {
                    String keyCode = keybindMap.get(keybind.getTranslationKey());
                    keybind.setBoundKey(InputUtil.fromTranslationKey(keyCode));
                }
            }
        } catch (IOException e) {
            log.error("Failed to load keybinds", e);
        }
    }
}