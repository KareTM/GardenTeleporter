package kare.gardenteleporter.client.PlotData;

import net.minecraft.item.Item;

public class PlotData {
    private final String name;
    private final Item icon;
    private final int number;

    public PlotData(String name, Item icon, int number) {
        this.name = name;
        this.icon = icon;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public Item getIcon() {
        return icon;
    }

    public int getNumber() {
        return number;
    }
}
