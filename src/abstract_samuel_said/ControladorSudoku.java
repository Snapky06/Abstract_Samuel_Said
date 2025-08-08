/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abstract_samuel_said;

/**
 *
 * @author unwir
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControladorSudoku implements VistaSudoku.OyenteCelda {
    private ModeloSudoku modelo;
    private VistaSudoku vista;
    private JPanel panelLateral;
    private JLabel etiquetaErrores;
    private JComboBox<String> comboDificultad;
    private int filaSel = -1, colSel = -1;
    private int contErrores = 0;
    private int pistasInicial = 30;
    private static final int MAX_ERRORES = 3;

    public ControladorSudoku(ModeloSudoku m, VistaSudoku v) {
        this.modelo = m;
        this.vista = v;
        vista.setOyente(this);
        modelo.generar(pistasInicial);
        crearPanelLateral();
        refrescarVista();
    }

    private void crearPanelLateral() {
        panelLateral = new JPanel(new BorderLayout(5, 5));
        etiquetaErrores = new JLabel("Errores: 0/" + MAX_ERRORES, SwingConstants.CENTER);
        etiquetaErrores.setFont(etiquetaErrores.getFont().deriveFont(Font.BOLD, 16f));
        panelLateral.add(etiquetaErrores, BorderLayout.NORTH);

        JPanel numPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        for (int i = 1; i <= 9; i++) {
            JButton btn = new JButton(String.valueOf(i));
            btn.setFont(btn.getFont().deriveFont(Font.BOLD, 18f));
            btn.addActionListener(e -> ingresarNumero(Integer.parseInt(btn.getText())));
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

        JPanel diffPanel = new JPanel();
        diffPanel.add(new JLabel("Dificultad:"));
        comboDificultad = new JComboBox<>(new String[]{"Fácil", "Media", "Difícil"});
        comboDificultad.setSelectedIndex(1);
        comboDificultad.addActionListener(e -> {
            switch (comboDificultad.getSelectedItem().toString()) {
                case "Fácil": pistasInicial = 40; break;
                case "Media": pistasInicial = 30; break;
                case "Difícil": pistasInicial = 20; break;
            }
            reiniciarJuego();
        });
        diffPanel.setLayout(new BoxLayout(diffPanel, BoxLayout.Y_AXIS));
        diffPanel.add(comboDificultad);
        panelLateral.add(diffPanel, BorderLayout.NORTH);
    }

    public JPanel obtenerPanelLateral() { return panelLateral; }

    @Override
    public void celdaSeleccionada(int f, int c) {
        filaSel = f; colSel = c;
        vista.seleccionar(f, c);
        refrescarVista();
    }

    private void ingresarNumero(int v) {
        if (filaSel < 0 || colSel < 0) return;
        if (contErrores >= MAX_ERRORES) return;
        if (modelo.esValido(filaSel, colSel, v)) {
            modelo.asignar(filaSel, colSel, v);
        } else {
            contErrores++;
            etiquetaErrores.setText("Errores: " + contErrores + "/" + MAX_ERRORES);
            if (contErrores >= MAX_ERRORES) {
                JOptionPane.showMessageDialog(null, "Límite de errores alcanzado. Nuevo juego.");
                reiniciarJuego();
                return;
            } else {
                int quedan = MAX_ERRORES - contErrores;
                JOptionPane.showMessageDialog(null, "Error. Quedan " + quedan + " intentos.");
            }
        }
        refrescarVista();
    }

    private void reiniciarJuego() {
        contErrores = 0;
        modelo.generar(pistasInicial);
        filaSel = colSel = -1;
        refrescarVista();
    }

    private void limpiarEntradas() {
        contErrores = 0;
        modelo.limpiarEntradas();
        refrescarVista();
    }

    private void refrescarVista() {
        etiquetaErrores.setText("Errores: " + contErrores + "/" + MAX_ERRORES);
        for (int f = 0; f < 9; f++) {
            for (int c = 0; c < 9; c++) {
                vista.actualizarCelda(f, c, modelo.obtener(f, c), modelo.esFijo(f, c));
            }
        }
    }
}

