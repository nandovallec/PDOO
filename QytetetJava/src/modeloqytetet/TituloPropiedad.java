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
public class TituloPropiedad {
    private String nombre;
    private boolean hipotecada;
    private int alquilerBase;
    private float factorRevalorizacion;
    private int hipotecaBase;
    private int precioEdificar;
    private Casilla casilla;
    private Jugador propietario;

    public TituloPropiedad(String nombre, int alquilerBase, float factorRevalorizacion, int hipotecaBase, int precioEdificar) {
        this.nombre = nombre;
        propietario = null;
        this.hipotecada =  false;
        this.alquilerBase = alquilerBase;
        this.factorRevalorizacion = factorRevalorizacion;
        this.hipotecaBase = hipotecaBase;
        this.precioEdificar = precioEdificar;
    }

    void cobrarAlquiler(int coste){
        propietario.modificarSaldo(coste);
    }
    
    public String getNombre() {
        return nombre;
    }

    public Casilla getCasilla() {
        return casilla;
    } 

    boolean getHipotecada() {
        return hipotecada;
    }

    int getAlquilerBase() {
        return alquilerBase;
    }

    float getFactorRevalorizacion() {
        return factorRevalorizacion;
    }

    int getHipotecaBase() {
        return hipotecaBase;
    }

    int getPrecioEdificar() {
        return precioEdificar;
    }
    
    boolean propietarioEcarcelado(){
        return propietario.getEncarcelado();
    }

    void setHipotecada(boolean hipotecada) {
        this.hipotecada = hipotecada;
    }
    
    void setCasilla(Casilla casilla){
        this.casilla = casilla;
    }
    
    void setPropietario(Jugador propietario){
        this.propietario = propietario;
    }
    
    boolean tengoPropietario(){
        return propietario != null;
    }

    @Override
    public String toString() {
        if (propietario == null)
            return "TituloPropiedad{" + "nombre=" + nombre + ", hipotecada=" + hipotecada + ", alquilerBase=" + alquilerBase + ", factorRevalorizacion=" + factorRevalorizacion + ", hipotecaBase=" + hipotecaBase + ", precioEdificar=" + precioEdificar + ", propietario=" + "NADIE" + '}';
        else
            return "TituloPropiedad{" + "nombre=" + nombre + ", hipotecada=" + hipotecada + ", alquilerBase=" + alquilerBase + ", factorRevalorizacion=" + factorRevalorizacion + ", hipotecaBase=" + hipotecaBase + ", precioEdificar=" + precioEdificar + ", propietario=" + propietario.getNombre() + '}';
    }
    
   
       
    
    
    
}
