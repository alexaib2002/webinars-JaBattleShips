package org.jabattleships;

import java.util.Random;
import java.util.Scanner;

import static org.jabattleships.MapUtils.*;

public class Main {

    public static final int[] Enemies = {4,};

    private static final char[][]
            GameMap = new char[SIZE][SIZE],
            EnemyMap = new char[SIZE][SIZE];
    private static boolean gameState = true;

    public static void main(String[] args) {
        initPlayerMap();
        initEnemyMap();
        Scanner sc = new Scanner(System.in);
        int[] selectedCords;

        while (gameState) {
            clearOutputBuffer();
            printMap(GameMap);
            selectedCords = promptNextTile(sc);
            try {
                if (checkEnemyHit(selectedCords[0], selectedCords[1]))
                    GameMap[selectedCords[0]][selectedCords[1]] = MAP_TILES[1]; // on enemy hit
                else
                    GameMap[selectedCords[0]][selectedCords[1]] = MAP_TILES[2]; // on enemy miss
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.printf("Has introducido unas coordenadas inexistentes en el mapa " +
                        "(escoge un numero entre 0 y %s)\nPresiona ENTER...", SIZE - 1);
                sc.nextLine();
            }
        }

        sc.close();
        System.out.println("Gracias por jugar!");
    }

    private static void clearOutputBuffer() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static int[] promptNextTile(Scanner sc) {
        int[] vec = new int[2];
        System.out.println("Introduce la casilla a la que deseas disparar: ");
        System.out.print("\tx = ");
        vec[0] = Integer.parseInt(sc.nextLine());
        System.out.print("\ty = ");
        vec[1] = Integer.parseInt(sc.nextLine());
        return (vec);
    }

    private static boolean checkEnemyHit(int x, int y) throws ArrayIndexOutOfBoundsException {
        return EnemyMap[x][y] == MAP_TILES[1];
    }

    private static void initPlayerMap() {
        iterMap(tile -> GameMap[tile[0]][tile[1]] = MAP_TILES[0]);
    }

    private static void initEnemyMap() {
        Random rnd = new Random();
        boolean invalid = true, orientation;
        int x = 0, y = 0;
        for (int enemy: Enemies) {
            orientation = rnd.nextBoolean(); // true: spans across x
            // generate its position
            while (invalid) {
                x = rnd.nextInt(SIZE - 1);
                y = rnd.nextInt(SIZE - 1);
                if (orientation) {
                    if (x + enemy >= SIZE)
                        continue;
                } else {
                    if (y + enemy >= SIZE)
                        continue;
                }
                invalid = false;
            }
            if (orientation)
                for (int i = 0; i < enemy; i++) {
                    EnemyMap[x + i][y] = MAP_TILES[1];
                }
            else
                for (int i = 0; i < enemy; i++) {
                    EnemyMap[x][y + i] = MAP_TILES[1];
                }
        }
    }

}
