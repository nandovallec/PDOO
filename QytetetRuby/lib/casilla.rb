#encoding: utf-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

module ModeloQytetet
  
  class Casilla
    
    def initialize(num_casilla, tip, cost, tit)
      @coste = cost
      @num_hoteles = 0
      @num_casas = 0
      @titulo = tit
      @tipo = tip
      @numero_casilla = num_casilla      
      
      if @titulo != nil
        @titulo.casilla = self
      end
      
    end
    
    
    def self.new1(num_casilla, tip)
      new(num_casilla, tip, nil, nil)
    end
    
    
    def self.new2(num_casilla, tip, cost, tit)
      new(num_casilla, tip, cost, tit)
    end
    
    
    public
    attr_reader :tipo, :titulo, :coste, :numero_casilla
    
    attr_accessor :num_hoteles, :num_casas
    
    
    
    def asignar_propietario(jugador)
      @titulo.propietario = jugador
      return @titulo
    end
    
    
    def calcular_valor_hipoteca()
      hipoteca_base = @titulo.hipotecaBase
      cantidad_recibida = hipoteca_base+ (@num_casas * 0.5 * hipoteca_base + @num_hoteles * hipoteca_base)
      return cantidad_recibida
    end
    
    
    def cancelar_hipoteca()
      @titulo.hipotecada = false;
      cantidad_pagar = calcular_valor_hipoteca * 1.10
      return cantidad_pagar
    end
    
    
    def cobrar_alquiler()
      coste_alquiler_base = @titulo.alquilerBase
      coste_alquiler = coste_alquiler_base + (@num_casas * 0.5 + @num_hoteles * 2)
      
      @titulo.cobrar_alquiler(coste_alquiler)
      return coste_alquiler
    end
    
    
    def edificar_casa()
      @num_casas += 1
      coste_edificar_casa = @titulo.precioEdificar
      return coste_edificar_casa
    end
    
    
    def edificar_hotel()      
      @num_hoteles += 1
      @num_casas = 0
      coste_edificar_hotel = @titulo.precioEdificar
      return coste_edificar_hotel
    end
    
    
    def esta_hipotecada()
      if @tipo != TipoCasilla::CALLE
        return false
      else
        return @titulo.hipotecada
      end
    end
    
    
    def get_coste_hipoteca()
      
    end
    
    
    def get_precio_edficar()
      return @titulo.precioEdificar
    end
    
    
    def hipotecar()
      @titulo.hipotecada = true
      cantidad_recibida = calcular_valor_hipoteca
      return cantidad_recibida
    end
    
    
    def precio_total_comprar()
      
    end
    
    
    def propietario_encarcelado()
      encarcelado = @titulo.propietario_encarcelado
      return encarcelado
    end
    
    
    def se_puede_edificar_casa()
      return @num_casas < 4
    end
    
    
    def se_puede_edificar_hotel()
      return @num_hoteles < 4
    end
    
    
    def soy_edificable()
      @tipo == TipoCasilla::CALLE
    end
    
    
    def tengo_propietario()
      return @titulo.tengo_propietario
    end
    
    
    def vender_titulo()
      precio_compra = @coste + (@num_casas + @num_hoteles) * @titulo.precioEdificar
      precio_venta = precio_compra + @titulo.factorRevalorizacion * precio_compra
      @titulo.propietario = nil
      @num_casas = 0
      @num_hoteles = 0
      return precio_venta
    end
    
    
    def to_s()
      
      if (@tipo == TipoCasilla::CALLE)
        "Casilla{ numero_casilla: #{@numero_casilla}, coste: #{@coste}, num_hoteles: #{@num_hoteles}, num_casas: #{@num_casas}, tipo: #{@tipo},
        titulo: #{@titulo} } \n"      
      else
        "Casilla{ numero_casilla: #{@numero_casilla}, tipo: #{@tipo} }\n"
      end  
      
    end
    
    
    private
    attr_writer :titulo
    
    def asignar_titulo_propiedad()
      
    end
        
      
  end

end
