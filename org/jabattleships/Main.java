package org.jabattleships;

import java.util.Random;
import java.util.Scanner;

import static org.jabattleships.MapUtils.*;

public class Main {

    public static final int[] Boats = {4, 3, 3, 1};

    private static final char[][]
            GameMap = new char[SIZE][SIZE],
            EnemyMap = new char[SIZE][SIZE],
            PlayerMap = new char[SIZE][SIZE];
    private static boolean gameState = true;
    private static final int[] points = new int[2];

    private static int endPoints;

    public static void main(String[] args) {
        initGameplayMap();
        initUserMap(EnemyMap);
        initUserMap(PlayerMap);
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < Boats.length; i++)
            endPoints += Boats[i];

        while (gameState) {
            clearOutputBuffer();
            // print maps
            System.out.println(" ==== Mapa del enemigo ==== ");
            printMap(GameMap);
            System.out.println(" ==== Mis barcos ==== ");
            printMap(PlayerMap);
            System.out.printf("Mis puntos - %s || CPU - %s\n", points[0], points[1]);
            // prompt user for shooting
            onUserTurn(sc);
            onCpuTurn();
        }

        sc.close();
        System.out.println("Gracias por jugar!");
    }

    private static void onUserTurn(Scanner sc) {
        int[] selectedCords;
        selectedCords = promptNextTile(sc);
        try {
            if (checkMapHit(selectedCords[0], selectedCords[1])) {
                GameMap[selectedCords[0]][selectedCords[1]] = MAP_TILES[1]; // on enemy hit
                points[0]++; // sum points to player
            }
            else
                GameMap[selectedCords[0]][selectedCords[1]] = MAP_TILES[2]; // on enemy miss
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.printf("Has introducido unas coordenadas inexistentes en el mapa " +
                    "(escoge un numero entre 0 y %s)\nPresiona ENTER...", SIZE - 1);
            sc.nextLine();
        }
        checkGameEnd(points[0]);
    }

    private static void onCpuTurn() {
        Random rnd = new Random();
        if (checkMapHit(rnd.nextInt(SIZE - 9), SIZE - 9))
            points[1]++; // sum points to enemy
        checkGameEnd(points[1]);
    }

    private static void checkGameEnd(int userPoints) {
        if (userPoints == endPoints)
            gameState = false;
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

    private static boolean checkMapHit(int x, int y) throws ArrayIndexOutOfBoundsException {
        return EnemyMap[x][y] == MAP_TILES[1];
    }

    private static void initGameplayMap() {
        iterMap(tile -> GameMap[tile[0]][tile[1]] = MAP_TILES[0]);
    }

    private static void initUserMap(char[][] map) {
        Random rnd = new Random();
        boolean invalid, orientation;
        int x = 0, y = 0;
        for (int enemy: Boats) {
            orientation = rnd.nextBoolean(); // true: spans across x
            invalid = true;
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
                    map[x + i][y] = MAP_TILES[1];
                }
            else
                for (int i = 0; i < enemy; i++) {
                    map[x][y + i] = MAP_TILES[1];
                }
        }
    }

}
