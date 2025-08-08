package abstract_samuel_said;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AplicacionSudoku {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Sudoku Mejorado");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(800, 600);
            ventana.setLocationRelativeTo(null);

            ModeloSudoku modelo = new ModeloSudoku();
            VistaSudoku vista = new VistaSudoku(modelo);
            ControladorSudoku controlador = new ControladorSudoku(modelo, vista);

            ventana.setLayout(new BorderLayout(5, 5));
            ventana.add(vista, BorderLayout.CENTER);
            ventana.add(controlador.obtenerPanelLateral(), BorderLayout.EAST);
            ventana.setVisible(true);
        });
    }
}

class ModeloSudoku {
    private int[][] tablero = new int[9][9];
    private boolean[][] fijos = new boolean[9][9];

    public int obtener(int f, int c) {
        return tablero[f][c];
    }

    public boolean esFijo(int f, int c) {
        return fijos[f][c];
    }

    public void asignar(int f, int c, int v) {
        tablero[f][c] = v;
    }

    public void generar(int pistas) {
        llenarTableroCompleto();
        quitarNumeros(81 - pistas);
        for (int f = 0; f < 9; f++) {
            for (int c = 0; c < 9; c++) {
                fijos[f][c] = (tablero[f][c] != 0);
            }
        }
    }

    public void limpiarEntradas() {
        for (int f = 0; f < 9; f++) {
            for (int c = 0; c < 9; c++) {
                if (!fijos[f][c]) {
                    tablero[f][c] = 0;
                }
            }
        }
    }

    public boolean esValido(int f, int c, int v) {
        for (int i = 0; i < 9; i++) {
            if (tablero[f][i] == v || tablero[i][c] == v) {
                return false;
            }
        }
        int sr = (f / 3) * 3;
        int sc = (c / 3) * 3;
        for (int i = sr; i < sr + 3; i++) {
            for (int j = sc; j < sc + 3; j++) {
                if (tablero[i][j] == v) {
                    return false;
                }
            }
        }
        return true;
    }

    private void llenarTableroCompleto() {
        tablero = new int[9][9];
        rellenarRecursivo(0, 0);
    }

    private boolean rellenarRecursivo(int f, int c) {
        if (f == 9) {
            return true;
        }
        int nf = (c == 8) ? f + 1 : f;
        int nc = (c == 8) ? 0 : c + 1;
        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 9; i++) nums.add(i);
        Collections.shuffle(nums);
        for (int v : nums) {
            if (esValido(f, c, v)) {
                tablero[f][c] = v;
                if (rellenarRecursivo(nf, nc)) {
                    return true;
                }
            }
        }
        tablero[f][c] = 0;
        return false;
    }

    private void quitarNumeros(int cuenta) {
        int eliminado = 0;
        Random rnd = new Random();
        while (eliminado < cuenta) {
            int f = rnd.nextInt(9);
            int c = rnd.nextInt(9);
            if (tablero[f][c] != 0) {
                int bk = tablero[f][c];
                tablero[f][c] = 0;
                int[][] copia = copiarTablero();
                if (contarSoluciones(copia, 2) == 1) {
                    eliminado++;
                } else {
                    tablero[f][c] = bk;
                }
            }
        }
    }

    private int contarSoluciones(int[][] bt, int lim) {
        return contarRecursivo(bt, 0, 0, lim);
    }

    private int contarRecursivo(int[][] bt, int f, int c, int lim) {
        if (f == 9) {
            return 1;
        }
        int nf = (c == 8) ? f + 1 : f;
        int nc = (c == 8) ? 0 : c + 1;
        if (bt[f][c] != 0) {
            return contarRecursivo(bt, nf, nc, lim);
        }
        int cnt = 0;
        for (int v = 1; v <= 9; v++) {
            if (validoEn(bt, f, c, v)) {
                bt[f][c] = v;
                cnt += contarRecursivo(bt, nf, nc, lim);
                if (cnt >= lim) break;
            }
        }
        bt[f][c] = 0;
        return cnt;
    }

    private boolean validoEn(int[][] bt, int f, int c, int v) {
        for (int i = 0; i < 9; i++) {
            if (bt[f][i] == v || bt[i][c] == v) {
                return false;
            }
        }
        int sr = (f / 3) * 3;
        int sc = (c / 3) * 3;
        for (int i = sr; i < sr + 3; i++) {
            for (int j = sc; j < sc + 3; j++) {
                if (bt[i][j] == v) {
                    return false;
                }
            }
        }
        return true;
    }

    private int[][] copiarTablero() {
        int[][] copia = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(tablero[i], 0, copia[i], 0, 9);
        }
        return copia;
    }
}

class VistaSudoku extends JPanel {
    private JTextField[][] celdas = new JTextField[9][9];
    private OyenteCelda oyente;

