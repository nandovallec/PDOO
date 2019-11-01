#encoding: utf-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

module ModeloQytetet
  
  class Tablero
        
    def initialize()
      @casillas = []
      @carcel
      inicializar()
    end
    
    public
    attr_reader :carcel
    
    
    def es_casilla_carcel(numero_casilla)
      return @carcel.numero_casilla == numero_casilla
    end
    
    
    def obtener_casilla_numero(numero_casilla)
      @casillas[numero_casilla]
    end
    
    
    def obtener_nueva_casilla(casilla, desplazamiento)
      @casillas[(((casilla.numero_casilla) + desplazamiento)%(@casillas.size()))]
    end
    
    
    def to_s()
      "Tablero{ casillas: #{@casillas} \n carcel: #{@carcel}"      
    end
    
    private
    def inicializar()
      @casillas << Casilla.new1(0, TipoCasilla::SALIDA)
      @casillas << Casilla.new2(1, TipoCasilla::CALLE, 600, TituloPropiedad.new("Avenida Calvo Sotelo", 50, 1.84, 150, 250))
      @casillas << Casilla.new2(2, TipoCasilla::CALLE, 600, TituloPropiedad.new("Calle Velazquez", 50, 1.75, 150, 250))
      @casillas << Casilla.new1(3, TipoCasilla::IMPUESTO)
      @casillas << Casilla.new2(4, TipoCasilla::CALLE, 650, TituloPropiedad.new("Calle de la Paz", 52, 1.52, 200, 250))
      @carcel = Casilla.new1(5, TipoCasilla::CARCEL)
      @casillas << @carcel
      @casillas << Casilla.new2(6, TipoCasilla::CALLE, 1000, TituloPropiedad.new("Plaza Balmes", 62, 1.59, 300, 350))
      @casillas << Casilla.new1(7, TipoCasilla::SORPRESA)
      @casillas << Casilla.new2(8, TipoCasilla::CALLE, 1000, TituloPropiedad.new("Calle de las Delicias", 62, 1.7, 300, 350))
      @casillas << Casilla.new2(9, TipoCasilla::CALLE, 1100, TituloPropiedad.new("Calle de los Serranos", 65, 1.9, 350, 400))
      @casillas << Casilla.new1(10, TipoCasilla::PARKING)
      @casillas << Casilla.new2(11, TipoCasilla::CALLE, 1700, TituloPropiedad.new("Plaza de America", 79, 1.73, 650, 500))
      @casillas << Casilla.new1(12, TipoCasilla::SORPRESA)
      @casillas << Casilla.new2(13, TipoCasilla::CALLE, 1700, TituloPropiedad.new("Calle de Brasil", 79, 1.45, 650, 500))
      @casillas << Casilla.new2(14, TipoCasilla::CALLE, 1900, TituloPropiedad.new("Calle de Alcala", 83, 1.33, 700, 550))
      @casillas << Casilla.new1(15, TipoCasilla::JUEZ)
      @casillas << Casilla.new2(16, TipoCasilla::CALLE, 2500, TituloPropiedad.new("Gran Via", 95, 1.52, 950, 700))
      @casillas << Casilla.new1(17, TipoCasilla::SORPRESA)
      @casillas << Casilla.new2(18, TipoCasilla::CALLE, 2500, TituloPropiedad.new("Ramblas", 95, 1.52, 950, 700))
      @casillas << Casilla.new2(19, TipoCasilla::CALLE, 2700, TituloPropiedad.new("GV Fernando el Catolico", 100, 1.92, 1000, 750))
    end
    
  end

end