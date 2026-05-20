package com.sistemafinanceiro.api.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistemafinanceiro.api.model.Transacao;
import com.sistemafinanceiro.api.service.TransacaoService;


@RestController
@RequestMapping("/transacoes")
public class TransacoesController{
    // ---------- atributos ----------
    
    private TransacaoService transacaoService;

    //---------- construtor ----------

     public TransacoesController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    // -------- Lista --------
    @PostMapping
    public void criar(@RequestBody Transacao t) throws  SQLException{
        transacaoService.addTran(t);
    }


    @GetMapping
    //--ler dados sql
    public List<Transacao> listar() throws SQLException {
       return transacaoService.lerDadosSQL();
    }

}