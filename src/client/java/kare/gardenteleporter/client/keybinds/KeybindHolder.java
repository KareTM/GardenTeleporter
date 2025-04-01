package kare.gardenteleporter.client.keybinds;

import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class KeybindHolder {

    private final InputUtil.Type type = InputUtil.Type.KEYSYM;
    private final String translationKey;
    private final int defaultKeyCode;
    private int keyCode;

    public KeybindHolder(String translationKey, int keyCode) {
        this.translationKey = translationKey;
        this.keyCode = keyCode;
        this.defaultKeyCode = keyCode;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public boolean matchesKey(int keyCode, int scanCode) {
        if (keyCode == InputUtil.UNKNOWN_KEY.getCode()) {
            return this.keyCode == scanCode;
        } else {
            return this.keyCode == keyCode;
        }
    }

    public Text getBoundKeyLocalizedText() {
        return type.createFromCode(keyCode).getLocalizedText();
    }

    public int getDefaultKey() {
        return defaultKeyCode;
    }
}
