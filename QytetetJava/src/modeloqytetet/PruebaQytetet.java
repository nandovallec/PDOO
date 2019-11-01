/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author nandovallec
 */
public class PruebaQytetet {
    private static ArrayList<Sorpresa> mazo = new ArrayList();
    private static final Qytetet juego = Qytetet.getInstance(new ArrayList(Arrays.asList("Paco", "Manolo", "Eufemiano")));
    
    
    private static ArrayList MayoresQueCero(){
        ArrayList<Sorpresa> mayores0 = new ArrayList();
        for (int i=0; i < mazo.size(); i++) {
            if (mazo.get(i).getValor() > 0)
                mayores0.add(mazo.get(i));
        }
        
        return mayores0;        
    }
    
    
    private static ArrayList TiposIrACasilla(){
        ArrayList<Sorpresa> iracasilla = new ArrayList();
        for (int i=0; i < mazo.size(); i++) {
            if (mazo.get(i).getTipo() == TipoSorpresa.IRACASILLA)
                iracasilla.add(mazo.get(i));
        }
        
        return iracasilla; 
    }
    
    
    private static ArrayList TipoEspecificado(TipoSorpresa tipo){
        ArrayList<Sorpresa> especificado = new ArrayList();
        for (int i=0; i < mazo.size(); i++) {
            if (mazo.get(i).getTipo() == tipo)
                especificado.add(mazo.get(i));
        }
        
        return especificado;
    }
    
    
    /**
     * @param args the command line arguments
     */
   /* public static void main(String[] args) {
        // TODO code application logic here
        System.out.println(juego.toString());
    }*/
}
