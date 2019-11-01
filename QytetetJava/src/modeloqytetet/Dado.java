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
public class Dado {
    
    int tirar(){    
        return (int)((Math.random() * 6)+1);
    }
    
    private Dado() {
    }
    
    public static Dado getInstance() {
        return DadoHolder.INSTANCE;
    }
    
    private static class DadoHolder {

        private static final Dado INSTANCE = new Dado();
    }
}
