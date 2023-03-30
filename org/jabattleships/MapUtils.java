package org.jabattleships;

import java.util.function.Consumer;

public class MapUtils {
    public static final int SIZE = 10;
    public static final char[] MAP_TILES = {'x', '*', '-',};

    public static void iterMap(Consumer<int[]> foreachCell) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                foreachCell.accept(new int[]{col, row});
            }
        }
    }

    public static void iterMap(Consumer<int[]> foreachCell, Runnable foreachRow) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                foreachCell.accept(new int[]{col, row});
            }
            foreachRow.run();
        }
    }

    public static void printMap(char[][] map) {
        iterMap((tile) -> System.out.print(map[tile[0]][tile[1]]), System.out::println);
    }
}
