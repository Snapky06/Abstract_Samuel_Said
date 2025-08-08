/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abstract_samuel_said;

import javax.swing.JButton;
/**
 *
 * @author unwir
 */
public abstract class Boton extends JButton {
    private int columna;
    private int fila;
    private int numBot;
   
    
    public Boton(int columna, int fila){
        this.columna = columna;
        this.fila = fila;            
    }
    
    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }
    
    public void setNum(int num){
        numBot = num;
    }
    
    public int getNum(){
        return numBot;
    }
    
}
