/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.beans;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.*;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Transient;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.exception.FileUploadException;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author Oscar
 * @version 1
 */
@ManagedBean
@ViewScoped
public class crearVivienda implements Serializable {

    private ArrayList<Imagenes> arrImagenes;
    private ArrayList<Preferencias> listaPref;
    private Pueblos pueblo;
    private Viviendas vivienda;
    private ArrayList<PrefVivienda> listaViv;
    @Transient
    private ArrayList<Integer> ids;

    public ArrayList<PrefVivienda> getListaViv() {
        return listaViv;
    }

    public void setListaViv(ArrayList<PrefVivienda> listaViv) {
        this.listaViv = listaViv;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public ArrayList<Preferencias> getListaPref() {
        return listaPref;
    }

    public void setListaPref(ArrayList<Preferencias> listaPref) {
        this.listaPref = listaPref;
    }

    public ArrayList<PrefVivienda> getPrefViv() {
        return listaViv;
    }

    public void setPrefViv(ArrayList<PrefVivienda> listaViv) {
        this.listaViv = listaViv;
    }

    @Transient
    private Boolean check;

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

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

    /**
     * Método de carga inicial de datos
     */
    @PostConstruct
    public void init() {
        setPueblo(new Pueblos());
        setArrImagenes(new ArrayList<Imagenes>());
        setVivienda(new Viviendas());
        setListaPref(listado());
        setCheck(Boolean.FALSE);
        setIds(new ArrayList<Integer>());
    }

    /**
     * Método que recibe un fichero, lo guarda en una carpeta del proyecto y
     * añade un objeto tipo Imagenes a un ArrayList para proceder a la
     * persistencia de estos registros en la base de datos
     *
     * @param event
     * @return String
     * @throws Exception
     */
    public String addImagenes(FileUploadEvent event) throws Exception {
        try {
            Imagenes imagenes = new Imagenes();
            UploadedFile item = event.getUploadedFile();
            imagenes.setTipoMime(item.getFileExtension());
            imagenes.setIdV(1);
            String filename = item.getName();
            imagenes.setRuta(filename);
            getArrImagenes().add(imagenes);
            Path folder = Paths.get("C:\\Users\\Oscar\\Documents\\NetBeansProjects\\ProyectoFinal_OscarArroyo\\src\\main\\webapp\\resources\\imgViviendas");
            Path file = Files.createTempFile(folder, filename, "");
            try (InputStream input = item.getInputStream()) {
                Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
            }
            Files.move(file, file.resolveSibling(filename));
        } catch (FileUploadException e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return null;
    }

    /**
     * Método que busca si el Código postal introducido pertenece a una
     * localidad
     */
    public void buscarPoblacion() {
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        FacesContext ctx = FacesContext.getCurrentInstance();
        ArrayList<Pueblos> puebloCod = (ArrayList<Pueblos>) igd.get("Pueblos where codPostal='" + getPueblo().getNombre() + "'");
        if (puebloCod.size() > 0) {
            poblacion = puebloCod.get(0).getNombre();
            getVivienda().setLocalidad(poblacion);
        } else {
            ctx.addMessage("formCrearVivienda:cp", new FacesMessage("Código postal desconocido, intentelo otra vez"));
        }
    }

    /**
     * Método que persiste los datos en la base de datos
     * @throws IOException
     */
    public void addDatos() throws IOException {
        FacesContext ctx = FacesContext.getCurrentInstance();

        if (getArrImagenes().size() >= 3) {
            if(getVivienda().getComentarios().length()<255){
            DAOFactory df = DAOFactory.getDAOFactory();
            IGenericoDAO igd = df.getGenericoDAO();
            Clientes cliente = (Clientes) ctx.getExternalContext().getSessionMap().get("cliente");
            getVivienda().setIdCliente(cliente.getId());
            igd.add(getVivienda());
            int idV = obtenerId();
            for (int i = 0; i < getArrImagenes().size(); i++) {
                arrImagenes.get(i).setIdV(idV);

                igd.add(arrImagenes.get(i));
            }
            for (int i = 0; i < getListaPref().size(); i++) {
                if (getListaPref().get(i).getSeleccionado()) {
                    PrefVivienda pv = new PrefVivienda();
                    pv.setIdPreferencia(getListaPref().get(i).getId());
                    pv.setValor('s');
                    pv.setIdVivienda(idV);
                    igd.add(pv);
                }
            }
            if (ctx.getExternalContext().getSessionMap().get("idv") == null) {
                ctx.getExternalContext().getSessionMap().put("idv", idV);
            } else {
                ctx.getExternalContext().getSessionMap().replace("idv", idV);
            }
            Utils.redirectUrlPeticion("/creacionOk.xhtml?id=" + idV);
            }else{
                System.out.println("longitud "+getVivienda().getComentarios().length());
                ctx.addMessage("formCrearVivienda:comentarios", new FacesMessage("Longitud maxima superada"));
            }
        } else {
            ctx.addMessage("formCrearVivienda:upload", new FacesMessage("Debes subir al menos tres imágenes"));
        }
    }
    /**
     * Método que selecciona o quita una preferencia
     * @param id 
     */
    public void insertPreferenciasVivienda(int id) {
        if (!getListaPref().get(id - 1).getSeleccionado()) {
            getListaPref().get(id - 1).setSeleccionado(true);
        } else {
            getListaPref().get(id - 1).setSeleccionado(false);
        }
    }
    /**
     * Método que devuelve la lista de preferencias
     * @return ArrayList
     */
    public ArrayList<Preferencias> listado() {
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        ArrayList<Preferencias> listado = (ArrayList<Preferencias>) igd.get("Preferencias");
        return listado;
    }
    /**
     * Método que obtiene el id de una vivienda
     * @return int 
     */
    private int obtenerId() {
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        ArrayList<Viviendas> idV = (ArrayList<Viviendas>) igd.get("Viviendas v where v.idCliente=" + getVivienda().getIdCliente() + " AND v.direccion='" + getVivienda().getDireccion() + "' AND v.numero='" + getVivienda().getNumero() + "'");
        return idV.get(0).getId();
    }

}
