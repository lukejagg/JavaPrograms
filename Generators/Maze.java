package Generators;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Maze {

    static Random rnd = new Random();

    public static boolean[][] recursiveBacktracking(int width, int height, int startX, int startY) {
        boolean[][] cells = new boolean[width = width * 2 + 1][height = height * 2 + 1];
        ArrayList<Character> dir = new ArrayList<Character>();
        int currentX = startX * 2 + 1, currentY = startY * 2 + 1;
        cells[currentX][currentY] = true;
        ArrayList<Character> choice = new ArrayList<Character>();
        do {
            choice.clear();
            if (currentX + 2 < width && cells[currentX + 2][currentY] == false)
                choice.add('d');
            if (currentX - 2 > 0 && cells[currentX - 2][currentY] == false)
                choice.add('a');
            if (currentY + 2 < height && cells[currentX][currentY + 2] == false)
                choice.add('w');
            if (currentY - 2 > 0 && cells[currentX][currentY - 2] == false)
                choice.add('s');
            if (choice.size() > 0) {
                int c = rnd.nextInt(choice.size());
                switch (choice.get(c)) {
                    case 'w':
                        cells[currentX][++currentY] = true;
                        cells[currentX][++currentY] = true;
                        break;
                    case 's':
                        cells[currentX][--currentY] = true;
                        cells[currentX][--currentY] = true;
                        break;
                    case 'd':
                        cells[++currentX][currentY] = true;
                        cells[++currentX][currentY] = true;
                        break;
                    case 'a':
                        cells[--currentX][currentY] = true;
                        cells[--currentX][currentY] = true;
                        break;
                }
                dir.add(choice.get(c));
            }
            else {
                if (dir.size() > 0) {
                    switch (dir.get(dir.size() - 1)) {
                        case 'w' :
                            currentY -= 2;
                            break;
                        case 's' :
                            currentY += 2;
                            break;
                        case 'd' :
                            currentX -= 2;
                            break;
                        case 'a' :
                            currentX += 2;
                            break;
                    }
                    dir.remove(dir.size() - 1);
                }
            }
        } while (dir.size() > 0 || choice.size() > 0);
        return cells;
    }

    public static boolean[][][] recursiveBacktracking(int width, int height, int depth, int startX, int startY, int startZ) {
        boolean[][][] cells = new boolean[width = width * 2 + 1][height = height * 2 + 1][depth = depth * 2 + 1];
        ArrayList<Character> dir = new ArrayList<Character>();
        int currentX = startX * 2 + 1;
        int currentY = startY * 2 + 1;
        int currentZ = startZ * 2 + 1;
        cells[currentX][currentY][currentZ] = true;
        ArrayList<Character> choice = new ArrayList<Character>();
        do {
            choice.clear();
            if (currentX + 2 < width && cells[currentX + 2][currentY][currentZ] == false) {
                choice.add('d');
            }
            if (currentX - 2 > 0 && cells[currentX - 2][currentY][currentZ] == false) {
                choice.add('a');
            }
            if (currentY + 2 < height && cells[currentX][currentY + 2][currentZ] == false) {
                choice.add('w');
            }
            if (currentY - 2 > 0 && cells[currentX][currentY - 2][currentZ] == false) {
                choice.add('s');
            }
            if (currentZ + 2 < depth && cells[currentX][currentY][currentZ + 2] == false) {
                choice.add('e');
            }
            if (currentZ - 2 > 0 && cells[currentX][currentY][currentZ - 2] == false) {
                choice.add('q');
            }
            if (choice.size() > 0) {
                int c = rnd.nextInt(choice.size());
                switch (choice.get(c)) {
                    case 'w':
                        cells[currentX][++currentY][currentZ] = true;
                        cells[currentX][++currentY][currentZ] = true;
                        break;
                    case 's':
                        cells[currentX][--currentY][currentZ] = true;
                        cells[currentX][--currentY][currentZ] = true;
                        break;
                    case 'd':
                        cells[++currentX][currentY][currentZ] = true;
                        cells[++currentX][currentY][currentZ] = true;
                        break;
                    case 'a':
                        cells[--currentX][currentY][currentZ] = true;
                        cells[--currentX][currentY][currentZ] = true;
                        break;
                    case 'e':
                        cells[currentX][currentY][++currentZ] = true;
                        cells[currentX][currentY][++currentZ] = true;
                        break;
                    case 'q':
                        cells[currentX][currentY][--currentZ] = true;
                        cells[currentX][currentY][--currentZ] = true;
                        break;
                }
                dir.add(choice.get(c));
            }
            else {
                if (dir.size() > 0) {
                    switch (dir.get(dir.size() - 1)) {
                        case 'w' :
                            currentY -= 2;
                            break;
                        case 's' :
                            currentY += 2;
                            break;
                        case 'd' :
                            currentX -= 2;
                            break;
                        case 'a' :
                            currentX += 2;
                            break;
                        case 'e' :
                            currentZ -= 2;
                            break;
                        case 'q' :
                            currentZ += 2;
                            break;                    }
                    dir.remove(dir.size() - 1);
                }
            }
        } while (dir.size() > 0 || choice.size() > 0);
        return cells;
    }

    public static boolean[][][] recursiveBacktrackingString(int width, int height, int depth, int startX, int startY, int startZ)
    {
        boolean[][][] cells = new boolean[width = width * 2 + 1][height = height * 2 + 1][depth = depth * 2 + 1];
        String dir = "";
        int currentX = startX * 2 + 1;
        int currentY = startY * 2 + 1;
        int currentZ = startZ * 2 + 1;
        cells[currentX][currentY][currentZ] = true;
        String choice = "";
        do
        {
            choice = "";
            if (currentX + 2 < width && cells[currentX + 2][currentY][currentZ] == false)
            {
                choice += "d";
            }
            if (currentX - 2 > 0 && cells[currentX - 2][currentY][currentZ] == false)
            {
                choice += "a";
            }
            if (currentY + 2 < height && cells[currentX][currentY + 2][currentZ] == false)
            {
                choice += "w";
            }
            if (currentY - 2 > 0 && cells[currentX][currentY - 2][currentZ] == false)
            {
                choice += "s";
            }
            if (currentZ + 2 < depth && cells[currentX][currentY][currentZ + 2] == false)
            {
                choice += "e";
            }
            if (currentZ - 2 > 0 && cells[currentX][currentY][currentZ - 2] == false)
            {
                choice += "q";
            }
            if (choice.length() > 0)
            {
                int c = rnd.nextInt(choice.length());
                switch (choice.substring(c, 1))
                {
                    case "w":
                        cells[currentX][++currentY][currentZ] = true;
                        cells[currentX][++currentY][currentZ] = true;
                        break;
                    case "s":
                        cells[currentX][--currentY][currentZ] = true;
                        cells[currentX][--currentY][currentZ] = true;
                        break;
                    case "d":
                        cells[++currentX][currentY][currentZ] = true;
                        cells[++currentX][currentY][currentZ] = true;
                        break;
                    case "a":
                        cells[--currentX][currentY][currentZ] = true;
                        cells[--currentX][currentY][currentZ] = true;
                        break;
                    case "e":
                        cells[currentX][currentY][++currentZ] = true;
                        cells[currentX][currentY][++currentZ] = true;
                        break;
                    case "q":
                        cells[currentX][currentY][--currentZ] = true;
                        cells[currentX][currentY][--currentZ] = true;
                        break;
                }
                dir += choice.substring(c, 1);
            }
            else
            {
                if (dir.length() > 0)
                {
                    switch (dir.substring(dir.length() - 1, 1))
                    {
                        case "w":
                            currentY -= 2;
                            break;
                        case "s":
                            currentY += 2;
                            break;
                        case "d":
                            currentX -= 2;
                            break;
                        case "a":
                            currentX += 2;
                            break;
                        case "e":
                            currentZ -= 2;
                            break;
                        case "q":
                            currentZ += 2;
                            break;
                    }
                    dir = dir.substring(0, dir.length() - 1);
                }
            }
        } while (dir.length() > 0 || choice.length() > 0);
        return cells;
    }

    public static boolean[][][][] recursiveBacktracking(int width, int height, int depth, int time, int startX, int startY, int startZ, int startW) {
        boolean[][][][] cells = new boolean[width = width * 2 + 1][height = height * 2 + 1][depth = depth * 2 + 1][time = time * 2 + 1];
        ArrayList<Character> dir = new ArrayList<Character>();
        int currentX = startX * 2 + 1;
        int currentY = startY * 2 + 1;
        int currentZ = startZ * 2 + 1;
        int currentW = startW * 2 + 1;
        cells[currentX][currentY][currentZ][currentW] = true;
        ArrayList<Character> choice = new ArrayList<Character>();
        do {
            choice.clear();
            if (currentX + 2 < width && cells[currentX + 2][currentY][currentZ][currentW] == false) {
                choice.add('d');
            }
            if (currentX - 2 > 0 && cells[currentX - 2][currentY][currentZ][currentW] == false) {
                choice.add('a');
            }
            if (currentY + 2 < height && cells[currentX][currentY + 2][currentZ][currentW] == false) {
                choice.add('w');
            }
            if (currentY - 2 > 0 && cells[currentX][currentY - 2][currentZ][currentW] == false) {
                choice.add('s');
            }
            if (currentZ + 2 < depth && cells[currentX][currentY][currentZ + 2][currentW] == false) {
                choice.add('e');
            }
            if (currentZ - 2 > 0 && cells[currentX][currentY][currentZ - 2][currentW] == false) {
                choice.add('q');
            }
            if (currentW + 2 < time && cells[currentX][currentY][currentZ][currentW + 2] == false) {
                choice.add('i');
            }
            if (currentW - 2 > 0 && cells[currentX][currentY][currentZ][currentW - 2] == false) {
                choice.add('k');
            }
            if (choice.size() > 0) {
                int c = rnd.nextInt(choice.size());
                switch (choice.get(c)) {
                    case 'w':
                        cells[currentX][++currentY][currentZ][currentW] = true;
                        cells[currentX][++currentY][currentZ][currentW] = true;
                        break;
                    case 's':
                        cells[currentX][--currentY][currentZ][currentW] = true;
                        cells[currentX][--currentY][currentZ][currentW] = true;
                        break;
                    case 'd':
                        cells[++currentX][currentY][currentZ][currentW] = true;
                        cells[++currentX][currentY][currentZ][currentW] = true;
                        break;
                    case 'a':
                        cells[--currentX][currentY][currentZ][currentW] = true;
                        cells[--currentX][currentY][currentZ][currentW] = true;
                        break;
                    case 'e':
                        cells[currentX][currentY][++currentZ][currentW] = true;
                        cells[currentX][currentY][++currentZ][currentW] = true;
                        break;
                    case 'q':
                        cells[currentX][currentY][--currentZ][currentW] = true;
                        cells[currentX][currentY][--currentZ][currentW] = true;
                        break;
                    case 'i':
                        cells[currentX][currentY][currentZ][++currentW] = true;
                        cells[currentX][currentY][currentZ][++currentW] = true;
                        break;
                    case 'k':
                        cells[currentX][currentY][currentZ][--currentW] = true;
                        cells[currentX][currentY][currentZ][--currentW] = true;
                        break;
                }
                dir.add(choice.get(c));
            }
            else {
                if (dir.size() > 0) {
                    switch (dir.get(dir.size() - 1)) {
                        case 'w' :
                            currentY -= 2;
                            break;
                        case 's' :
                            currentY += 2;
                            break;
                        case 'd' :
                            currentX -= 2;
                            break;
                        case 'a' :
                            currentX += 2;
                            break;
                        case 'e' :
                            currentZ -= 2;
                            break;
                        case 'q' :
                            currentZ += 2;
                            break;
                        case 'i' :
                            currentW -= 2;
                            break;
                        case 'k' :
                            currentW += 2;
                            break;         }
                    dir.remove(dir.size() - 1);
                }
            }
        } while (dir.size() > 0 || choice.size() > 0);
        return cells;
    }

    public static boolean[][] wallTracking(int width, int height, int startX, int startY, double sameProbability) {
        class cell {
            int x;
            int y;
            ArrayList<Character> dir = new ArrayList<Character>();
            cell (int x, int y, ArrayList<Character> dir) {
                this.x = x;
                this.y = y;
                this.dir = dir;
            }
        }
        boolean[][] cells = new boolean[width = width * 2 + 1][height = height * 2 + 1];
//        ArrayList<Character> possible = new ArrayList<Character>();
        ArrayList<cell> possibleCells = new ArrayList<cell>();
        ArrayList<Character> pFirst = new ArrayList<Character>();
        possibleCells.add(new cell(startX, startY, new ArrayList<Character>()));
        int index = 0;
        int x = 0, y = 0;
        do {
//            ArrayList<Integer> remove = new ArrayList<Integer>();
            index = -1;
            for (int i = 0; i < possibleCells.size(); i++) {
                cell c = possibleCells.get(i);
                c.dir.clear();
                if (c.x * 2 + 3 < width && cells[c.x * 2 + 3][c.y * 2 + 1] == false)
                    c.dir.add('d');
                if (c.x * 2 - 1 > 0 && cells[c.x * 2 - 1][c.y * 2 + 1] == false)
                    c.dir.add('a');
                if (c.y * 2 + 3 < height && cells[c.x * 2 + 1][c.y * 2 + 3] == false)
                    c.dir.add('w');
                if (c.y * 2 - 1 > 0 && cells[c.x * 2 + 1][c.y * 2 - 1] == false)
                    c.dir.add('s');
                if (c.dir.size() == 0)
                    possibleCells.remove(i--);
                else {
                    if (c.x == x && c.y == y) {
                        index = i;
                    }
                }
            }
            if (possibleCells.size() > 0) {
                int r = (rnd.nextDouble() < sameProbability ? index : rnd.nextInt(possibleCells.size()));
                if (r == -1)
                    r = rnd.nextInt(possibleCells.size());
                cell c = possibleCells.get(r);
                r = rnd.nextInt(c.dir.size());
                switch (c.dir.get(r)) {
                    case 'd':
                        cells[c.x * 2 + 2][c.y * 2 + 1] = true;
                        cells[c.x * 2 + 3][c.y * 2 + 1] = true;
                        possibleCells.add(new cell(c.x + 1, c.y, new ArrayList<Character>()));
                        x = c.x + 1;
                        y = c.y;
                        break;
                    case 'a':
                        cells[c.x * 2][c.y * 2 + 1] = true;
                        cells[c.x * 2 - 1][c.y * 2 + 1] = true;
                        possibleCells.add(new cell(c.x - 1, c.y, new ArrayList<Character>()));
                        x = c.x - 1;
                        y = c.y;
                        break;
                    case 'w':
                        cells[c.x * 2 + 1][c.y * 2 + 2] = true;
                        cells[c.x * 2 + 1][c.y * 2 + 3] = true;
                        possibleCells.add(new cell(c.x, c.y + 1, new ArrayList<Character>()));
                        x = c.x;
                        y = c.y + 1;
                        break;
                    case 's':
                        cells[c.x * 2 + 1][c.y * 2] = true;
                        cells[c.x * 2 + 1][c.y * 2 - 1] = true;
                        possibleCells.add(new cell(c.x, c.y - 1, new ArrayList<Character>()));
                        x = c.x;
                        y = c.y - 1;
                        break;
                }
            }
        } while (possibleCells.size() > 0);
        return cells;
    }

}
