/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfazTextualQytetet;
import java.io.IOException;
import modeloqytetet.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/**
 *
 * @author nandovallec
 */
public class ControladorQytetet {
    private static Qytetet juego;
    private static Jugador jugador;
    private static Casilla casilla;
    private static VistaTextualQytetet vista = new VistaTextualQytetet();

    public static Qytetet getJuego() {
        return juego;
    }

    public static Jugador getJugador() {
        return jugador;
    }

    public static Casilla getCasilla() {
        return casilla;
    }
    
    public void inicializarJuego(){
        ArrayList<String> nombres = vista.obtenerNombreJugadores();
        juego = Qytetet.getInstance(nombres);
        jugador = juego.getJugadorActual();
        System.out.println(nombres);
        casilla = jugador.getCasillaActual();
        

    }
    
    
    public void desarrolloJuego(){     
        boolean bancarrota;
        do{
        vista.mostrar("El jugador " + jugador.getNombre() + " comienza a jugar. Está situado en la casilla número " + jugador.getCasillaActual().getNumeroCasilla());
        vista.mostrar("El jugador tiene las siguientes características: "+ jugador.toString());
        
        if(jugador.getEncarcelado()){
            MetodoSalirCarcel metodo_a_utilizar = (MetodoSalirCarcel.values())[vista.menuSalirCarcel()];
            boolean libre = juego.intentarSalirCarcel(metodo_a_utilizar);
            
            if(libre){
                vista.mostrar("El jugador ha conseguido salir de la carcel.");
            }
            else{
                vista.mostrar("El jugador no ha conseguido salir de la carcel.");
            }
            vista.pausaLeer();
        }
        
        if(!(jugador.getEncarcelado())){
            boolean noTienePropietario = juego.jugar();
            casilla = jugador.getCasillaActual();
            vista.mostrar("El jugador " + jugador.getNombre() + " se mueve y avanza a la casilla " + casilla.toString());
            vista.pausaLeer();
            bancarrota = jugador.getSaldo() < 0;
            
            if(bancarrota)
                vista.mostrar("Has caido en bancarrota.");
            
            else{
                if(jugador.getEncarcelado())
                    vista.mostrar("Estás en la cárcel.");
                
                else{
                    if(casilla.getTipo() == TipoCasilla.CALLE){
                        vista.mostrar("El jugador esta en una casilla de tipo calle con el siguiente título de propiedad.");
                        vista.mostrar(casilla.getTitulo().toString());
                        vista.pausaLeer();
                        if(!(casilla.tengoPropietario())){
                            if(vista.elegirQuieroComprar())
                                
                                if(juego.comprarTituloPropiedad())
                                    vista.mostrar("Has adquirido la propiedad correctamente.");
                                else
                                    vista.mostrar("No has podido adquierir la propiedad.");
                        }
                    }
                    
                    else if (casilla.getTipo() == TipoCasilla.SORPRESA){
                        noTienePropietario = juego.aplicarSorpresa();
                        
                        if(jugador.getEncarcelado())
                            vista.mostrar("Has sido encarcelado.");
                        else{
                            bancarrota = jugador.getSaldo() < 0;
                            if(bancarrota)
                                vista.mostrar("Has caido en bancarrota.");
                            else{
                                if (casilla.getTipo() == TipoCasilla.CALLE){
                                    vista.mostrar("El jugador esta en una casilla de tipo calle con el siguiente título de propiedad.");
                                    vista.mostrar(casilla.getTitulo().toString());
                                    vista.pausaLeer();
                                    if(!(casilla.tengoPropietario())){
                                        if(vista.elegirQuieroComprar())
                                            if(juego.comprarTituloPropiedad())
                                                vista.mostrar("Has adquirido la propiedad correctamente.");
                                            else
                                                vista.mostrar("No has podido adquierir la propiedad.");
                                    
                                    }
                                }
                            }
                        }
                            
                        
                    }
                    
                    if(!(jugador.getEncarcelado()) && !bancarrota && jugador.tengoPropiedades()){
                        vista.mostrar("Las propiedades del jugador son: ");
                        int opcion = vista.menuGestionInmobiliaria();
                        while(opcion != 0 && jugador.tengoPropiedades()){
                            int elegida = vista.menuElegirPropiedad(getMenuPropiedades(opcion));
                            switch (opcion){
                                case 1:
                                    juego.edificarCasa(jugador.getPropiedades().get(elegida).getCasilla());
                                    break;
                                case 2:
                                    juego.edificarHotel(jugador.getPropiedades().get(elegida).getCasilla());
                                    break;
                                case 3:
                                    juego.venderPropiedad(jugador.getPropiedades().get(elegida).getCasilla());
                                    break;
                                case 4:
                                    juego.hipotecarPropiedad(jugador.getPropiedades().get(elegida).getCasilla());
                                    break;
                                case 5:
                                    juego.cancelarHipoteca(jugador.getPropiedades().get(elegida).getCasilla());
                                    break;
                            }
                            
                            vista.mostrar("Las propiedades del jugador son: ");
                            opcion = vista.menuGestionInmobiliaria();
                            
                        }
                    }
                }
            }
                    
        }
        
        bancarrota = jugador.getSaldo() < 0;
        if (!bancarrota){
            vista.mostrar("El estado final del jugador actual es: "+ jugador.toString());
            vista.mostrar("Fin de turno de " + jugador.getNombre());
            juego.siguienteJugador();
            jugador = juego.getJugadorActual();
            vista.mostrar("Comienza el turno de " + juego.getJugadorActual().getNombre());
        }
        
        bancarrota = jugador.getSaldo() < 0;
        if(bancarrota)
            vista.mostrar("El jugador " + jugador.getNombre() + " cayó en bancarrota.");
        
        } while(!bancarrota);
        
        vista.mostrar("El juego ha terminado");
        vista.mostrar("El ranking final es:");
        Map<Integer, String> ranking = (juego.obtenerRanking()).descendingMap();
        for(Integer clave : ranking.keySet()) {
            String nombre = ranking.get(clave);
            vista.mostrar("Jugador: " + nombre + " con capital: " + clave);
        }
    }
    
