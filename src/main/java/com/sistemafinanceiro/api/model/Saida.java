package com.sistemafinanceiro.api.model;

public class Saida extends Transacao{


    //CONSTRUTOR
    public Saida(int id, double valor, String categoria, String data) {
        super(id, -valor, categoria, data);
        
    }
    
}
