/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.beans;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import java.io.Serializable;
import java.util.ArrayList;
/*import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;*/
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

    public void addDatos() {
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        this.setUltimoAcceso(new Date());
        this.setTipo('u');
        this.setBloqueado('n');
        igd.add(Usuarios.this); //Cliente.this = this
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", this);
    }

    public void login() {
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        ArrayList<Usuarios> usuarios = (ArrayList<Usuarios>) igd.get("Usuarios");
        if (!usuarios.isEmpty()) {
            for (int i = 0; i < usuarios.size(); i++) {
                if (this.email.equals(usuarios.get(i).getEmail()) && this.password.equals(usuarios.get(i).getPassword())) {
                    System.out.println("Encontrado");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuarios.get(i));
                    break;
                }else{
                   System.out.println("No Encontrado"); 
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Login desconocido, intentelo otra vez"));
                }

            }
        }
    }
    public void cerrarSesion(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuario");
    }

    /*private String encriptar(String cadena) throws UnsupportedEncodingException {
        try {
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecretKey myDesKey = keygenerator.generateKey();

            Cipher desCipher;

            // Create the cipher
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            // Initialize the cipher for encryption
            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

            //sensitive information
            byte[] text = cadena.getBytes();

            // Encrypt the text
            byte[] textEncrypted = desCipher.doFinal(text);

            String value = new String(textEncrypted, "UTF-8");
            
            // Initialize the same cipher for decryption
            desCipher.init(Cipher.DECRYPT_MODE, myDesKey);

            // Decrypt the text
            byte[] textDecrypted = desCipher.doFinal(textEncrypted);
            
            return value;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
     */
}
