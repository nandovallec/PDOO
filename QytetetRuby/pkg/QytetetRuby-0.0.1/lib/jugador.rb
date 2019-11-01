# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

module ModeloQytetet
  
  
  class Jugador
        
    def initialize(nombre, casillaActual)
      @encarcelado = false
      @nombre = nombre
      @saldo = 7500 
      @casillaActual = casillaActual
      @cartaLibertad
      @propiedades = []
    end
    
    
    public
    attr_reader :encarcelado, :saldo

    attr_writer :cartaLibertad, :encarcelado

    attr_accessor :casillaActual, :propiedades
    
    def tengo_propiedades()
      @propiedades.size() != 0
    end
    
    
    def actualizar_posicion(casilla)
      if casilla.numero_casilla < @casillaActual.numero_casilla
        modificar_saldo(Qytetet.get_saldo_salida)
      end
      
      tengo_propietario = false
      @casillaActual = casilla
      
      if casilla.soy_edificable
        tengo_propietario = casilla.tengo_propietario
        
        if tengo_propietario
          @encarcelado = casilla.propietario_encarcelado()
          
          if not @encarcelado
            coste_alquiler = casilla.cobrar_alquiler
            modificar_saldo(-coste_alquiler)
          end
        end
        
      else
        
        if casilla.tipo == TipoCasilla::IMPUESTO
          modificar_saldo(-200)
        end
        
      end
      return tengo_propietario
    end

    
    def comprar_titulo()
      puedo_comprar = false
      
      if @casillaActual.soy_edificable
        tengo_propietario = @casillaActual.tengo_propietario
        
        if not tengo_propietario
          coste_compra = @casillaActual.coste
          
          if coste_compra < @saldo
            titulo = @casillaActual.asignar_propietario(self)
            @propiedades << titulo
            modificar_saldo(-coste_compra)
            puedo_comprar = true
          end
        end
      end
      
      return puedo_comprar
    end

    
    def devolver_carta_libertad()
      devolver = @cartaLibertad
      @cartaLibertad = nil
      return devolver
    end

    
    def ir_a_carcel(casilla)
      @casillaActual = casilla
      @encarcelado = true
    end
    
    
    def modificar_saldo(cantidad)
      @saldo = @saldo + cantidad
    end
    
    
    def obtener_capital()
      total = @saldo
      
      for propiedad in @propiedades
        total = total + propiedad.casilla.coste + propiedad.precioEdificar * 
        (propiedad.casilla.num_casas + propiedad.casilla.num_hoteles)
        
        if propiedad.hipotecada
          total = total - propiedad.hipotecaBase
        end
      end
      
      return total
    end
    
    
    def obtener_propiedades_hipotecadas(hipotecada)
      titulos = []
      @propiedades.each { 
        |titulo|
        
        if (titulo.hipotecada == hipotecada)
          titulos << titulo
        end
      }
      return titulos
    end
    
 
    def pagar_cobrar_por_casa_y_hotel(cantidad)
      numero_total = cuantas_casas_hoteles_tengo
      modificar_saldo(numero_total*cantidad)
    end
    
    
    def pagar_libertad(cantidad)
      tengo_saldo = tengo_saldo(cantidad)
      
      if tengo_saldo
        modificar_saldo(-cantidad)
      end
      
      return tengo_saldo
    end
    
    
    def puedo_edificar_casa(casilla)
      es_mia = es_de_mi_propiedad(casilla)
      
      if es_mia
        coste_edificar_casa = casilla.titulo.precioEdificar
        tengo_saldo = tengo_saldo(coste_edificar_casa)
        return (tengo_saldo and es_mia)
      end
      
      return false
    end
    
    
    def puedo_edificar_hotel(casilla)
      es_mia = es_de_mi_propiedad(casilla)
      
      if es_mia
        coste_edificar_hotel = casilla.titulo.precioEdificar
        tengo_saldo = tengo_saldo(coste_edificar_hotel)
        casas_suficientes = (casilla.num_casas == 4)
        return (tengo_saldo and casas_suficientes and es_mia)
      end
      
      return false
    end
    
    
    def puedo_hipotecar(casilla)
      es_mia = es_de_mi_propiedad(casilla)
      return es_mia
    end
    
    
    def puedo_pagar_hipoteca(casilla)
      es_mia = es_de_mi_propiedad(casilla)
      return es_mia
    end
    
    
    def puedo_vender_propiedad(casilla)
      return (es_de_mi_propiedad(casilla) and !(casilla.titulo.hipotecada))
    end
    
    
    def tengo_carta_libertad()
      @cartaLibertad != nil
    end
    
    
    def vender_propiedad(casilla)
      precio_venta = casilla.vender_titulo
      modificar_saldo(precio_venta)
      eliminar_de_mis_propiedades(casilla)
    end
    
    
    private
    def cuantas_casas_hoteles_tengo()
      total = 0
      @propiedades.each{
        |propiedad|
        total = total + propiedad.num_casas + propiedad.num_hoteles
      }
      return total
    end
    
    
    def eliminar_de_mis_propiedades(casilla)
      @propiedades.delete(casilla.titulo)
      casilla.titulo.propietario = nil
    end
    
    
    def es_de_mi_propiedad(casilla)
      @propiedades.each{
        |propiedad|
        if propiedad.casilla == casilla
          return true
        end
      }
      return false
    end
    
    
    def tengo_saldo(cantidad)
      @saldo >= cantidad
    end
    
    
    public
    def to_s()
      "Jugador{ nombre: #{@nombre}, saldo: #{@saldo}, encarcelado: #{@encarcelado},
      cartaLibertad: #{@cartaLibertad}, casillaActual: #{@casillaActual} }\n"
    end
    
  end
end