    public ArrayList<String> getMenuPropiedades(int opcion){
        ArrayList<String> devolver = new ArrayList<>();
        ArrayList<TituloPropiedad> buscar;
        if(opcion == 4)
            buscar = jugador.obtenerPropiedadesHipotecadas(false);
        else if (opcion == 5)
            buscar = jugador.obtenerPropiedadesHipotecadas(true);
        else
            buscar = jugador.getPropiedades();
        
        for(TituloPropiedad titulo: buscar){
            String aniadir = "Casilla numero: " + titulo.getCasilla().getNumeroCasilla() + " Num_casas: " + titulo.getCasilla().getNumCasas() + " Num_hoteles: "
                    + titulo.getCasilla().getNumHoteles() +" "+ titulo.toString();
            devolver.add(aniadir);
        }
        
        return devolver;
    }
    
    
    public Casilla elegirPropiedad(ArrayList<Casilla> propiedades){ 
// //este metodo toma una lista de propiedades y genera una lista de strings, con el numero y nombre de las propiedades
// //luego llama a la vista para que el usuario pueda elegir.
        vista.mostrar("\tCasilla\tTitulo");
        int seleccion;
        ArrayList<String> listaPropiedades= new ArrayList();
        for ( Casilla cas: propiedades) {
                listaPropiedades.add( "\t"+cas.getNumeroCasilla()+"\t"+cas.getTitulo().getNombre()); 
        }
        seleccion=vista.menuElegirPropiedad(listaPropiedades);  
        return propiedades.get(seleccion);
         }
 
    
    
    public static void main(String[] args) {
        ControladorQytetet meh = new ControladorQytetet();
        meh.inicializarJuego();
        System.out.println(meh.getJuego().toString());
        vista.pausaLeer();
        meh.desarrolloJuego();
        vista.mostrar("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
    }
    
    
    
    
}


