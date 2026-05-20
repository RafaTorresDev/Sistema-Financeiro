package com.sistemafinanceiro.api.service;

import com.sistemafinanceiro.api.model.Transacao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Service;


@Service
public class TransacaoService{

    //--------------------atributos---------------------------
    private List<Transacao> lista = new ArrayList<Transacao>();


    //----------------construtor--------------------
    public TransacaoService(List<Transacao> lista) {
        this.lista = new ArrayList<>();
    }

    public TransacaoService() {}

    //-------- get --------
    public List<Transacao> getLista() {
        return lista;
    }


    //-------- banco de dados --------

    public void deletarDadosSQL(int id) throws SQLException{
        String deletar = "DELETE FROM transacao WHERE ide = ?;";
        Connection conec = conectar();
        PreparedStatement ps = conec.prepareStatement(deletar);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public Connection conectar(){
        String url = "jdbc:mysql://localhost:3306/sistema_financeiro";
        String usuario = "root";
        String senha = "16160";
        Connection con = null;

        try {
            con = DriverManager.getConnection(url, usuario, senha);
        } catch (Exception e) {
            System.out.println("ERROR!");
        }
        return con;
    }

    public void salvarDadosSQL(Transacao t) throws SQLException{
        String salvar = "INSERT INTO transacao (valor, categoria, data) VALUES (?, ?, ?)";
        Connection conec = conectar();
        PreparedStatement ps = conec.prepareStatement(salvar);
        ps.setDouble(1, t.getValor());
        ps.setString(2, t.getCategoria());
        ps.setString(3, formatarData(t.getData()));
        ps.executeUpdate();
    }

    public List<Transacao> lerDadosSQL() throws SQLException{
        String ler = "SELECT * FROM transacao";
        Connection conec = conectar();
        PreparedStatement ps = conec.prepareStatement(ler);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int id = rs.getInt("ide");
            double valor = rs.getDouble("valor");
            String categoria = rs.getString("categoria");
            String data = rs.getString("data");

            Transacao t = new Transacao(id, valor, categoria, data);
            lista.add(t);
        }
        return lista;
    }


    //-------- transações --------

    public void addTran(Transacao T){
        lista.add(T);
        try {
            salvarDadosSQL(T);
        } catch (SQLException e) {
            System.out.println("Erro ao salvar transação no banco de dados.");
        }
    }

    public Transacao lerTransacao(Scanner sc){
        sc.nextLine();
        System.out.println("Valor: ");
        double v = sc.nextDouble();
        sc.nextLine();
        System.out.println("Qual a categoria: ");
        String cat = sc.nextLine();
        System.out.println("Data modelo (dd/MM/YYYY): ");
        String data = sc.nextLine();

        Transacao t = new Transacao(0, v, cat, data);

        return t;
    }


    //-------- relatório --------

    public double calculoSaldo(){
        double saldoTotal = 0.0;
        for(Transacao t : lista){
           saldoTotal += t.getValor();
        }
        return saldoTotal;
    }

    public void exibir() throws SQLException{
        lista.clear();
        lerDadosSQL();
        
        for(Transacao t : lista){
            System.out.println(t.getId() + " - " + t.getData() + " - " + t.getCategoria() + " - " + t.getValor());
        }
        double total = calculoSaldo();
        System.out.println("Saldo total: " + total);
        System.out.println();
    }

    public void exibirCategoria(String categoria){
        double total = 0.0;
        for(Transacao t : lista){
            if(t.getCategoria().equalsIgnoreCase(categoria)){
                System.out.println(t.getId() + " - " + t.getData() + " - " + t.getCategoria() + " - " + t.getValor());
                total += t.getValor();
            }
        }
        System.out.println("Saldo total: " + total);
        System.out.println();
    }

    public void deletarDadosLista(int id){
        for(Transacao t : lista){
            if(t.getId() == id){
                lista.remove(t);
                break;
            }
        }
    }

    public void deletar(int id) throws SQLException{
        deletarDadosLista(id);
        deletarDadosSQL(id);
    }


    //-------- utilitários --------

    public String formatarData(String data){
        String[] partes = data.split("/");

        if(partes.length == 3){
            String dia = partes[0];
            String mes = partes[1];
            String ano = partes[2];

            return ano + "-" + mes + "-" + dia;
        } else {
            throw new IllegalArgumentException("Data no formato inválido. Use dd/MM/yyyy.");
        }
    }
}