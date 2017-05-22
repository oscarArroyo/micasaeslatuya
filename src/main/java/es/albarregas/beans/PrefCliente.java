/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Oscar
 */
@ManagedBean
@Entity
@Table (name = "PrefCliente")
public class PrefCliente implements Serializable{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "IdPrefCliente")
    private int id;
    private int idCliente;
    private int idPreferencia;
    private char valor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdPreferencia() {
        return idPreferencia;
    }

    public void setIdPreferencia(int idPreferencia) {
        this.idPreferencia = idPreferencia;
    }

    public char getValor() {
        return valor;
    }

    public void setValor(char valor) {
        this.valor = valor;
    }
    
}
