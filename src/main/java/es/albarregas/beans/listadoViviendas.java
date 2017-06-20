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
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Transient;

/**
 *
 * @author Oscar
 * @version 1
 */
@ManagedBean
@ViewScoped
public class listadoViviendas implements Serializable{
    
    private ArrayList<Viviendas> listaViviendas;
    private ArrayList<Imagenes> img;
    
    @Transient
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public ArrayList<Viviendas> getListaViviendas() {
        return listaViviendas;
    }

    public void setListaViviendas(ArrayList<Viviendas> listaViviendas) {
        this.listaViviendas = listaViviendas;
    }

    public ArrayList<Imagenes> getImg() {
        return img;
    }

    public void setImg(ArrayList<Imagenes> img) {
        this.img = img;
    }
    /**
     * Método de carga inicial de datos
     */
    @PostConstruct
    public void init(){
        setListaViviendas(cargarListaViviendas());
    }

    /**
     * Método que carga la lista de viviendas segun la localidad
     * @return ArrayList
     */
    private ArrayList<Viviendas> cargarListaViviendas() {
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx.getExternalContext().getSessionMap().get("mensaje")!=null){
            ctx.getExternalContext().getSessionMap().remove("mensaje");
        }
        String bus = (String)ctx.getExternalContext().getSessionMap().get("bus");
        ArrayList<Pueblos> p = (ArrayList<Pueblos>)igd.get("Pueblos where nombre='"+bus+"'");
        if(p.isEmpty()){
            setCheck(false);
            return null;
        }
        ArrayList<Viviendas> localidad = (ArrayList<Viviendas>)igd.get("Viviendas v where v.localidad = '"+bus+"'");
        if(localidad.size()>0){
            setCheck(true);
            return localidad;
        }else{
            ctx.getExternalContext().getSessionMap().put("mensaje", "No existen viviendas en esa localidad");
            setCheck(false);
            return null;
        }
    }
    /**
     * Método que navega hacia la vivienda la cual ha pulsado el usuario
     * @param id
     * @throws java.io.IOException
     */
    public void goVivienda(int id) throws IOException{
          FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx.getExternalContext().getSessionMap().get("idv") == null) {
                ctx.getExternalContext().getSessionMap().put("idv", id);
            } else {
                ctx.getExternalContext().getSessionMap().replace("idv", id);
        }
          Utils.redirectUrlPeticion("/vivienda.xhtml");
    }  
}
