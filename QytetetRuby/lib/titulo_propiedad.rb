#encoding: utf-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

module ModeloQytetet
  
  class TituloPropiedad
    
    def initialize(nom, alq_base, fact_rev, hip_base, prec_ed)
      @nombre = nom
      @hipotecada = false
      @alquilerBase = alq_base
      @factorRevalorizacion = fact_rev
      @hipotecaBase = hip_base
      @precioEdificar = prec_ed     
      @casilla
      @propietario
    end
    
    
    public
    attr_reader :nombre, :alquilerBase, :factorRevalorizacion,
                        :hipotecaBase, :precioEdificar
              
    attr_accessor :hipotecada, :casilla, :propietario
    
    
    def cobrar_alquiler(coste)
      @propietario.modificar_saldo(coste)
    end
    
    
    def propietario_encarcelado()
      return @propietario.encarcelado
    end
    
    
    def tengo_propietario()
      @propietario != nil
    end
    
    
    def to_s()
      "TituloPropiedad{ nombre: #{@nombre}, hipotecada: #{@hipotecada},  alquilerBase: #{@alquilerBase},
      factorRevalorizacion: #{@factorRevalorizacion}, hipotecaBase: #{@hipotecaBase}, precioEdificar: #{@precioEdificar} }\n"
    end
    
  end

end