/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.beans;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.exception.FileUploadException;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author Oscar
 */
@ManagedBean
@SessionScoped
public class crearVivienda implements Serializable{
    private ArrayList<Imagenes> arrImagenes;
    private Pueblos pueblo;
    private Viviendas vivienda;
  
    public Viviendas getVivienda() {
        return vivienda;
    }

    public void setVivienda(Viviendas vivienda) {
        this.vivienda = vivienda;
    }
    private String poblacion;

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }
            
    public Pueblos getPueblo() {
        return pueblo;
    }

    public void setPueblo(Pueblos pueblo) {
        this.pueblo = pueblo;
    }

    public ArrayList<Imagenes> getArrImagenes() {
        return arrImagenes;
    }

    public void setArrImagenes(ArrayList<Imagenes> arrImagenes) {
        this.arrImagenes = arrImagenes;
    }
    @PostConstruct
    public void init(){
        setPueblo(new Pueblos());
        setArrImagenes(new ArrayList<Imagenes>());
        setVivienda(new Viviendas());

    }
     public String addImagenes(FileUploadEvent event) throws Exception {
        try {
            Imagenes imagenes = new Imagenes();
            DAOFactory df = DAOFactory.getDAOFactory();
            IGenericoDAO igd = df.getGenericoDAO();
            UploadedFile item = event.getUploadedFile();

            Blob blob = new javax.sql.rowset.serial.SerialBlob(item.getData());
                imagenes.setTipoMime(item.getFileExtension());
                imagenes.setIdV(1);
                imagenes.setImagen(blob);  
                getArrImagenes().add(imagenes);

        } catch (FileUploadException | SQLException e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return null;
    }
     public String buscarPoblacion(){
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        FacesContext ctx = FacesContext.getCurrentInstance();
        ArrayList<Pueblos> puebloCod =(ArrayList<Pueblos>) igd.get("Pueblos where codPostal='"+getPueblo().getNombre()+"'");
        if(puebloCod.size()>0){
            poblacion=puebloCod.get(0).getNombre();
            return poblacion;
        }
         ctx.addMessage("formCrearVivienda:cp", new FacesMessage("Código postal desconocido, intentelo otra vez"));
         return null;
     }
     public String addDatos(){
        getVivienda().setLocalidad(poblacion);
        FacesContext ctx = FacesContext.getCurrentInstance();
        Clientes cliente = (Clientes)ctx.getExternalContext().getSessionMap().get("cliente");
         System.out.println(""+cliente.getId());
        getVivienda().setIdCliente(cliente.getId());
         System.out.println("es.albarregas.beans.crearVivienda.addDatos()" + getVivienda().getIdCliente());
         System.out.println("es.albarregas.beans.crearVivienda.addDatos()" + getVivienda().getLocalidad());
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        igd.add(getVivienda());
         
         System.out.println("es.albarregas.beans.crearVivienda.addDatos() "+getArrImagenes().size());
         return null;
     }
       /* public void paint(OutputStream out, Object data) throws IOException, SQLException {
            DAOFactory df = DAOFactory.getDAOFactory();
            IGenericoDAO igd = df.getGenericoDAO();
           
        try {
            Imagenes imgPrueba=(Imagenes)igd.getOne(1, Imagenes.class);
            //System.out.println("i");
             int blobLength = (int) imgPrueba.getImagen().length();  
                RenderedImage image = ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(imgPrueba.getImagen().getBytes(1, blobLength))));
                //RenderedImage image = ImageIO.read(stream);               
                ImageIO.write(image,imgPrueba.getTipoMime(),out);
            }

       catch ( Exception e ) {
           e.printStackTrace();
       }
        }*/
}
