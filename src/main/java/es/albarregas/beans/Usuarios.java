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
import javax.persistence.Transient;

/**
 *
 * @author Oscar
 * @version 1
 * Modelo Usuarios
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
    
    @Transient
    private String otraPass;

    public String getOtraPass() {
        return otraPass;
    }

    public void setOtraPass(String otraPass) {
        this.otraPass = otraPass;
    }
    
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
    /**
     * Método que se encarga de añadir un objeto Usuarios a la base de datos e incluirlo en la sesión. También se crea un registro en la tabla Clientes con el mismo id
     * @return String
     * @throws Exception 
     */
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
            Clientes cli = new Clientes();
            cli.setId(this.getId());
            cli.setNombre("");
            cli.setApe1("");
            cli.setNif("");
            cli.setTlf("");
            igd.add(cli);
            ctx.getExternalContext().getSessionMap().put("cliente", cli);
            return "cor";
        }else{
             ctx.addMessage("formularioReg:btnReg", new FacesMessage("Email ya registrado"));
             return null;
        }
       
    }
    /**
     * Método que realiza el login en la aplicación
     * @throws IOException
     * @throws Exception 
     */
    public void login() throws IOException, Exception {
        FacesContext ctx = FacesContext.getCurrentInstance();
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        ArrayList<Usuarios> usuarios = (ArrayList<Usuarios>) igd.get("Usuarios u where u.email='" + this.getEmail() + "'");
        if (!usuarios.isEmpty() && this.password.equals(Utils.decode(usuarios.get(0).getPassword()))) {
                Clientes cli = (Clientes)igd.getOne(usuarios.get(0).getId(), Clientes.class);
                ctx.getExternalContext().getSessionMap().put("usuario", usuarios.get(0));
                ctx.getExternalContext().getSessionMap().put("cliente",cli);
                Utils.redirectUrlPeticion(ctx.getExternalContext().getRequestPathInfo());
        } else{
           ctx.addMessage("formulario:btnLog", new FacesMessage("Login desconocido, intentelo otra vez"));
        }

    }
    /**
     * Método que cierra la sesión del usuario en la aplicacion y elimina los datos de esta.
     * @throws IOException 
     */
    public void cerrarSesion() throws IOException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getExternalContext().getSessionMap().remove("usuario");
        ctx.getExternalContext().getSessionMap().remove("cliente");
        ctx.getExternalContext().getSessionMap().remove("bus");
        Utils.redirectUrlPeticion("/index.xhtml");
    }

    /**
     * Método que navega al panel del usuario
     * @return String
     */
    public String goPanel(){
      return "panelUsuario";
  
    }
    /**
     * Método que navega a la creación de una vivienda
     * @return String 
     */
    public String crearVivienda(){
    return "crearVivienda";
}
    /**
     * Método que cambia la password de un usuario
     * @throws Exception 
     */
    public void cambioPass() throws Exception{
        FacesContext ctx = FacesContext.getCurrentInstance();
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        Usuarios usuSesion = (Usuarios)ctx.getExternalContext().getSessionMap().get("usuario");
        if(Utils.decode(usuSesion.getPassword()).equals(this.getPassword())){
            usuSesion.setPassword(Utils.encode(this.getOtraPass()));
            igd.update(usuSesion);
            ctx.getExternalContext().getSessionMap().replace("usuario",getPassword(),this.getOtraPass());
            ctx.addMessage(null, new FacesMessage("Datos modificados correctamente"));
        }else{
            ctx.addMessage(null, new FacesMessage("La contraseña antigua no se corresponde con la indicada"));
        }
    }
   
}
