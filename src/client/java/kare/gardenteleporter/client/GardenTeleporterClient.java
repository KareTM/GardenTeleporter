package kare.gardenteleporter.client;

import kare.gardenteleporter.client.PlotData.PlotDataManager;
import kare.gardenteleporter.client.UI.GardenKeybindsScreen;
import kare.gardenteleporter.client.UI.PlotScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class GardenTeleporterClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        var keys = getKeybinds();
        KeybindStorage.loadKeybinds(keys);
        PlotDataManager.loadPlotData();


        var screenkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.gardenteleporter.plotscreen", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_Z, // The keycode of the key
                "category.gardenteleporter.keys" // The translation key of the keybinding's category.
        ));

        var keybindKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.gardenteleporter.keyscreen", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_COMMA, // The keycode of the key
                "category.gardenteleporter.keys" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            var newPlotData = PlotGrabber.detectAndExtractPlotData();
            if (newPlotData != null) {
                PlotDataManager.savePlotData(newPlotData);
            } else {
                newPlotData = PlotDataManager.plotData;
            }

            if (screenkey.isPressed()) {
                MinecraftClient.getInstance().setScreenAndRender(new PlotScreen(newPlotData, keys));
            }

            if (keybindKey.isPressed()) {
                MinecraftClient.getInstance().setScreenAndRender(new GardenKeybindsScreen(keys));
            }
        });
    }


    private List<KeyBinding> getKeybinds() {
        var hotkey1 = new KeyBinding(
                "key.gardenteleporter.key1", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_1, // The keycode of the key
                "category.gardenteleporter.keys" // The translation key of the keybinding's category.
        );

        var hotkey2 = new KeyBinding(
                "key.gardenteleporter.key2", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_2, // The keycode of the key
                "category.gardenteleporter.keys" // The translation key of the keybinding's category.
        );

        var hotkey3 = new KeyBinding(
                "key.gardenteleporter.key3", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_3, // The keycode of the key
                "category.gardenteleporter.keys" // The translation key of the keybinding's category.
        );

        var hotkey4 = new KeyBinding(
                "key.gardenteleporter.key4", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_4, // The keycode of the key
                "category.gardenteleporter.keys" // The translation key of the keybinding's category.
        );

        var hotkey5 = new KeyBinding(
                "key.gardenteleporter.key5", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_5, // The keycode of the key
                "category.gardenteleporter.keys" // The translation key of the keybinding's category.
        );
        return List.of(hotkey1, hotkey2, hotkey3, hotkey4, hotkey5);
    }
}
