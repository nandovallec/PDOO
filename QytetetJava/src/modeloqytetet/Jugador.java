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
public class Jugador {
    private boolean encarcelado = false;
    private String nombre;
    private int saldo = 7500;
    private Casilla casillaActual;
    private ArrayList<TituloPropiedad> propiedades = new ArrayList();
    private Sorpresa cartaLibertad = null;

    public Jugador(String nombre, Casilla casillaActual) {
        this.nombre = nombre;
        this.casillaActual = casillaActual;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<TituloPropiedad> getPropiedades() {
        return propiedades;
    }
    
    public int getSaldo() {
        return saldo;
    }
    
    public Casilla getCasillaActual(){
        return casillaActual;        
    }
    
    public boolean getEncarcelado(){
        return encarcelado;
    }
    
    public boolean tengoPropiedades(){
        return !(propiedades.isEmpty());
    }
    
    boolean actualizarPosicion(Casilla casilla){
        if ( casilla.getNumeroCasilla() < casillaActual.getNumeroCasilla() )
            modificarSaldo(Qytetet.SALDO_SALIDA);
        
        boolean tengoPropietario = false;
        setCasillaActual(casilla);
        
        if ( casilla.soyEdificable() ){
            tengoPropietario = casilla.tengoPropietario();
        
            if (tengoPropietario){
                boolean prop_encarcelado = casilla.propietarioEncarcelado();
                boolean esta_hipotecada = casilla.getTitulo().getHipotecada();
            
                if (!prop_encarcelado && !esta_hipotecada){ 
                    int costeAlquiler = casilla.cobrarAlquiler();
                    modificarSaldo( -costeAlquiler );
                }
            }
        }else{
            if (casilla.getTipo() == TipoCasilla.IMPUESTO){
                int coste = casilla.getCoste();
                modificarSaldo( -coste );
            }
            
        }       
        return tengoPropietario;      
        
    }
    
    boolean comprarTitulo(){
        boolean puedoComprar = false;
        
        if ( casillaActual.soyEdificable() ){
            boolean tengoPropietario = casillaActual.tengoPropietario();
            
            if ( !tengoPropietario ){
                int costeCompra = casillaActual.getCoste();
                
                if ( costeCompra <= saldo){
                    TituloPropiedad titulo = casillaActual.asignarPropietario(this);
                    propiedades.add(titulo);
                    modificarSaldo( -costeCompra );
                    puedoComprar = true;
                }
            }
        }
        
        return puedoComprar;
    }
    
    Sorpresa devolverCartaLibertad(){
        Sorpresa devolver = cartaLibertad;
        cartaLibertad = null;
        return devolver;
    }
    
    void irACarcel(Casilla casilla){
        setCasillaActual(casilla);
        setEncarcelado(true);
    }
    
    void modificarSaldo(int cantidad){
        saldo += cantidad;
    }
    
    int obtenerCapital(){
        int total = saldo;
        
        for(int i = 0; i < propiedades.size(); i++){
            total = total + propiedades.get(i).getCasilla().getCoste() + 
                    propiedades.get(i).getPrecioEdificar() * (propiedades.get(i).getCasilla().getNumCasas() + 
                    propiedades.get(i).getCasilla().getNumHoteles());
            
            if (propiedades.get(i).getHipotecada())
                total = total - propiedades.get(i).getHipotecaBase();
        }
        return total;
    }
    
    public ArrayList<TituloPropiedad> obtenerPropiedadesHipotecadas(boolean hipotecada){
        ArrayList<TituloPropiedad> devolver = new ArrayList();
        for (int i = 0; i < propiedades.size(); i++)
            if(propiedades.get(i).getHipotecada() == hipotecada)
                devolver.add(propiedades.get(i));
        return devolver;
    }
    
    void pagarCobrarPorCasaYHotel(int cantidad){
        int numeroTotal = cuantasCasasHotelesTengo();
        modificarSaldo(numeroTotal*cantidad);
    }
    
    boolean pagarLibertad(int cantidad){
        boolean tengoSaldo = tengoSaldo(cantidad);
        if(tengoSaldo)
            modificarSaldo(-cantidad);
        
        return tengoSaldo;
    }
    
    boolean puedoEdificarCasa(Casilla casilla){
        boolean esMia = esDeMipropiedad(casilla);
        if (esMia){
            int costeEdificarCasa = casilla.getPrecioEdificar();
            boolean tengoSaldo = tengoSaldo(costeEdificarCasa);
            return esMia && tengoSaldo;
        }
        return false;
    }
    
    boolean puedoEdificarHotel(Casilla casilla){
        boolean esMia = esDeMipropiedad(casilla);
        if (esMia){
            int costeEdificarHotel = casilla.getPrecioEdificar();
            boolean tengoSaldo = tengoSaldo(costeEdificarHotel);
            boolean casasSuficientes = casilla.getNumCasas() == 4;
            return esMia && tengoSaldo && casasSuficientes;
        }
        return false;
    }
    
    boolean puedoHipotecar(Casilla casilla){
        boolean esMia = esDeMipropiedad(casilla);
        return esMia;
    }
    
    boolean puedoPagarHipoteca(Casilla casilla){
        return tengoSaldo(casilla.cancelarHipoteca());
    }
    
    boolean puedoVenderPropiedad(Casilla casilla){
        return esDeMipropiedad(casilla) && !(casilla.estaHipotecada());
    }

    void setEncarcelado(boolean encarcelado) {
        this.encarcelado = encarcelado;
    }

    void setCasillaActual(Casilla casilla) {
        this.casillaActual = casilla;
    }

    void setCartaLibertad(Sorpresa cartaLibertad) {
        if (cartaLibertad.getTipo()==TipoSorpresa.SALIRCARCEL){
            this.cartaLibertad = cartaLibertad;
        }
    }
    
    boolean tengoCartaLibertad(){
        return cartaLibertad != null;
    }
    
    void venderPropiedad(Casilla casilla){
        int precioVenta = casilla.venderTitulo();
        modificarSaldo(precioVenta);
        eliminarDeMisPropiedades(casilla);
    }
    
    private int cuantasCasasHotelesTengo(){
        int total = 0;
        for (int i = 0; i < propiedades.size(); i++)
            total = total + propiedades.get(i).getCasilla().getNumCasas() + propiedades.get(i).getCasilla().getNumHoteles();
        return total;       
    }
    
    private void eliminarDeMisPropiedades(Casilla casilla){
        if(!(propiedades.remove(casilla.getTitulo())))
            throw new UnsupportedOperationException("No eres propietario de esa casilla.");
        casilla.getTitulo().setPropietario(null);
    }
    
    private boolean esDeMipropiedad(Casilla casilla){
        for(int i = 0; i < propiedades.size(); i++)
            if (propiedades.get(i).getCasilla() == casilla)
                return true;
        
        return false;
    }
    
    private boolean tengoSaldo(int cantidad){
        return saldo >= cantidad;
    }

    @Override
    public String toString() {
        return "Jugador{" + "encarcelado=" + encarcelado + ", nombre=" + nombre + ", saldo=" + saldo 
                + ", casillaActual=" + casillaActual + ", cartaLibertad=" + cartaLibertad + '}';
    }
    
    
}
