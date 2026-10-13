package icu.kudikan.make_ars_lectern_great_again.utils;

public class Constants {
    public static final int VANILLA_ROW_COUNT = 3;
    public static final int MIN_ROW_COUNT = 3;
    public static final int ROW_HEIGHT = 18;
    public static final int EXPEND_EXTRA_ROWS = 4; // Rows gained when the crafting grid is collapsed
    public static final int SCREEN_MARGIN = 8;     // Vertical space kept free around the GUI

    // Runtime row count. Shared with the (common) menu mixin, so it must not depend on client classes.
    // Stays at the vanilla value on a dedicated server; the client screen updates it on every init().
    private static volatile int rowCount = VANILLA_ROW_COUNT;

    public static int rows() {
        return rowCount;
    }

    public static int expandedRows() {
        return rowCount + EXPEND_EXTRA_ROWS;
    }

    public static int extraRows() {
        return rowCount - VANILLA_ROW_COUNT;
    }

    public static void setRows(int rows) {
        rowCount = Math.max(MIN_ROW_COUNT, rows);
    }

    /**
     * @param screenHeight GUI-scaled screen height
     * @param imageHeight  height of the vanilla (3 rows) GUI
     * @param fixedRows    configured row count, 0 = adaptive
     * @param maxRows      cap for the adaptive mode, 0 = unlimited
     */
    public static int computeRows(int screenHeight, int imageHeight, int fixedRows, int maxRows) {
        if (fixedRows > 0) return Math.max(MIN_ROW_COUNT, fixedRows);
        int free = screenHeight - imageHeight - SCREEN_MARGIN * 2;
        int rows = VANILLA_ROW_COUNT + Math.floorDiv(free, ROW_HEIGHT);
        if (maxRows > 0) rows = Math.min(rows, maxRows);
        return Math.max(MIN_ROW_COUNT, rows);
    }
}
