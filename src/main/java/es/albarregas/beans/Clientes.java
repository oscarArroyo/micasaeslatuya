/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.beans;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

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
    @Column(name = "IdCliente")
    private int id;
    @Column (nullable = false)
    private String nif;
    @Column (nullable = false)
    private String nombre;
    @Column(name = "Apellido1",nullable = false) 
    private String ape1;
    @Column(name = "Apellido2")
    private String ape2;
    @Column(name = "Telefono")
    private String tlf;

    @Transient
    private Clientes cliente;

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }
    
    
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
    
   // @PostConstruct
   /* public void init(){
        System.out.println("Entro init");
        FacesContext ctx = FacesContext.getCurrentInstance();
        cliente = (Clientes)ctx.getExternalContext().getSessionMap().get("cliente");
    }*/
    public void addDatos(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        Usuarios usu=(Usuarios)ctx.getExternalContext().getSessionMap().get("usuario");
        cliente = (Clientes)ctx.getExternalContext().getSessionMap().get("cliente");
        cliente.setId(usu.getId());
        igd.update(cliente);
        ctx.getExternalContext().getSessionMap().replace("cliente", cliente);
        ctx.addMessage("formularioCliente:datos", new FacesMessage("Datos modificados correctamente"));
    }
    
}
