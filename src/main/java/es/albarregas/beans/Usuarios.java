/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.beans;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import es.albarregas.utils.Utils;

/**
 *
 * @author Oscar
 */
@ManagedBean
@Entity
@Table(name = "Usuarios")
public class Usuarios implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "IdUsuario")
    private int id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date ultimoAcceso;
    @Column(columnDefinition = "char(1) default 'n' not null")
    private char bloqueado;
    @Column(columnDefinition = "char(1) default 'u' not null")
    private char tipo;
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Clientes cliente;

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setIdUsuarios(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public char getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(char bloqueado) {
        this.bloqueado = bloqueado;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public void oneCliente() {
        if (this.id > 0) {
            DAOFactory df = DAOFactory.getDAOFactory();
            IGenericoDAO igd = df.getGenericoDAO();
            Usuarios usu = (Usuarios) igd.getOne(this.id, Usuarios.class);
            this.id = usu.getId();
            this.email = usu.getEmail();
            this.password = usu.getPassword();
        }

    }

    public String addDatos() throws Exception {
        FacesContext ctx = FacesContext.getCurrentInstance();
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        ArrayList<Usuarios> usuarios = (ArrayList<Usuarios>) igd.get("Usuarios u where u.email='" + this.getEmail() + "'");
        if(usuarios.isEmpty()){
            this.setPassword(Utils.encode(this.getPassword()));
            this.setUltimoAcceso(new Date());
            this.setTipo('u');
            this.setBloqueado('n');
            igd.add(Usuarios.this);
            ctx.getExternalContext().getSessionMap().put("usuario", this);
            return "cor";
        }else{
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Email ya registrado"));
             return null;
        }
       
    }

    public void login() throws IOException, Exception {
        boolean encontrado = true;
        FacesContext ctx = FacesContext.getCurrentInstance();
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        ArrayList<Usuarios> usuarios = (ArrayList<Usuarios>) igd.get("Usuarios u where u.email='" + this.getEmail() + "'");
        if (!usuarios.isEmpty()) {
            if (this.password.equals(Utils.decode(usuarios.get(0).getPassword()))) {
                encontrado = false;
                ctx.getExternalContext().getSessionMap().put("usuario", usuarios.get(0));
                Utils.redirectUrlPeticion(ctx.getExternalContext().getRequestPathInfo());
            }
        } else if (encontrado) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Login desconocido, intentelo otra vez"));
        }

    }

    public void cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuario");
    }

}
