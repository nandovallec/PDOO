/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

/**
 *
 * @author jose26398
 */
public class Casilla {
    private int numeroCasilla;
    private int coste;
    private int numHoteles;
    private int numCasas;
    private TipoCasilla tipo;
    private TituloPropiedad titulo;

    public Casilla(int numeroCasilla, TipoCasilla tipo) {
        this.numeroCasilla = numeroCasilla;
        this.tipo = tipo;
    }
    
    public Casilla(int numeroCasilla, TipoCasilla tipo, int coste) {
        this.coste = coste;
        this.numeroCasilla = numeroCasilla;
        this.tipo = tipo;
    }

    public Casilla(int numeroCasilla, int coste, TituloPropiedad titulo) {
        this.numeroCasilla = numeroCasilla;
        this.coste = coste;
        this.numHoteles = 0;
        this.numCasas = 0;
        this.tipo = TipoCasilla.CALLE;
        this.titulo = titulo;
        titulo.setCasilla(this);
    }

    public int getNumeroCasilla() {
        return numeroCasilla;
    }

    int getCoste() {
        return coste;
    }

    public int getNumHoteles() {
        return numHoteles;
    }

    public int getNumCasas() {
        return numCasas;
    }

    public TipoCasilla getTipo() {
        return tipo;
    }

    public TituloPropiedad getTitulo() {
        return titulo;
    }

    void setNumHoteles(int numHoteles) {
        this.numHoteles = numHoteles;
    }

    void setNumCasas(int numCasas) {
        this.numCasas = numCasas;
    }

    private void setTitulo(TituloPropiedad titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        if(tipo == TipoCasilla.CALLE)
            return "Casilla{" + "numeroCasilla=" + numeroCasilla + ", coste=" 
                    + coste + ", numHoteles=" + numHoteles + ", numCasas=" 
                    + numCasas + ", tipo=" + tipo + ", titulo=" + titulo + "}";
        else
            return "Casilla{" + "numeroCasilla=" + numeroCasilla + ", tipo=" + tipo + "}";
    }
    
    TituloPropiedad asignarPropietario(Jugador jugador){
        titulo.setPropietario(jugador);
        return titulo;
    }
    
    int calcularValorHipoteca(){
        int hipotecaBase = titulo.getHipotecaBase();
        int cantidadRecibida = (int) (hipotecaBase + (numCasas * 0.5 * hipotecaBase + numHoteles * hipotecaBase));
        return cantidadRecibida;
    }
    
    int cancelarHipoteca(){
        titulo.setHipotecada(false);
        int cantidadPagar = (int) (calcularValorHipoteca() *1.10);
        return cantidadPagar;    
    }
    
    int cobrarAlquiler(){
        int costeAlquilerBase = titulo.getAlquilerBase();
        int costeAlquiler = costeAlquilerBase + (int)((numCasas * 0.5 + numHoteles * 2));
        
        titulo.cobrarAlquiler(costeAlquiler);
        return costeAlquiler;
    }
    
    int edificarCasa() {
        setNumCasas(numCasas+1);
        int costeEdificarCasa = titulo.getPrecioEdificar();
        return costeEdificarCasa;
    }
    
    int edificarHotel(){
        setNumHoteles(numHoteles+1);
        setNumCasas(0);
        int costeEdificarHotel = titulo.getPrecioEdificar();
        return costeEdificarHotel;    
    }
    
    boolean estaHipotecada(){
        if(tipo != TipoCasilla.CALLE)
            return false;
        else{
            return titulo.getHipotecada();
        }
    }
    
    int getCosteHipoteca(){
        throw new UnsupportedOperationException("Sin implementar");
    }
    
    int getPrecioEdificar(){
        return titulo.getPrecioEdificar();
    }
    
    int hipotecar(){
        titulo.setHipotecada(true);
        int cantidadRecibida = calcularValorHipoteca();
        return cantidadRecibida;
    }
    
    int precioTotalComprar(){
        throw new UnsupportedOperationException("Sin implementar");
    }
    
    boolean propietarioEncarcelado(){
        boolean encarcelado = titulo.propietarioEcarcelado();
        return encarcelado;
    }
    
    boolean sePuedeEdificarCasa(){
        return numCasas < 4;
    }
    
    boolean sePuedeEdificarHotel(){
        return numHoteles < 4;
    }
    
    boolean soyEdificable(){
        return tipo == TipoCasilla.CALLE;
    }
    
    public boolean tengoPropietario(){
        return titulo.tengoPropietario();
    }
    
    int venderTitulo(){
        int precioCompra = (int)(coste + (numCasas+numHoteles) * titulo.getPrecioEdificar());
        int precioVenta = (int) (precioCompra + titulo.getFactorRevalorizacion() * precioCompra);
        titulo.setPropietario(null);
        setNumCasas(0);
        setNumHoteles(0);
        return precioVenta;
    }
    
    private void asignarTituloPropiedad(){
        throw new UnsupportedOperationException("Sin implementar");
    }
    
}
