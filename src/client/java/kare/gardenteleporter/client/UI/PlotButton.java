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
    private final ButtonWidget buttonWidget;

    public PlotButton(int x, int y, String name, Item itemIcon
    ) {
        this.x = x;
        this.y = y;
        this.itemIcon = itemIcon;

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
                })
                .dimensions(x, y, buttonSize, buttonSize)  // Button width and height
                .tooltip(tt)
                .build();
    }

    public static PlotButton fromPlotData(PlotData plotData, Map<Integer, Position> map) {
        var pos = map.get(plotData.getNumber());
        return new PlotButton(pos.getX(), pos.getY(), plotData.getName(), plotData.getIcon());
    }

    public static PlotButton createBarn(Map<Integer, Position> map) {
        var pos = map.get(0);
        return new PlotButton(pos.getX(), pos.getY(), "Barn", Items.SPRUCE_PLANKS);
    }

    public static PlotButton createEmpty(Map<Integer, Position> map) {
        var pos = map.get(0);
        return new PlotButton(pos.getX(), pos.getY(), "UNKNOWN PLOT", Items.BARRIER);
    }

    public void render(DrawContext context) {
        int offset = 2;
        context.drawItem(new ItemStack(itemIcon), x + offset, y + offset);  // Draw a diamond shape at the button's position
    }

    public ButtonWidget getButtonWidget() {
        return buttonWidget;
    }
}