    public VistaSudoku(ModeloSudoku m) {
        setLayout(new GridLayout(9, 9));
        for (int f = 0; f < 9; f++) {
            for (int c = 0; c < 9; c++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(SwingConstants.CENTER);
                tf.setFont(tf.getFont().deriveFont(Font.BOLD, 20f));
                tf.setBorder(new MatteBorder(
                    (f % 3 == 0 ? 2 : 1),
                    (c % 3 == 0 ? 2 : 1),
                    (f == 8 ? 2 : 1),
                    (c == 8 ? 2 : 1),
                    Color.BLACK
                ));
                final int rf = f, cf = c;
                tf.setEditable(false);
                tf.setBackground(Color.WHITE);
                tf.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (oyente != null) oyente.celdaSeleccionada(rf, cf);
                    }
                });
                celdas[f][c] = tf;
                add(tf);
            }
        }
    }

    void setOyente(OyenteCelda o) {
        this.oyente = o;
    }

    void actualizarCelda(int f, int c, int v, boolean fijo) {
        JTextField tf = celdas[f][c];
        tf.setText(v == 0 ? "" : String.valueOf(v));
        tf.setForeground(fijo ? Color.BLACK : Color.BLUE);
    }

    interface OyenteCelda {
        void celdaSeleccionada(int fila, int col);
    }
}

class ControladorSudoku implements VistaSudoku.OyenteCelda {
    private ModeloSudoku modelo;
    private VistaSudoku vista;
    private JPanel panelLateral;
    private JLabel etiquetaErrores;
    private int filaSel = -1, colSel = -1;
    private int contErrores = 0;
    private static final int MAX_ERRORES = 3;

    public ControladorSudoku(ModeloSudoku m, VistaSudoku v) {
        this.modelo = m;
        this.vista = v;
        vista.setOyente(this);
        modelo.generar(30);
        crearPanelLateral();
        refrescarVista();
    }

    private void crearPanelLateral() {
        panelLateral = new JPanel(new BorderLayout(5, 5));
        etiquetaErrores = new JLabel("Errores: 0/" + MAX_ERRORES,
            SwingConstants.CENTER);
        etiquetaErrores.setFont(
            etiquetaErrores.getFont().deriveFont(Font.BOLD, 16f)
        );
        panelLateral.add(etiquetaErrores, BorderLayout.NORTH);

        JPanel numPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        for (int i = 1; i <= 9; i++) {
            JButton btn = new JButton(String.valueOf(i));
            btn.setFont(btn.getFont().deriveFont(Font.BOLD, 18f));
            btn.addActionListener(e -> ingresarNumero(
                Integer.parseInt(btn.getText())
            ));
            numPanel.add(btn);
        }
        panelLateral.add(numPanel, BorderLayout.CENTER);

        JPanel ctrlPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton btnNuevo = new JButton("Nuevo Juego");
        JButton btnLimpiar = new JButton("Reiniciar");
        btnNuevo.addActionListener(e -> reiniciarJuego());
        btnLimpiar.addActionListener(e -> limpiarEntradas());
        ctrlPanel.add(btnNuevo);
        ctrlPanel.add(btnLimpiar);
        panelLateral.add(ctrlPanel, BorderLayout.SOUTH);
    }

    public JPanel obtenerPanelLateral() {
        return panelLateral;
    }

    @Override
    public void celdaSeleccionada(int f, int c) {
        if (modelo.esFijo(f, c)) return;
        filaSel = f;
        colSel  = c;
    }

    private void ingresarNumero(int v) {
        if (filaSel < 0 || colSel < 0) return;
        if (contErrores >= MAX_ERRORES) {
            JOptionPane.showMessageDialog(
                null,
                "Limite de errores alcanzado."
            );
            return;
        }
        if (modelo.esValido(filaSel, colSel, v)) {
            modelo.asignar(filaSel, colSel, v);
        } else {
            contErrores++;
            etiquetaErrores.setText(
                "Errores: " + contErrores + "/" + MAX_ERRORES
            );
            int quedan = MAX_ERRORES - contErrores;
            JOptionPane.showMessageDialog(
                null,
                "Error. Quedan " + quedan + " intentos."
            );
        }
        refrescarVista();
    }

    private void reiniciarJuego() {
        contErrores = 0;
        modelo.generar(30);
        filaSel = colSel = -1;
        refrescarVista();
    }

    private void limpiarEntradas() {
        contErrores = 0;
        modelo.limpiarEntradas();
        refrescarVista();
    }

    private void refrescarVista() {
        etiquetaErrores.setText(
            "Errores: " + contErrores + "/" + MAX_ERRORES
        );
        for (int f = 0; f < 9; f++) {
            for (int c = 0; c < 9; c++) {
                vista.actualizarCelda(
                    f, c,
                    modelo.obtener(f, c),
                    modelo.esFijo(f, c)
                );
            }
        }
    }
}
