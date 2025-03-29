package kare.gardenteleporter.client.UI;

import kare.gardenteleporter.client.PlotData.PlotData;
import kare.gardenteleporter.client.UI.util.Position;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;

public class PlotButton {
    public static final int buttonSize = 20;
    private final int x;
    private final int y;
    private final Item itemIcon;
    private final int number;
    private final ButtonWidget buttonWidget;
    public boolean active;

    public PlotButton(int x, int y, String name, Item itemIcon, int number) {
        this.x = x;
        this.y = y;
        this.itemIcon = itemIcon;
        this.number = number;
        this.active = true;

        boolean isUnknown = name.equals("UNKNOWN PLOT");

        Tooltip tt = Tooltip.of(Text.literal("Teleport to Plot " + name));
        if (isUnknown) {
            tt = Tooltip.of(Text.literal("Plot data missing. Open Skyblock Plot Screen!").setStyle(Style.EMPTY.withBold(true).withItalic(true).withColor(Formatting.RED)));
        }

        this.buttonWidget = ButtonWidget.builder(Text.literal(""), button -> {
                    // Send the command to the server
                    var networkHandler = MinecraftClient.getInstance().getNetworkHandler();
                    if (networkHandler != null && !isUnknown) {
                        networkHandler.sendCommand("plottp " + name);
                    }

                    MinecraftClient.getInstance().setScreen(null);
                }).dimensions(x, y, buttonSize, buttonSize)  // Button width and height
                .tooltip(tt).build();
    }

    public static PlotButton fromPlotData(PlotData plotData, Map<Integer, Position> map) {
        var pos = map.get(plotData.getNumber());
        return new PlotButton(pos.getX(), pos.getY(), plotData.getName(), plotData.getIcon(), plotData.getNumber());
    }

    public static PlotButton createBarn(Map<Integer, Position> map) {
        var pos = map.get(0);
        return new PlotButton(pos.getX(), pos.getY(), "Barn", Items.SPRUCE_PLANKS, 0);
    }

    public static PlotButton createEmpty(Map<Integer, Position> map) {
        var pos = map.get(0);
        return new PlotButton(pos.getX(), pos.getY(), "UNKNOWN PLOT", Items.BARRIER, -1);
    }

    public static int[] getButtonRowCol(int buttonNumber) {
        // Center position is (2, 2) for button 0
        if (buttonNumber == -1) {
            return new int[]{-1, -1};
        }

        if (buttonNumber == 0) {
            return new int[]{2, 2};
        }


        int row = (buttonNumber - 1) / 5;
        int col = (buttonNumber - 1) % 5;

        // If the button number is greater than 12, we shift the row position
        if (buttonNumber > 12) {
            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
        }

        return new int[]{row, col};
    }

    public void render(DrawContext context) {
        if (!this.active) {
            return;
        }
        int offset = 2;
        context.drawItem(new ItemStack(itemIcon), x + offset, y + offset);  // Draw a diamond shape at the button's position
    }

    public ButtonWidget getButtonWidget() {
        return buttonWidget;
    }

    public int getNumber() {
        return number;
    }
}