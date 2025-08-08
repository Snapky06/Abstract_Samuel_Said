/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abstract_samuel_said;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;

/**
 *
 * @author unwir
 */
public class VistaSudoku extends JPanel {
    private JTextField[][] celdas = new JTextField[9][9];
    private OyenteCelda oyente;
    private int selRow = -1, selCol = -1;

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
                tf.setEditable(false);
                tf.setBackground(Color.WHITE);
                final int rf = f, cf = c;
                tf.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (oyente != null && !m.esFijo(rf, cf)) {
                            oyente.celdaSeleccionada(rf, cf);
                        }
                    }
                });
                celdas[f][c] = tf;
                add(tf);
            }
        }
    }

    public void setOyente(OyenteCelda o) { this.oyente = o; }
    public void seleccionar(int f, int c) { selRow = f; selCol = c; }

    public void actualizarCelda(int f, int c, int v, boolean fijo) {
        JTextField tf = celdas[f][c];
        tf.setText(v == 0 ? "" : String.valueOf(v));
        tf.setForeground(fijo ? Color.BLACK : Color.BLUE);
        // Colorear fila, columna y bloque resaltado
        if (f == selRow || c == selCol ||
            (f/3 == selRow/3 && c/3 == selCol/3)) {
            tf.setBackground(new Color(220, 240, 255));
        } else {
            tf.setBackground(Color.WHITE);
        }
    }

    interface OyenteCelda { void celdaSeleccionada(int fila, int col); }
}

// ControladorSudoku.java

