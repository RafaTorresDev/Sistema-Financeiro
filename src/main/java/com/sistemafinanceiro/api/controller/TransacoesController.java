package com.sistemafinanceiro.api.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // -------- endpoints --------

    @GetMapping
    public List<Transacao> listar() throws SQLException {
        return transacaoService.lerDadosSQL();
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody Transacao t) throws SQLException {
        transacaoService.addTran(t);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws SQLException {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}