/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abstract_samuel_said;

public class Sudoku {
    private int[][] grid = new int[9][9];
    private int[] rowMask = new int[9];
    private int[] colMask = new int[9];
    private int[] boxMask = new int[9];

    public Sudoku(int[][] inicial) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = inicial[r][c];
                if (val != 0) {
                    set(r, c, val, true);
                }
            }
        }
    }

    private int boxIndex(int r, int c) {
        return (r / 3) * 3 + (c / 3);
    }

    /**
     * Coloca o quita un valor en la celda (r,c), actualizando máscaras.
     */
    public void set(int r, int c, int val, boolean place) {
        if (place) {
            grid[r][c] = val;
            int bit = 1 << (val - 1);
            rowMask[r] |= bit;
            colMask[c] |= bit;
            boxMask[boxIndex(r, c)] |= bit;
        } else {
            int bit = ~(1 << (grid[r][c] - 1));
            rowMask[r] &= bit;
            colMask[c] &= bit;
            boxMask[boxIndex(r, c)] &= bit;
            grid[r][c] = 0;
        }
    }

    /**
     * Resuelve el Sudoku usando backtracking con heurística de celda mínima.
     * @return true si se resolvió completamente.
     */
    public boolean solve() {
        int bestR = -1, bestC = -1, minOpts = 10;
        // Busca la celda vacía con menos candidatos
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid[r][c] != 0) continue;
                int used = rowMask[r] | colMask[c] | boxMask[boxIndex(r, c)];
                int opts = 9 - Integer.bitCount(used);
                if (opts < minOpts) {
                    minOpts = opts;
                    bestR = r;
                    bestC = c;
                }
            }
        }
        if (bestR < 0) {
            // No hay celdas vacías: ¡resuelto!
            return true;
        }
        int used = rowMask[bestR] | colMask[bestC] | boxMask[boxIndex(bestR, bestC)];
        for (int v = 1; v <= 9; v++) {
            int bit = 1 << (v - 1);
            if ((used & bit) == 0) {
                set(bestR, bestC, v, true);
                if (solve()) return true;
                set(bestR, bestC, v, false);
            }
        }
        return false;
    }

    public int[][] getGrid() {
        return grid;
    }
}