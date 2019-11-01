#encoding: utf-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

require "singleton"

module ModeloQytetet
  
  class Qytetet

    include Singleton
   
    @@MAX_JUGADORES = 4
    @@MAX_CARTAS = 10
    @@MAX_CASILLAS = 20    
    @@PRECIO_LIBERTAD = 200
    @@SALDO_SALIDA = 1000
    @@nombres = [];

    
    private
    def initialize()
      @cartaActual
      @mazo = []
      @jugadorActual
      @tablero
      @jugadores = []
      @dado = Dado.instance
    
      inicializar_juego(@@nombres);
      
    end

    
    public
    attr_reader :cartaActual, :jugadorActual, :mazo, :jugadores

    
    def self.asignar_nombres(nombres)
      @@nombres = nombres
    end
    
    
    def aplicar_sorpresa()
      tiene_propietario = false;

      if @cartaActual.tipo == TipoSorpresa::PAGARCOBRAR
        @jugadorActual.modificar_saldo( @cartaActual.valor )
      
      elsif @cartaActual.tipo == TipoSorpresa::IRACASILLA
        es_carcel = @tablero.es_casilla_carcel(@cartaActual.valor)
        
        if es_carcel == true
          encarcelar_jugador
        
        else
          nuevaCasilla = @tablero.obtener_casilla_numero(@cartaActual.valor)
          tiene_propietario = @jugadorActual.actualizar_posicion(nuevaCasilla)
        end
        
      elsif @cartaActual.tipo == TipoSorpresa::PORCASAHOTEL
        @jugadorActual.pagar_cobrar_por_casa_y_hotel(@cartaActual.valor)
      
      elsif @cartaActual.tipo == TipoSorpresa::PORJUGADOR
        for jugador in @jugadores
          if jugador != @jugadorActual
            jugador.modificar_saldo(@cartaActual.valor)
            @jugadorActual.modificar_saldo(-(@cartaActual.valor))
          end
        end
      end
      
      if @cartaActual.tipo == TipoSorpresa::SALIRCARCEL
        @jugadorActual.cartaLibertad = @cartaActual
      else
        @mazo << @cartaActual 
      end
      
      return tiene_propietario
    end

    
    def self.get_saldo_salida
      @@SALDO_SALIDA
    end

    
    def cancelar_hipoteca(casilla)
      puedo_deshipotecar = false
      
      if casilla.soy_edificable()
        se_puede_deshipotecar = (casilla.esta_hipotecada())
        
        if se_puede_deshipotecar
          puedo_deshipotecar = @jugadorActual.puedo_pagar_hipoteca(casilla)
          
          if puedo_deshipotecar
            cantidad_pagar = casilla.cancelar_hipoteca()
            @jugadorActual.modificar_saldo(-cantidad_pagar)
          end
        end
      end
      
      return puedo_deshipotecar
    end


    def comprar_titulo_propiedad()
      return @jugadorActual.comprar_titulo()
    end


    def edificar_casa(casilla)
      puedo_edificar = false
      
      if casilla.soy_edificable() == true
        se_puede_edificar = casilla.se_puede_edificar_casa()
        
        if se_puede_edificar == true
          puedo_edificar = @jugadorActual.puedo_edificar_casa(casilla)
          
          if puedo_edificar == true
            coste_edificar_casa = casilla.edificar_casa()
            @jugadorActual.modificar_saldo(-coste_edificar_casa)
          end
        end
      end
      
      return puedo_edificar
    end


    def edificar_hotel(casilla)
      puedo_edificar = false
      
      if casilla.soy_edificable() == true
        se_puede_edificar = casilla.se_puede_edificar_hotel()
        
        if se_puede_edificar == true
          puedo_edificar = @jugadorActual.puedo_edificar_hotel(casilla)
          
          if puedo_edificar == true
            coste_edificar_hotel = casilla.edificar_hotel()
            @jugadorActual.modificar_saldo(-coste_edificar_hotel)
          end
        end
      end
      
      return puedo_edificar
    end


    def hipotecar_propiedad(casilla)
      puedo_hipotecar = false
      
      if casilla.soy_edificable()
        se_puede_hipotecar = !(casilla.esta_hipotecada())
        
        if se_puede_hipotecar
          puedo_hipotecar = @jugadorActual.puedo_hipotecar(casilla)
          
          if puedo_hipotecar
            cantidad_recibida = casilla.hipotecar()
            @jugadorActual.modificar_saldo(cantidad_recibida)
          end
        end
      end
      
      return puedo_hipotecar
    end


    def inicializar_juego(nombres)
      if nombres.size >= 2 and nombres.size <= @@MAX_JUGADORES
        inicializar_cartas_sorpresa
        inicializar_tablero
        inicializar_jugadores(nombres)
        salida_jugadores
      end
    end


    def intentar_salir_carcel(metodo)
      libre = false
      
      if metodo == MetodoSalirCarcel::TIRANDODADO
        valor_dado = @dado.tirar()
        libre = (valor_dado > 5)
         
      elsif metodo == MetodoSalirCarcel::PAGANDOLIBERTAD
        tengo_saldo = @jugadorActual.pagar_libertad(@@PRECIO_LIBERTAD)
        libre = tengo_saldo
      end
      
      if libre == true
        @jugadorActual.encarcelado = false
      end
      
      return libre
    end


    def jugar()
      valor_dado = @dado.tirar()
      gets.chomp
      
      casilla_posicion = @jugadorActual.casillaActual
      nueva_casilla = @tablero.obtener_nueva_casilla(casilla_posicion, valor_dado)
      tiene_propietario = @jugadorActual.actualizar_posicion(nueva_casilla)
      
      if not nueva_casilla.soy_edificable()
        
        if nueva_casilla.tipo == TipoCasilla::JUEZ
          encarcelar_jugador()
          print("\nEl JUEZ te ha mandado a la carcel")
        elsif nueva_casilla.tipo == TipoCasilla::SORPRESA
          @cartaActual = @mazo[0]
          @mazo.delete_at(0)
        end
        
      end
      
      return tiene_propietario
    end


    def obtener_ranking()
      ranking = {}
      jugadores = @jugadores.sort{|j1,j2| j2.obtener_capital - j1.obtener_capital}

      for jugador in jugadores
        capital = jugador.obtener_capital
        ranking[jugador.nombre] = capital
      end
      
      return ranking
    end


    def propiedades_hipotecadas_jugador(hipotecadas)
      devolver = []
      @jugadorActual.obtener_propiedades_hipotecadas(hipotecadas).each{
        |aniadir|
        devolver << aniadir.casilla
      }
      return devolver
    end


    def siguiente_jugador()
      @jugadorActual = @jugadores[(@jugadores.index(@jugadorActual )    + 1) % @jugadores.size()]
      return @jugadorActual
    end


    def vender_propiedad(casilla)
      puedo_vender = false
      
      if (casilla.soy_edificable())
        puedo_vender = @jugadorActual.puedo_vender_propiedad(casilla)
        
        if puedo_vender
          @jugadorActual.vender_propiedad(casilla)
        end
      end
      
      return puedo_vender
    end
    
    
    private
    def encarcelar_jugador()
      
      if not @jugadorActual.tengo_carta_libertad
        casilla_carcel = @tablero.carcel;
        @jugadorActual.ir_a_carcel(casilla_carcel)
        
      else
        carta = @jugadorActual.devolver_carta_libertad
        @mazo << carta
      end

    end


    def inicializar_cartas_sorpresa()
      @mazo << Sorpresa.new("Ve a la carcel! Vaya directamente a la carcel sin
      pasar por la casilla de salida y sin cobrar los 1.000 euros", 5, TipoSorpresa::IRACASILLA)

      @mazo << Sorpresa.new("Un fan anonimo ha pagado tu fianza. Sales
      de la cárcel.", 0, TipoSorpresa::SALIRCARCEL)

      @mazo << Sorpresa.new("Avanza hasta la casilla de salida", 0,
      TipoSorpresa::IRACASILLA)

      @mazo << Sorpresa.new("Vas de viaje a la Calle de Velázquez", 2, 
      TipoSorpresa::IRACASILLA)

      @mazo << Sorpresa.new("Multa por embriaguez. Page 600€", -600,
      TipoSorpresa::PAGARCOBRAR)

      @mazo << Sorpresa.new("Cobra una herencia de 1.000€", 1000,
      TipoSorpresa::PAGARCOBRAR)

      @mazo << Sorpresa.new("Haga reparaciones en todas sus casas.
      Page por cada casa 200€", -300, TipoSorpresa::PORCASAHOTEL)

      @mazo << Sorpresa.new("Ganas un premio por el diseño de tus casas.
      Recibes 100€ por cada casa.", 200, TipoSorpresa::PORCASAHOTEL)

      @mazo << Sorpresa.new("Hoy es tu cumpleaños. Por cada jugador recibe 150€",
      -150, TipoSorpresa::PORJUGADOR)

      @mazo << Sorpresa.new("Invitas a una cena a todos los jugadores.
      Das 200€ a cada jugador.", 200, TipoSorpresa::PORJUGADOR)

      @mazo.shuffle!
    end


    def inicializar_jugadores(nombres)
      for i in nombres
        @jugadores << Jugador.new(i, @tablero.obtener_casilla_numero(0))
      end
    end


    def inicializar_tablero()
      @tablero = Tablero.new()
    end


    def salida_jugadores()
      @jugadores.each{
        |jug|
        jug.modificar_saldo(7500 - jug.saldo)
        jug.actualizar_posicion(@tablero.obtener_casilla_numero(0))
      }
      @jugadorActual = @jugadores[rand(@jugadores.size())]
    end
    
    
    public
    def to_s()
      "Qytetet{ MAX_JUGADORES: #{@@MAX_JUGADORES}, MAX_CARTAS: #{@@MAX_CARTAS}, MAX_CASILLAS: #{@@MAX_CASILLAS},
      PRECIO_LIBERTAD: #{@@PRECIO_LIBERTAD}, SALDO_SALIDA: #{@@SALDO_SALIDA} \n
      jugadores #{@jugadores} \n cartaActual: #{@cartaActual}, jugadorActual: #{@jugadorActual} \n
      mazo: #{@mazo} \n tablero: #{@tablero} } \n"
    end

  end
  
end