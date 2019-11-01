#encoding: utf-8
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

require "singleton"

module ModeloQytetet

  class Dado

    include Singleton

    def initialize

    end

    
    def tirar()    
      numero = rand(6) + 1
      print("\nTe ha salido un #{numero}\n")
      return numero
    end
    
    
    def to_s()
      
    end
    
  end

end