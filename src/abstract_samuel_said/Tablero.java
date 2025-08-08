package abstract_samuel_said;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

/**
 * Panel de Sudoku estilizado al estilo de juego clásico:
 * bordes gruesos cada 3x3, celdas centradas y fondo blanco.
 */
public class Tablero extends JPanel {
    private Boton[][] botones = new Boton[9][9];
    private Sudoku modelo;
    
    public Tablero(Sudoku modelo) {
        this.modelo = modelo;
        // Usamos GridLayout sin gaps, las MatteBorder dibujan las líneas
        setLayout(new GridLayout(9, 9, 0, 0));

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = modelo.getGrid()[r][c];
                Boton b = (val > 0)
                    ? new BotonRandom(c, r)
                    : new BotonPlayer(c, r);

                // Inicializar número y texto
                b.setNum(val);
                b.setText(val > 0 ? String.valueOf(val) : "");

                // Estética: fuente y alineación
                b.setFont(new Font("SansSerif", Font.BOLD, 20));
                b.setHorizontalAlignment(SwingConstants.CENTER);
                b.setVerticalAlignment(SwingConstants.CENTER);

                // Fondo blanco y opaco
                b.setBackground(Color.WHITE);
                b.setOpaque(true);

                // Prefijados deshabilitados
                if (val > 0) {
                    b.setEnabled(false);
                }

                // Bordes gruesos cada 3 celdas
                int top = (r % 3 == 0 ? 3 : 1);
                int left = (c % 3 == 0 ? 3 : 1);
                int bottom = (r == 8 ? 3 : 1);
                int right = (c == 8 ? 3 : 1);
                b.setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));

                // Click para ciclar número
                b.addActionListener(e -> manejarClick((Boton) e.getSource()));

                botones[r][c] = b;
                add(b);
            }
        }
    }

    private void manejarClick(Boton b) {
        int n = (b.getNum() % 9) + 1;
        b.setNum(n);
        b.setText(n > 0 ? String.valueOf(n) : "");
        modelo.set(b.getFila(), b.getColumna(), n, true);
    }
    
    /**
     * Valida el tablero y marca en rosa los botones en conflicto.
     */
    public void validar() {
        boolean[][] error = new boolean[9][9];
        int[][] grid = modelo.getGrid();

        // Filas
        for (int r = 0; r < 9; r++) {
            for (int c1 = 0; c1 < 9; c1++) {
                int v = grid[r][c1];
                if (v == 0) continue;
                for (int c2 = c1 + 1; c2 < 9; c2++) {
                    if (grid[r][c2] == v) {
                        error[r][c1] = true;
                        error[r][c2] = true;
                    }
                }
            }
        }
        // Columnas
        for (int c = 0; c < 9; c++) {
            for (int r1 = 0; r1 < 9; r1++) {
                int v = grid[r1][c];
                if (v == 0) continue;
                for (int r2 = r1 + 1; r2 < 9; r2++) {
                    if (grid[r2][c] == v) {
                        error[r1][c] = true;
                        error[r2][c] = true;
                    }
                }
            }
        }
        // Subcuadrantes 3x3
        for (int br = 0; br < 3; br++) {
            for (int bc = 0; bc < 3; bc++) {
                int baseR = br * 3, baseC = bc * 3;
                for (int i = 0; i < 9; i++) {
                    int r1 = baseR + i / 3;
                    int c1 = baseC + i % 3;
                    int v = grid[r1][c1];
                    if (v == 0) continue;
                    for (int j = i + 1; j < 9; j++) {
                        int r2 = baseR + j / 3;
                        int c2 = baseC + j % 3;
                        if (grid[r2][c2] == v) {
                            error[r1][c1] = true;
                            error[r2][c2] = true;
                        }
                    }
                }
            }
        }
        // Aplicar color de fondo
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Boton b = botones[r][c];
                b.setBackground(error[r][c] ? Color.PINK : Color.WHITE);
            }
        }
    }

    /**
     * Devuelve el botón en la posición dada.
     */
    public Boton getBoton(int r, int c) {
        return botones[r][c];
    }
}


