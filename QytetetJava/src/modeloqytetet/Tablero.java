/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.ArrayList;

/**
 *
 * @author jose26398
 */
public class Tablero {
    private ArrayList<Casilla> casillas = new ArrayList();;
    private Casilla carcel;

    public Tablero() {
        inicializar();
    }
    
    Casilla getCarcel() {
        return carcel;
    }

    Casilla obtenerCasillaNumero(int numeroCasilla){
        return casillas.get(numeroCasilla);
    }
    
    Casilla obtenerNuevaCasilla(Casilla casilla, int desplazamiento){
        return casillas.get((casilla.getNumeroCasilla()+desplazamiento)% casillas.size());
    }
    
    boolean esCasillaCarcel(int numeroCasilla){
        return getCarcel().getNumeroCasilla() == numeroCasilla;
    }
    
    @Override
    public String toString() {
        return "Tablero{" + "casillas=" + casillas + ", carcel=" + carcel + "}";
    }
    
    private void inicializar(){
        casillas.add(new Casilla(0, TipoCasilla.SALIDA));
        casillas.add(new Casilla(1, 600, new TituloPropiedad("Avenida Calvo Sotelo", 50, (float)1.84, 150, 250)));
        casillas.add(new Casilla(2, 600, new TituloPropiedad("Calle de Velázquez", 50, (float)1.75, 150, 250)));
        casillas.add(new Casilla(3, TipoCasilla.IMPUESTO, 400));
        casillas.add(new Casilla(4, 650, new TituloPropiedad("Calle de la Paz", 52, (float)1.52, 200, 250)));
        carcel = new Casilla(5, TipoCasilla.CARCEL);
        casillas.add(carcel);
        casillas.add(new Casilla(6, 1000, new TituloPropiedad("Plaza Bálmes", 62, (float)1.59, 300, 350)));
        casillas.add(new Casilla(7, TipoCasilla.SORPRESA));
        casillas.add(new Casilla(8, 1000, new TituloPropiedad("Calle de las Delicias", 62, (float) 1.70, 300, 350)));
        casillas.add(new Casilla(9, 1100, new TituloPropiedad("Calle de Serranos", 65, (float) 1.9, 350, 400)));
        casillas.add(new Casilla(10, TipoCasilla.PARKING));
        casillas.add(new Casilla(11, 1700, new TituloPropiedad("Plaza de América", 79, (float) 1.73, 650, 500)));
        casillas.add(new Casilla(12, TipoCasilla.SORPRESA));
        casillas.add(new Casilla(13, 1700, new TituloPropiedad("Calle de Brasil", 79, (float) 1.45, 650, 500)));
        casillas.add(new Casilla(14, 1900, new TituloPropiedad("Calle de Alcalá", 83, (float)1.33, 700, 550)));
        casillas.add(new Casilla(15, TipoCasilla.JUEZ));
        casillas.add(new Casilla(16, 2500, new TituloPropiedad("Gran Vía", 95, (float)1.52, 950, 700)));
        casillas.add(new Casilla(17, TipoCasilla.SORPRESA));
        casillas.add(new Casilla(18, 2500, new TituloPropiedad("Ramblas", 95, (float)1.52, 950, 700)));
        casillas.add(new Casilla(19, 2700, new TituloPropiedad("GV Fernando el Católico", 100, (float)1.92, 1000, 750)));    
        
    }
}
