package kare.gardenteleporter.client;

import kare.gardenteleporter.client.PlotData.PlotDataManager;
import kare.gardenteleporter.client.UI.PlotScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class GardenTeleporterClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PlotDataManager.loadPlotData();

        var screenkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.gardenteleporter.plotscreen", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_Z, // The keycode of the key
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
                MinecraftClient.getInstance().setScreenAndRender(new PlotScreen(newPlotData));
            }
        });
    }


}
