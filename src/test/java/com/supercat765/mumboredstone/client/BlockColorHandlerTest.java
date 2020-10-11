package com.supercat765.mumboredstone.client;

import org.junit.Test;

import static com.supercat765.mumboredstone.client.BlockColorHandler.WHITE;
import static com.supercat765.mumboredstone.client.BlockColorHandler.getBluestoneWireColorForPowerLevel;
import static net.minecraft.util.ColorHelper.PackedColor.*;
import static org.junit.Assert.assertEquals;

public class BlockColorHandlerTest {

    public static final int BLACK = 0xFF_00_00_00;

    @Test
    public void testGetBluestoneWireColorForPowerLevel() {
        assertColorEquals(BLACK, getBluestoneWireColorForPowerLevel(0, BLACK, WHITE));
        assertColorEquals(WHITE, getBluestoneWireColorForPowerLevel(0, WHITE, WHITE));
        assertColorEquals(WHITE, getBluestoneWireColorForPowerLevel(0, WHITE, BLACK));
        assertColorEquals(WHITE, getBluestoneWireColorForPowerLevel(15, BLACK, WHITE));
        assertColorEquals(BLACK, getBluestoneWireColorForPowerLevel(15, WHITE, BLACK));
        int green = 0xFF_00_FF_00;
        int blue = 0xFF_00_00_FF;
        int color = getBluestoneWireColorForPowerLevel(8, green, blue);
        assertEquals(255, getAlpha(color));
        assertEquals(0, getRed(color));
        // 8 is not exactly half of 15, it's 53% of 15, so there's a tiny bit of leeway here
        assertEquals(255 / 2F, getGreen(color), 10);
        assertEquals(255 / 2F, getBlue(color), 10);
    }

    private void assertColorEquals(int expected, int actual) {
        try {
            assertEquals(expected, actual);
        } catch (AssertionError e) {
            String expectedString = colorToRGBAString(expected);
            String actualString = colorToRGBAString(actual);
            String message = "expected:<" + expectedString + "> but was:<" + actualString + ">";
            throw new AssertionError(message, e);
        }
    }

    private String colorToRGBAString(int expected) {
        return "(" + getAlpha(expected) + ", " + getRed(expected) + ", " + getGreen(expected) + ", " + getBlue(expected) + ")";
    }

}
