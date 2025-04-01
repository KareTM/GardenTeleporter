package kare.gardenteleporter.client.UI;

import kare.gardenteleporter.client.PlotData.PlotData;
import kare.gardenteleporter.client.UI.util.ButtonPositionMapper;
import kare.gardenteleporter.client.keybinds.KeybindHolder;
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
    private final List<KeybindHolder> keyBindings;
    private boolean selectedRow;
    private int selectedRowIndex;
    // List to store buttons
    List<PlotButton> buttons;

    public PlotScreen(List<PlotData> plotDataList, List<KeybindHolder> keyBindings) {
        super(Text.literal("Garden Plot Teleport"));
        this.plotDataList = plotDataList;
        this.keyBindings = keyBindings;
        buttons = new ArrayList<>(); // Initialize the list
        selectedRow = false;
    }

    @Override
    protected void init() {
        super.init();

        setPlotData(plotDataList);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (KeybindHolder key : keyBindings) {
            if (key.matchesKey(keyCode, scanCode)) {
                selectRowColumn(keyBindings.indexOf(key));
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        buttons.forEach(button -> button.render(context));

        // Center of the screen
        int centerX = width / 2 - 15;
        int centerY = height / 2 - 15;
        int offset = ButtonPositionMapper.BTN_SZ * 3 / 2;

        if (selectedRow) {
            drawColumnTexts(context, centerX, centerY, offset);
        } else {
            drawRowTexts(context, centerX, centerY, offset);
        }
    }

    private void drawRowTexts(DrawContext context, int centerX, int centerY, int offset) {
        // Text for each row
        for (int row = 0; row < 5; row++) {
            var newoffset = offset * (row - 2);

            Text label = keyBindings.get(row).getBoundKeyLocalizedText();
            int xPosition = centerX - offset * 2 - 5; // 5 pixels to the left of the button
            int yPosition = centerY + newoffset + 5; // 10 pixels above the topmost button

            // Draw text
            context.drawCenteredTextWithShadow(this.textRenderer, label, xPosition, yPosition, 0xFFFFFF);
        }
    }

    private void drawColumnTexts(DrawContext context, int centerX, int centerY, int offset) {
        // Text for each column
        for (int col = 0; col < 5; col++) {
            var newoffset = offset * (col - 2);

            Text label = keyBindings.get(col).getBoundKeyLocalizedText();
            int xPosition = centerX + newoffset + 10; // Centered
            int yPosition = centerY - offset * 2 + selectedRowIndex * offset - 10; // 10 pixels above top button

            // Draw text
            context.drawCenteredTextWithShadow(this.textRenderer, label, xPosition, yPosition, 0xFFFFFF);
        }
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

    public void selectRowColumn(int i) {
        System.out.println(i);
        if (selectedRow) {
            selectColumn(i);
        } else {
            selectRow(i);
        }
    }

    private void selectRow(int i) {
        for (PlotButton button : buttons) {
            var rc = PlotButton.getButtonRowCol(button.getNumber());
            if (rc[0] == -1) {
                return;
            }
            if (rc[0] != i) {
                button.active = false;
                remove(button.getButtonWidget());
            }
        }

        selectedRow = true;
        selectedRowIndex = i;
    }

    private void selectColumn(int i) {
        for (PlotButton button : buttons) {
            var rc = PlotButton.getButtonRowCol(button.getNumber());

            if (rc[0] == selectedRowIndex && rc[1] == i) {
                button.getButtonWidget().onPress();
                close();
            }
        }

        selectedRow = false;
    }
}
