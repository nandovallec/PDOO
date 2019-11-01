#encoding: utf-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

require_relative "casilla"
require_relative "dado"
require_relative "jugador"
require_relative "metodo_salir_carcel"
require_relative "qytetet"
require_relative "sorpresa"
require_relative "tablero"
require_relative "tipo_casilla"
require_relative "tipo_sorpresa"
require_relative "titulo_propiedad"
require_relative "vista_textual_qytetet"

module InterfazVisualQytetet

  class ControladorQytetet
    
    include ModeloQytetet

    
    def initialize()
      @juego
      @jugador
      @casilla
      @vista = VistaTextualQytetet.new
    end
    
    
    
    def inicializacion_juego()
      nombres = @vista.obtener_nombre_jugadores()      
      Qytetet.asignar_nombres(nombres)
      
      @juego = Qytetet.instance
      @jugador = @juego.jugadorActual
      @casilla = @jugador.casillaActual
      
      @vista.mostrar( "\n-------Pulse tecla cualquiera para continuar-------")
      gets.chomp
      
    end
    
    
    
    def desarrollo_juego()
      bancarrota = false
      begin

          @vista.mostrar( "\nTURNO DEL JUGADOR: #{@jugador} ")

          if @jugador.encarcelado
            @vista.mostrar("Estas encarcelado, selecciona como quieres salir\n")
            metodo = @vista.menu_salir_carcel()
            if (metodo == 0)
              libre = @juego.intentar_salir_carcel(MetodoSalirCarcel::TIRANDODADO)
            else
              libre = @juego.intentar_salir_carcel(MetodoSalirCarcel::PAGANDOLIBERTAD)
            end
            
            
            if libre
              @vista.mostrar("\nEl jugador ha conseguido salir de la carcel")
            else
              @vista.mostrar("\nEl jugador no ha conseguido salir de la carcel")
            end

          end
          
          
          if not @jugador.encarcelado
            no_tiene_propietario = @juego.jugar()
            @casilla = @jugador.casillaActual
            bancarrota = (@jugador.saldo <= 0)            

            if bancarrota
              @vista.mostrar("\nHas caido en bancarrota\n")

            else
              if @jugador.encarcelado
                @vista.mostrar("\nEstas en la carcel\n")

              else

                if @casilla.tipo == TipoCasilla::CALLE
                  @vista.mostrar("\nHas caido en: #{@casilla}")
                  gets.chomp
                    
                  if not @casilla.tengo_propietario
                      if @vista.elegir_quiero_comprar == 1
                        if @juego.comprar_titulo_propiedad()
                          @vista.mostrar("Compra realizada correctamente\n")
                        else
                          @vista.mostrar("No ha podido comprar la calle\n")
                        end
                      end
                  end
                
                elsif @casilla.tipo == TipoCasilla::SORPRESA
                  @vista.mostrar("\n#{@juego.cartaActual}")
                  no_tiene_propietario = @juego.aplicar_sorpresa()
                  
                  if not no_tiene_propietario
                    @casilla = @jugador.casillaActual
                    if (not @jugador.encarcelado and (not bancarrota) and @casilla.tipo == TipoCasilla::CALLE)
                      if @vista.elegir_quiero_comprar == 1
                        if @juego.comprar_titulo_propiedad()
                          @vista.mostrar("Compra realizada correctamente\n")
                        else
                          @vista.mostrar("No ha podido comprar la calle\n")
                        end
                      end
                    end
                  end
                end
                
                bancarrota = (@jugador.saldo <= 0)   
                if ((not @jugador.encarcelado) and (not bancarrota) and (@jugador.tengo_propiedades))                  
                  opcion = @vista.menu_gestion_inmobiliaria
                    
                  while (opcion != 0) and (@jugador.tengo_propiedades)
                    @vista.mostrar("\nLas propiedades del jugador son:")
                    num_propiedad = @vista.menu_elegir_propiedad(@jugador.propiedades)
                    casilla = @jugador.propiedades[num_propiedad].casilla
                    case opcion
                      when 0
                        @vista.mostrar("Has dado el turno al siguiente jugador")
                      when 1
                        @juego.edificar_casa(casilla)
                      when 2
                        @juego.edificar_hotel(casilla)
                      when 3
                        @juego.vender_propiedad(casilla)
                      when 4
                        @juego.hipotecar_propiedad(casilla)
                      when 5
                        @juego.cancelar_hipoteca(casilla)
                    end
                   
                    @vista.mostrar("\nTe quedan #{@jugador.saldo} euros\n")
                    opcion = @vista.menu_gestion_inmobiliaria
                  end  

                end                
              end    
            end
            
          end
     
          gets.chomp
          bancarrota = (@jugador.saldo <= 0) 
          if not bancarrota
            @vista.mostrar("\FINAL DE TURNO DE: #{@jugador}\n\n")
            @juego.siguiente_jugador
            @jugador = @juego.jugadorActual
          end
          
          bancarrota = (@jugador.saldo <= 0) 
          if bancarrota
            @vista.mostrar("El jugador #{@jugador.nombre} esta en bacarrota")
          end

        end until bancarrota
        @vista.mostrar("El juego ha terminado");
        @vista.mostrar("El ranking final es: #{@juego.obtener_ranking}");
      
    end
    
    
    def self.main
      prueba = ControladorQytetet.new
      prueba.inicializacion_juego()
      prueba.desarrollo_juego()
    end
    
    
    
    private
    def elegir_propiedad(propiedades) # lista de propiedades a elegir
        @vista.mostrar("\tCasilla\tTitulo");
        
        listaPropiedades = Array.new
        for prop in propiedades  # crea una lista de strings con numeros y nombres de propiedades
          propString = prop.numeroCasilla.to_s+' '+prop.titulo.nombre; 
          listaPropiedades << propString
        end
        seleccion=@vista.menu_elegir_propiedad(listaPropiedades)  # elige de esa lista del menu
        propiedades.at(seleccion)
    end
    
  end
  
  ControladorQytetet.main
  
end

