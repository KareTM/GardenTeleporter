package kare.gardenteleporter.client;

import kare.gardenteleporter.client.PlotData.PlotData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

public class PlotGrabber {
    // Blacklist items
    private static final List<ItemStack> blacklistedItems = List.of(
            new ItemStack(Items.BLACK_STAINED_GLASS_PANE),
            new ItemStack(Items.ARROW),
            new ItemStack(Items.BARRIER),
            new ItemStack(Items.SPRUCE_PLANKS)
    );

    private static final List<Integer> plotNumbers = List.of(21, 13, 9, 14, 22,
            15, 5, 1, 6, 16,
            10, 2, 3, 11,
            17, 7, 4, 8, 18,
            23, 19, 12, 20, 24);

    private static final long COOLDOWN_TIME = 10000;
    private static long lastActionTime = 0;

    public static List<PlotData> detectAndExtractPlotData() {
        long currentTime = System.currentTimeMillis();

        // Check if enough time has passed since the last action
        if (currentTime - lastActionTime < COOLDOWN_TIME) {
            return null; // Cooldown not complete, skip action
        }

        Screen currentScreen = MinecraftClient.getInstance().currentScreen;

        if (currentScreen == null)
            return null;

        if (currentScreen instanceof GenericContainerScreen inventoryScreen) {
            // Assuming the inventory title contains "Configure Plots" or some identifier
            if (inventoryScreen.getTitle().getString().contains("Configure Plots")) {
                lastActionTime = currentTime;
                return extractPlotData(inventoryScreen);
            }
        }

        return null;
    }

    static List<PlotData> extractPlotData(GenericContainerScreen inventoryScreen) {
        // Get the screen handler that represents the inventory container
        var screenHandler = inventoryScreen.getScreenHandler();

        // Create a list to store the plot data
        List<PlotData> plotDataList = new ArrayList<>();

        int plotNum = 0;
        // Assuming plot items are in specific slots, iterate over the slots
        for (int i = 0; i < 54; i++) {
            Slot slot = screenHandler.getSlot(i);
            ItemStack itemStack = slot.getStack();

            // Skip blacklisted items
            if (isBlacklisted(itemStack)) {
                continue;
            }

            if (!itemStack.isEmpty()) {
                // Here, we can assume that any non-empty slot holds a plot
                String plotName = itemStack.getName().getString();
                if (!plotName.contains("Plot - "))
                    continue;
                else {
                    plotName = plotName.replace("Plot - ", "");
                }

                // Store the plot data
                plotDataList.add(new PlotData(plotName, itemStack.getItem(), plotNumbers.get(plotNum++)));
            }
        }

        return plotDataList;
    }

    static boolean isBlacklisted(ItemStack itemStack) {
        // Check if the item matches any of the blacklisted items
        for (ItemStack blacklistedItem : blacklistedItems) {
            if (itemStack.itemMatches(blacklistedItem.getRegistryEntry())) {
                return true;
            }
        }
        return false;
    }
}