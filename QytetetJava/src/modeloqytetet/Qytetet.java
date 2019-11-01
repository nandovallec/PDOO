/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;
import InterfazTextualQytetet.VistaTextualQytetet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
/**
 *
 * @author jose26398
 */
public class Qytetet {
    static public int MAX_JUGADORES = 4;
    static final int MAX_CARTAS = 10;
    static final int MAX_CASILLAS = 20;
    static final int PRECIO_LIBERTAD = 200;
    static final int SALDO_SALIDA = 1000;
    private Dado dado = Dado.getInstance();
    private Sorpresa cartaActual;
    private Jugador jugadorActual;
    private ArrayList<Sorpresa> mazo = new ArrayList();
    private ArrayList<Jugador> jugadores = new ArrayList();
    private Tablero tablero;

    
    private Qytetet(ArrayList<String> nombres) {
        inicializarJuego(nombres);
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    
    public boolean aplicarSorpresa(){
        boolean tienePropietario = false;
        
        if (cartaActual.getTipo() == TipoSorpresa.PAGARCOBRAR)
            jugadorActual.modificarSaldo(cartaActual.getValor());
        
        else if(cartaActual.getTipo() == TipoSorpresa.IRACASILLA){
            boolean esCarcel = tablero.esCasillaCarcel(cartaActual.getValor());
            
            if(esCarcel){
                encarcelarJugador();
            }
            else{
                Casilla nuevaCasilla = tablero.obtenerCasillaNumero(cartaActual.getValor());
                tienePropietario = jugadorActual.actualizarPosicion(nuevaCasilla);
            }
        }
        else if(cartaActual.getTipo()==TipoSorpresa.PORCASAHOTEL){
            jugadorActual.pagarCobrarPorCasaYHotel(cartaActual.getValor());
            
        }
        else if(cartaActual.getTipo()==TipoSorpresa.PORJUGADOR){
            for(Jugador jugador: jugadores){
                if(jugador != jugadorActual){
                    jugador.modificarSaldo(cartaActual.getValor());
                    jugadorActual.modificarSaldo(-cartaActual.getValor());
                }
            }
        }
        
        if(cartaActual.getTipo() == TipoSorpresa.SALIRCARCEL)
            jugadorActual.setCartaLibertad(cartaActual);
        
        else{
            mazo.add(cartaActual);
            cartaActual = null;
        }
        
        return tienePropietario;
    }
    
    public boolean cancelarHipoteca(Casilla casilla){
        boolean puedoDeshipotecar = false;
        if(casilla.soyEdificable()){
            boolean sePuedeDeshipotecar = casilla.estaHipotecada();
            
            if(sePuedeDeshipotecar){
                puedoDeshipotecar = jugadorActual.puedoPagarHipoteca(casilla);
                
                if(puedoDeshipotecar){
                    int cantidadPagar = casilla.cancelarHipoteca();
                    jugadorActual.modificarSaldo(-cantidadPagar);
                }
            }
        }
        return puedoDeshipotecar;
    }
    
    public boolean comprarTituloPropiedad(){
        return jugadorActual.comprarTitulo();
    }
    
    public boolean edificarCasa(Casilla casilla){
        boolean puedoEdificar = false;
        
        if(casilla.soyEdificable()){
            boolean sePuedeEdificar = casilla.sePuedeEdificarCasa();
            
            if(sePuedeEdificar){
                puedoEdificar = jugadorActual.puedoEdificarCasa(casilla);
                
                if(puedoEdificar){
                    int costeEdificarCasa = casilla.edificarCasa();
                    jugadorActual.modificarSaldo(-costeEdificarCasa);
                }
            }
        }
        return puedoEdificar;
    }
    
    public boolean edificarHotel(Casilla casilla){
        boolean puedoEdificar = false;
        
        if(casilla.soyEdificable()){
            boolean sePuedeEdificar = casilla.sePuedeEdificarHotel();
            
            if(sePuedeEdificar){
                puedoEdificar = jugadorActual.puedoEdificarHotel(casilla);
                
                if(puedoEdificar){
                    int costeEdificarHotel = casilla.edificarHotel();
                    jugadorActual.modificarSaldo(-costeEdificarHotel);
                }
            }
        }
        return puedoEdificar;
    }

    public Sorpresa getCartaActual() {
        return cartaActual;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }
    
    public boolean hipotecarPropiedad(Casilla casilla){
        boolean puedoHipotecar = false;
        if(casilla.soyEdificable()){
            boolean sePuedeHipotecar = !(casilla.estaHipotecada());
            
            if(sePuedeHipotecar){
                puedoHipotecar = jugadorActual.puedoHipotecar(casilla);
                
                if(puedoHipotecar){
                    int cantidadRecibida = casilla.hipotecar();
                    jugadorActual.modificarSaldo(cantidadRecibida);
                }
            }
        }
        return puedoHipotecar;
    }
    
    private void inicializarJuego(ArrayList<String> nombres){
        if ( nombres.size() >= 2 && nombres.size() <= MAX_JUGADORES ){
            inicializarSorpresas();
            inicializarTablero();
            inicializarJugadores(nombres);
            salidaJugadores();            
        }
    }
    
    public boolean intentarSalirCarcel(MetodoSalirCarcel metodo){
        boolean libre = false;
        if(metodo == MetodoSalirCarcel.TIRANDODADO){
            int valorDado = dado.tirar();
            libre = valorDado > 5;
        }
        else if (metodo == MetodoSalirCarcel.PAGANDOLIBERTAD){
            boolean tengoSaldo = jugadorActual.pagarLibertad(PRECIO_LIBERTAD);
            libre = tengoSaldo;
        }
        
        if(libre)
            jugadorActual.setEncarcelado(false);
        
        return libre;
    }
    
    public boolean jugar(){        
        int valorDado = dado.tirar();
        Casilla casillaPosicion = jugadorActual.getCasillaActual();
        Casilla nuevaCasilla = tablero.obtenerNuevaCasilla(casillaPosicion, valorDado);
        boolean tienePropietario = jugadorActual.actualizarPosicion(nuevaCasilla);
        
        if(!nuevaCasilla.soyEdificable()){
            if(nuevaCasilla.getTipo() == TipoCasilla.JUEZ)
                encarcelarJugador();
            else if (nuevaCasilla.getTipo() == TipoCasilla.SORPRESA){
                cartaActual = mazo.get(0);
                mazo.remove(cartaActual);
                VistaTextualQytetet.Mostrar("La carta sorpresa es: " + cartaActual.toString());
            }
        }
        return tienePropietario;
    }
    
    public TreeMap obtenerRanking(){
        TreeMap<Integer,String> ranking = new TreeMap<>();
        
        for(Jugador jugador: jugadores){
           ranking.put(jugador.obtenerCapital(), jugador.getNombre());
        }
        return ranking;
    }
    
    public ArrayList<Casilla> propiedadesHipotecadasJugador(boolean hipotecadas){
        ArrayList<TituloPropiedad> titulos = jugadorActual.obtenerPropiedadesHipotecadas(hipotecadas);
        ArrayList<Casilla> devolver = new ArrayList();
        for(int i = 0; i < titulos.size(); i++)
            devolver.add(titulos.get(i).getCasilla());
        return devolver;
    }
    
    public void siguienteJugador(){        
        jugadorActual = jugadores.get((jugadores.indexOf(jugadorActual)+1)%jugadores.size());
    }
    
    public boolean venderPropiedad(Casilla casilla){
        boolean puedoVender = false;
        if(casilla.soyEdificable()){
            puedoVender = jugadorActual.puedoVenderPropiedad(casilla);
            
            if(puedoVender){
                jugadorActual.venderPropiedad(casilla);
            }
        }
        return puedoVender;
    }
    
    private void encarcelarJugador(){
        if(!jugadorActual.tengoCartaLibertad()){
            Casilla casillaCarcel = tablero.getCarcel();
            jugadorActual.irACarcel(casillaCarcel);
        }
        else{
            Sorpresa carta = jugadorActual.devolverCartaLibertad();
            mazo.add(carta);
        }
        
    }
    
    private void inicializarSorpresas(){
        mazo.add(new Sorpresa ("¡Ve a la cárcel! Vaya directamente a la caŕcel sin"
        + " pasar por la casilla de salida y sin cobrar los 1.000€", 5, TipoSorpresa.IRACASILLA));

        mazo.add(new Sorpresa ("Un fan anónimo ha pagado tu fianza. Sales "
        + "de la cárcel.", 0, TipoSorpresa.SALIRCARCEL));
        
        mazo.add(new Sorpresa ("Avanza hasta la casilla de salida", 0,
        TipoSorpresa.IRACASILLA));
        
        mazo.add(new Sorpresa ("Vas de viaje a la Calle de Velázquez.", 2,
        TipoSorpresa.IRACASILLA));
        
        mazo.add(new Sorpresa ("Multa por embriaguez. Page 600€", -600,
        TipoSorpresa.PAGARCOBRAR));
        
        mazo.add(new Sorpresa ("Cobra una herencia de 1.000€", 1000,
        TipoSorpresa.PAGARCOBRAR));
        
        mazo.add(new Sorpresa ("Haga reparaciones en todas sus casas."
        + "Page por cada casa 200€", -300, TipoSorpresa.PORCASAHOTEL));
        
        mazo.add(new Sorpresa ("Ganas un premio por el diseño de tus casas."
        + "Recibes 100€ por cada casa.", 200, TipoSorpresa.PORCASAHOTEL));
        
        mazo.add(new Sorpresa ("Hoy es tu cumpleaños. Por cada jugador recibe 150€",
        150, TipoSorpresa.PORJUGADOR));
        
        mazo.add(new Sorpresa ("Invitas a una cena a todos los jugadores."
        + "Das 200€ a cada jugador.", -200, TipoSorpresa.PORJUGADOR));
        
        //Collections.shuffle(mazo);
    }
    
   
    private void inicializarTablero(){
        tablero = new Tablero();
    }
    
    
    private void inicializarJugadores(ArrayList<String> nombres){
        for (int i=0; i<nombres.size(); i++)
            jugadores.add(new Jugador(nombres.get(i), tablero.obtenerCasillaNumero(0)));  
    }
    
    
    public static Qytetet getInstance(ArrayList<String> nombres) {
        if (instance == null){
            instance = new Qytetet(nombres);
            return instance;
        }
        else
            return instance;
    }
    
    private static Qytetet instance = null;

    @Override
    public String toString() {
        return "Qytetet{ MAX_JUGADORES=" + MAX_JUGADORES + ", MAX_CARTAS=" + MAX_CARTAS + ", MAX_CASILLAS=" + MAX_CASILLAS + ", PRECIO_LIBERTAD=" + PRECIO_LIBERTAD + ", SALDO_SALIDA=" + SALDO_SALIDA + "\n\nnombres=" + jugadores + "\n\ncartaActual=" + cartaActual + ", jugadorActual="+ jugadorActual + "\n\nmazo=" + mazo + "\n\ntablero=" + tablero + " } \n";
    }
    
    private void salidaJugadores(){
        for(int i = 0; i < jugadores.size(); i++){
            jugadores.get(i).modificarSaldo(7500 - jugadores.get(i).getSaldo());
            jugadores.get(i).actualizarPosicion(tablero.obtenerCasillaNumero(0));
        }
        jugadorActual = jugadores.get((int)(Math.random() * jugadores.size()));
    }
}
