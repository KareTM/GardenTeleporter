package kare.gardenteleporter.client.UI.util;

import kare.gardenteleporter.client.UI.PlotButton;

import java.util.HashMap;
import java.util.Map;

public class ButtonPositionMapper {
    public static final int BTN_SZ = PlotButton.buttonSize;  // Adjust this based on your button size

    public static Map<Integer, Position> createButtonPositionMap(int width, int height) {
        Map<Integer, Position> positionMap = new HashMap<>();

        // Center of the screen
        int centerX = width / 2 - 15;
        int centerY = height / 2 - 15;
        int offset = BTN_SZ * 3 / 2;

        // Map button numbers to their positions
        positionMap.put(1, new Position(centerX - offset * 2, centerY - offset * 2));
        positionMap.put(2, new Position(centerX - offset, centerY - offset * 2));
        positionMap.put(3, new Position(centerX, centerY - offset * 2));
        positionMap.put(4, new Position(centerX + offset, centerY - offset * 2));
        positionMap.put(5, new Position(centerX + offset * 2, centerY - offset * 2));

        positionMap.put(6, new Position(centerX - offset * 2, centerY - offset));
        positionMap.put(7, new Position(centerX - offset, centerY - offset));
        positionMap.put(8, new Position(centerX, centerY - offset));
        positionMap.put(9, new Position(centerX + offset, centerY - offset));
        positionMap.put(10, new Position(centerX + offset * 2, centerY - offset));

        positionMap.put(11, new Position(centerX - offset * 2, centerY));
        positionMap.put(12, new Position(centerX - offset, centerY));
        positionMap.put(0, new Position(centerX, centerY));  // Barn button (position unchanged)
        positionMap.put(13, new Position(centerX + offset, centerY));
        positionMap.put(14, new Position(centerX + offset * 2, centerY));

        positionMap.put(15, new Position(centerX - offset * 2, centerY + offset));
        positionMap.put(16, new Position(centerX - offset, centerY + offset));
        positionMap.put(17, new Position(centerX, centerY + offset));
        positionMap.put(18, new Position(centerX + offset, centerY + offset));
        positionMap.put(19, new Position(centerX + offset * 2, centerY + offset));

        positionMap.put(20, new Position(centerX - offset * 2, centerY + offset * 2));
        positionMap.put(21, new Position(centerX - offset, centerY + offset * 2));
        positionMap.put(22, new Position(centerX, centerY + offset * 2));
        positionMap.put(23, new Position(centerX + offset, centerY + offset * 2));
        positionMap.put(24, new Position(centerX + offset * 2, centerY + offset * 2));

        return positionMap;
    }
}
