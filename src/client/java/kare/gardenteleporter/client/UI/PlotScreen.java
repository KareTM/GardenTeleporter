package kare.gardenteleporter.client.UI;

import kare.gardenteleporter.client.PlotData.PlotData;
import kare.gardenteleporter.client.UI.util.ButtonPositionMapper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class PlotScreen extends Screen {
    private final List<PlotData> plotDataList;
    // List to store buttons
    List<PlotButton> buttons;

    public PlotScreen(List<PlotData> plotDataList) {
        super(Text.literal("Garden Plot Teleport"));
        this.plotDataList = plotDataList;
        buttons = new ArrayList<>(); // Initialize the list
    }

    @Override
    protected void init() {
        super.init();

        setPlotData(plotDataList);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        buttons.forEach(button -> button.render(context));
    }

    public void setPlotData(List<PlotData> plotDataList) {
        var map = ButtonPositionMapper.createButtonPositionMap(width, height);
        buttons.clear();
        if (plotDataList.isEmpty()) {
            var empty = PlotButton.createEmpty(map);
            buttons.add(empty);
            addDrawableChild(empty.getButtonWidget());

            return;
        }

        for (PlotData plotData : plotDataList) {
            var but = PlotButton.fromPlotData(plotData, map);
            buttons.add(but);
            addDrawableChild(but.getButtonWidget());
        }

        var barn = PlotButton.createBarn(map);
        buttons.add(barn);
        addDrawableChild(barn.getButtonWidget());
    }
}
