/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oscar
 * @version 1
 * Modelo Contacto
 */
@Entity
@Table(name = "Contacto")
public class Contacto implements Serializable{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column (name = "IdContacto")
    private int id;
    @Column(nullable = false)
    private int idUsuario;
    @Column(nullable = false)
    private int idArrendador;
    @Column(nullable = false)
    private String mensaje;
    @Column(nullable = false)
    @Temporal (value = TemporalType.TIMESTAMP)
    private Date fecha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdArrendador() {
        return idArrendador;
    }

    public void setIdArrendador(int idArrendador) {
        this.idArrendador = idArrendador;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
}
