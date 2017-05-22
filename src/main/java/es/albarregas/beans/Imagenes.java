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
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author Oscar
 */
@ManagedBean
@Entity
@Table(name = "Imagenes")
public class Imagenes implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "IdImagen")
    private int id;

    @Column(name = "IdVivienda", nullable = false)
    private int idV;
    @Column(nullable = false)
    private Blob imagen;
    @Column(nullable = false)
    private String tipoMime;

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdV() {
        return idV;
    }

    public void setIdV(int idV) {
        this.idV = idV;
    }

    public Blob getImagen() {
        return imagen;
    }

    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

    public String addDatos(FileUploadEvent event) throws Exception {
        try {
            DAOFactory df = DAOFactory.getDAOFactory();
            IGenericoDAO igd = df.getGenericoDAO();
            UploadedFile item = event.getUploadedFile();

            Blob blob = new javax.sql.rowset.serial.SerialBlob(item.getData());
                this.setTipoMime(item.getFileExtension());

                //Integer tamanio = item.getFileName().length();
                //String stExtension = item.getFileName().substring(tamanio - 3, tamanio);
                System.out.println("es.albarregas.beans.Imagenes.addDatos()"+item.getData());
                this.setIdV(1);
                this.setImagen(blob);
                igd.add(Imagenes.this);
                //this.setImagen(item.getData());        
                //putMessageInfoLiteral("El archivo se ha subido correctamente. Recuerde guardar los cambios para almacenarlos en su ficha.");    

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return null;
    }
        public void paint(OutputStream out, Object data) throws IOException, SQLException {
            DAOFactory df = DAOFactory.getDAOFactory();
            IGenericoDAO igd = df.getGenericoDAO();
           
        try {
            Imagenes imgPrueba=(Imagenes)igd.getOne(5, Imagenes.class);
            //System.out.println("i");
             int blobLength = (int) imgPrueba.getImagen().length();  
                RenderedImage image = ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(imgPrueba.getImagen().getBytes(1, blobLength))));
                //RenderedImage image = ImageIO.read(stream);               
                ImageIO.write(image,imgPrueba.getTipoMime(),out);
            }

       catch ( Exception e ) {
           e.printStackTrace();
       }
        }
}
