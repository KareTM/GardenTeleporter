package kare.gardenteleporter.client.UI;

import kare.gardenteleporter.client.KeybindStorage;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GardenKeybindsScreen extends Screen {
    private static final Text TITLE_TEXT = Text.translatable("controls.gardenteleporter.title");
    private final List<KeyBinding> keybinds;
    private final Map<KeyBinding, ButtonWidget> buttons = new HashMap<>();
    @Nullable
    private KeyBinding selectedKeyBinding;

    public GardenKeybindsScreen(List<KeyBinding> keybinds) {
        super(TITLE_TEXT);
        this.keybinds = keybinds;
    }

    @Override
    protected void init() {
        int y = 40; // Starting Y position for buttons
        for (KeyBinding keyBinding : keybinds) {
            buttons.put(keyBinding, createButton(keyBinding, y));
            y += 24;
        }

        y += 24;

        // Reset all button
        ButtonWidget resetAllButton = ButtonWidget.builder(Text.translatable("controls.resetAll"), button -> {
            for (KeyBinding keyBinding : keybinds) {
                keyBinding.setBoundKey(keyBinding.getDefaultKey());

                var but = buttons.get(keyBinding);
                var ybut = but.getY();
                remove(but);

                buttons.put(keyBinding, createButton(keyBinding, ybut));
            }
        }).dimensions(this.width / 2 - 100, y, 200, 20).build();
        this.addDrawableChild(resetAllButton);
        y += 24;

        // Done button
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close())
                .dimensions(this.width / 2 - 100, y, 200, 20).build());
    }

    private ButtonWidget createButton(KeyBinding keyBinding, int y) {


        var but = ButtonWidget.builder(
                Text.translatable(keyBinding.getTranslationKey()).append(": ").append(keyBinding.getBoundKeyLocalizedText()),
                button -> this.selectedKeyBinding = keyBinding
        ).dimensions(this.width / 2 - 100, y, 200, 20).build();
        this.addDrawableChild(but);

        return but;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.selectedKeyBinding != null) {
            var but = buttons.get(this.selectedKeyBinding);
            var y = but.getY();

            remove(but);

            if (keyCode == 256) { // Escape key clears the keybind
                this.selectedKeyBinding.setBoundKey(InputUtil.UNKNOWN_KEY);
            } else {
                this.selectedKeyBinding.setBoundKey(InputUtil.fromKeyCode(keyCode, scanCode));
            }

            buttons.put(this.selectedKeyBinding, createButton(this.selectedKeyBinding, y));
            this.selectedKeyBinding = null;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public void close() {
        KeybindStorage.saveKeybinds(this.keybinds);
        super.close();
    }
}

