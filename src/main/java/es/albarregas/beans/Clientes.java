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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Oscar
 */
@ManagedBean
@Entity
@Table(name = "Clientes")
public class Clientes implements Serializable {

    @OneToOne(mappedBy = "cliente")
    @PrimaryKeyJoinColumn
    private Usuarios usuario;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "IdCliente")
    private int id;
    private String nif;
    private String nombre;
    @Column(name = "Apellido1")
    private String ape1;
    @Column(name = "Apellido2")
    private String ape2;
    @Column(name = "Telefono")
    private String tlf;

   public Usuarios getUsuario() {
       return usuario;
   }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
   }
   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApe1() {
        return ape1;
    }

    public void setApe1(String ape1) {
        this.ape1 = ape1;
    }

    public String getApe2() {
        return ape2;
    }

    public void setApe2(String ape2) {
        this.ape2 = ape2;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }
    
}
