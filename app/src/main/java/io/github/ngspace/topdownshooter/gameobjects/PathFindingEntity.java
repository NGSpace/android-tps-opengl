package io.github.ngspace.topdownshooter.gameobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.ngspace.topdownshooter.renderer.renderer.TextureInfo;
import io.github.ngspace.topdownshooter.utils.Logcat;

public class PathFindingEntity extends Entity {
    public double tick = 0;
    public Node targetpos;
    public Node prevpos;
    private List<int[]> pathList = null;
    public PathFindingEntity(TextureInfo texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height);
    }

    //TODO FIX THIS SHIT
    public void setTarget(int[][] grid, float destx, float desty) {
        int dx = (int) destx/100;
        int dy = (int) desty/100;
        StringBuilder strw = new StringBuilder("[");
        for (int[] ints : grid) {
            strw.append(Arrays.toString(ints));
            strw.append(",\n");
        }
        strw.append(']');
        Logcat.log(strw.toString(), (int) (getX()/100), (int) (getY()/100));
        pathList = aStarSearch(grid, (int) (getX()/100), (int) (getY()/100), dx, dy, 19, 19);
        StringBuilder str = new StringBuilder();
        pathList.forEach(p -> {
            if (p[0] == 2 || p[0] == 1) str.append("-> (").append(p[0]).append(", ").append(p[1]).append(")");
            else str.append("-> (").append(p[0]).append(", ").append(p[1]).append(")");
        });
        Logcat.log(getX(), getY());
        Logcat.log(getX()/100, getY()/100, str);
        prevpos = new Node((int) getX(), (int) getY());

    }

    public Node getNext() {
        if (pathList==null||pathList.isEmpty()) {targetpos=prevpos;return null;}
        int[] nextpos = pathList.get(pathList.size()-1);
        pathList.remove(pathList.size()-1);
        return new Node(nextpos[0]*100, nextpos[1]*100);
    }

    public static class Node {
        public int x;
        public int y;

        public Node(int nx, int ny) {x=nx;y=ny;}
    }

    static class Cell {
        int parent_i;
        int parent_j;
        double f;
        double g;
        double h;
    }

    private static boolean isValid(int row, int col, int LEN_VERTICAL, int LEN_HOR) {
        return (row >= 0) && (row < LEN_VERTICAL) && (col >= 0) && (col < LEN_HOR);
    }

    private static boolean isUnBlocked(int[][] grid, int row, int col) {
        return grid[row][col] == 0;
    }

    private static boolean isDestination(int row, int col, int[] dest) {
        return row == dest[0] && col == dest[1];
    }

    private static double calculateHValue(int row, int col, int[] dest){
        return Math.sqrt((row - dest[0]) * (row - dest[0])  + (col - dest[1]) * (col - dest[1]));
    }

    private List<int[]> tracePath(Cell[][] cellDetails, int[] dest) {
        int row = dest[0];
        int col = dest[1];

        Map<int[], Boolean> path = new LinkedHashMap<>();

        while (!(cellDetails[row][col].parent_i == row && cellDetails[row][col].parent_j == col)) {
            path.put(new int[] { row, col }, true);
            int temp_row = cellDetails[row][col].parent_i;
            int temp_col = cellDetails[row][col].parent_j;
            row = temp_row;
            col = temp_col;
        }

//        if (row*100!=getX()||col*100!=getY())
//            path.put(new int[] { row, col }, true);
//        List<int[]> pathList = new ArrayList<>(path.keySet());
//        Collections.reverse(pathList);

        return new ArrayList<>(path.keySet());
    }

    private List<int[]> aStarSearch(int[][] grid, int x, int y, int destx, int desty, int LEN_VERTICAL, int LEN_HOR) {

        int[] dest = {desty, destx};

        boolean[][] closedList = new boolean[LEN_VERTICAL][LEN_HOR];
        Cell[][] cellDetails = new Cell[LEN_VERTICAL][LEN_HOR];

        for (int i = 0; i < LEN_VERTICAL; i++) {
            for (int j = 0; j < LEN_HOR; j++) {
                cellDetails[i][j] = new Cell();
                cellDetails[i][j].f = Double.POSITIVE_INFINITY;
                cellDetails[i][j].g  = Double.POSITIVE_INFINITY;
                cellDetails[i][j].h = Double.POSITIVE_INFINITY;
                cellDetails[i][j].parent_i = -1;
                cellDetails[i][j].parent_j = -1;
            }
        }

        int i = y;
        int j = x;
        cellDetails[i][j].f = 0;
        cellDetails[i][j].g = 0;
        cellDetails[i][j].h = 0;
        cellDetails[i][j].parent_i = i;
        cellDetails[i][j].parent_j = j;

        Map<Double, int[]> openList = new HashMap<>();
        openList.put(0.0, new int[] { i, j });

        while (!openList.isEmpty()) {
            Map.Entry<Double, int[]> p = openList.entrySet().iterator().next();
            for (Map.Entry<Double, int[]> q : openList.entrySet()) {
                if (q.getKey() < p.getKey()) {
                    p = q;
                }
            }

            openList.remove(p.getKey());

            i = p.getValue()[0];
            j = p.getValue()[1];
            closedList[i][j] = true;

            double gNew;
            double hNew;
            double fNew;

            // 1st Successor (North)
            if (isValid(i - 1, j, LEN_VERTICAL, LEN_HOR)) {
                if (isDestination(i - 1, j, dest)) {
                    cellDetails[i - 1][j].parent_i = i;
                    cellDetails[i - 1][j].parent_j = j;
                    return tracePath(cellDetails, dest);
                }
                else if (!closedList[i - 1][j] && isUnBlocked(grid, i - 1, j)) {
                    gNew = cellDetails[i][j].g + 1;
                    hNew = calculateHValue(i - 1, j, dest);
                    fNew = gNew + hNew;
                    if (cellDetails[i - 1][j].f == Double.POSITIVE_INFINITY || cellDetails[i - 1][j].f > fNew) {
                        openList.put(fNew, new int[] { i - 1, j });
                        cellDetails[i - 1][j].f = fNew;
                        cellDetails[i - 1][j].g = gNew;
                        cellDetails[i - 1][j].h = hNew;
                        cellDetails[i - 1][j].parent_i = i;
                        cellDetails[i - 1][j].parent_j = j;
                    }
                }
            }

            // 2nd Successor (South)
            if (isValid(i + 1, j, LEN_VERTICAL, LEN_HOR)) {
                if (isDestination(i + 1, j, dest)) {
                    cellDetails[i + 1][j].parent_i = i;
                    cellDetails[i + 1][j].parent_j = j;
                    return tracePath(cellDetails, dest);
                }
                else if (!closedList[i + 1][j] && isUnBlocked(grid, i + 1, j)) {
                    gNew = cellDetails[i][j].g + 1;
                    hNew = calculateHValue(i + 1, j, dest);
                    fNew = gNew + hNew;
                    if (cellDetails[i + 1][j].f == Double.POSITIVE_INFINITY || cellDetails[i + 1][j].f > fNew) {
                        openList.put(fNew, new int[] { i + 1, j });
                        cellDetails[i + 1][j].f = fNew;
                        cellDetails[i + 1][j].g = gNew;
                        cellDetails[i + 1][j].h = hNew;
                        cellDetails[i + 1][j].parent_i = i;
                        cellDetails[i + 1][j].parent_j = j;
                    }
                }
            }

            // 3rd Successor (East)
            if (isValid(i, j + 1, LEN_VERTICAL, LEN_HOR)) {
                if (isDestination(i, j + 1, dest)) {
                    cellDetails[i][j + 1].parent_i = i;
                    cellDetails[i][j + 1].parent_j = j;
                    return tracePath(cellDetails, dest);
                }
                else if (!closedList[i][j + 1] && isUnBlocked(grid, i, j + 1)) {
                    gNew = cellDetails[i][j].g + 1;
                    hNew = calculateHValue(i, j + 1, dest);
                    fNew = gNew + hNew;
                    if (cellDetails[i][j + 1].f == Double.POSITIVE_INFINITY || cellDetails[i][j + 1].f > fNew) {
                        openList.put(fNew, new int[] { i, j + 1 });
                        cellDetails[i][j + 1].f = fNew;
                        cellDetails[i][j + 1].g = gNew;
                        cellDetails[i][j + 1].h = hNew;
                        cellDetails[i][j + 1].parent_i = i;
                        cellDetails[i][j + 1].parent_j = j;
                    }
                }
            }

            // 4th Successor (West)
            if (isValid(i, j - 1, LEN_VERTICAL, LEN_HOR)) {
                if (isDestination(i, j - 1, dest)) {
                    cellDetails[i][j - 1].parent_i = i;
                    cellDetails[i][j - 1].parent_j = j;
                    return tracePath(cellDetails, dest);
                }
                else if (!closedList[i][j - 1] && isUnBlocked(grid, i, j - 1)) {
                    gNew = cellDetails[i][j].g + 1;
                    hNew = calculateHValue(i, j - 1, dest);
                    fNew = gNew + hNew;
                    if (cellDetails[i][j - 1].f == Double.POSITIVE_INFINITY || cellDetails[i][j - 1].f > fNew) {
                        openList.put(fNew, new int[] { i, j - 1 });
                        cellDetails[i][j - 1].f = fNew;
                        cellDetails[i][j - 1].g = gNew;
                        cellDetails[i][j - 1].h = hNew;
                        cellDetails[i][j - 1].parent_i = i;
                        cellDetails[i][j - 1].parent_j = j;
                    }
                }
            }
        }
        throw new UnsupportedOperationException("Failed to find the destination cell");
    }
}

