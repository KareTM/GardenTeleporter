package kare.gardenteleporter.client.PlotData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlotDataManager {

    private static final Gson gson = new Gson();
    private static final File configDir = new File(MinecraftClient.getInstance().runDirectory, "config");
    private static final File plotDataFile = new File(configDir, "gardenteleporter_plot_data.json");
    private static final Logger log = LoggerFactory.getLogger(PlotDataManager.class);
    public static List<PlotData> plotData = new ArrayList<>();

    public static void loadPlotData() {
        List<PlotData> plotDataList = new ArrayList<>();

        try (Reader reader = new FileReader(plotDataFile)) {
            JsonElement jsonElement = gson.fromJson(reader, JsonElement.class);

            // Deserialize the data into a list of PlotData objects
            if (jsonElement.isJsonArray()) {
                jsonElement.getAsJsonArray().forEach(jsonElement1 -> {
                    // Extract data from each element in the array and convert it to a PlotData object
                    String plotName = jsonElement1.getAsJsonObject().get("name").getAsString();
                    int plotNumber = jsonElement1.getAsJsonObject().get("number").getAsInt();
                    JsonElement iconJson = jsonElement1.getAsJsonObject().get("icon");

                    // Deserialize the icon (ItemStack) from JSON
                    ItemStack icon = ItemStack.CODEC.parse(JsonOps.INSTANCE, iconJson).result().orElse(ItemStack.EMPTY);

                    // Create PlotData object from the loaded data
                    PlotData plotData = new PlotData(plotName, icon.getItem(), plotNumber);
                    plotDataList.add(plotData);
                });
            }
        } catch (IOException e) {
            log.error("Failed to load plot data", e);
        }

        plotData = plotDataList;
    }

    public static void savePlotData(List<PlotData> plots) {
        plotData = plots;
        if (!configDir.exists()) {
            boolean res = configDir.mkdirs();
            if (!res) {
                log.error("Failed to create config directory");
            }
        }

        try (Writer writer = new FileWriter(plotDataFile)) {
            var out = new JsonArray();
            for (PlotData plotData : plots) {
                var plot = new JsonObject();
                plot.addProperty("name", plotData.getName());
                plot.addProperty("number", plotData.getNumber());
                var iconEl = ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, new ItemStack(plotData.getIcon()));
                plot.add("icon", iconEl.getOrThrow());
                out.add(plot);
            }

            gson.toJson(out, writer);
        } catch (IOException e) {
            log.error("Failed to save plot data", e);
        }
    }
}
