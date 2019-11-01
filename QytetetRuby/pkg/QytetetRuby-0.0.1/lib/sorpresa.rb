#encoding: utf-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

module ModeloQytetet
  
  class Sorpresa

    def initialize(t,v,tp)
      @texto = t
      @tipo = tp
      @valor = v
    end
    
    public
    attr_reader :texto, :valor, :tipo

    def to_s
      "Texto: #{@texto} \n Valor: #{@valor} \n Tipo: #{@tipo} \n\n"
    end 
    
  end
  
end