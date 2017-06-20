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
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * 
 * @author Oscar
 * @version 1
 */
@ManagedBean
@ViewScoped
public class busqueda implements Serializable{
    
    private ArrayList<Contacto> arrContacto;
    private ArrayList<String> arrPueblos;
    private ArrayList<String> arrAutocomplete;
    private String bus;

    
    public ArrayList<Contacto> getArrContacto() {
        return arrContacto;
    }

    public void setArrContacto(ArrayList<Contacto> arrContacto) {
        this.arrContacto = arrContacto;
    }
    
    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public ArrayList<String> getArrAutocomplete() {
        return arrAutocomplete;
    }

    public void setArrAutocomplete(ArrayList<String> arrAutocomplete) {
        this.arrAutocomplete = arrAutocomplete;
    }

    public ArrayList<String> getArrPueblos() {
        return arrPueblos;
    }

    public void setArrPueblos(ArrayList<String> arrPueblos) {
        this.arrPueblos = arrPueblos;
    }
    /**
     * Método de carga inicial de datos
     */
    @PostConstruct
    public void init(){
        setArrContacto(mostrarMensajes());
        setBus(new String());
        setArrAutocomplete(new ArrayList<String>());
        setArrPueblos(cargarPueblos());
        for(String nombre : getArrPueblos()){
            getArrAutocomplete().add(nombre);
        }
    }
    /**
     * Retorna una lista de nombres que contienen la cadena introducida por el usuario
     * @param prefix cadena que el usuario ha introducido para hacer la busqueda
     * @return Una lista de String
     */
    public ArrayList<String> autocomplete(String prefix) {
        System.out.println("prefix "+prefix);
        ArrayList<String> result = new ArrayList();
        if ((prefix == null) || (prefix.length() == 0)) {
            for (int i = 0; i < 10; i++) {
                result.add(getArrPueblos().get(i));
            }
        } else {
            Iterator<String> iterator = getArrPueblos().iterator();
            while (iterator.hasNext()) {
                String elem = ((String) iterator.next());
                if ((elem != null && elem.toLowerCase().indexOf(prefix.toLowerCase()) == 0)
                    || "".equals(prefix)) {
                    result.add(elem);
                }
            }
        }
        return result;
    }
    /**
     * Retorna una lista del nombre de todos los pueblos
     * @return una lista de String
     */
    public ArrayList<String> cargarPueblos(){
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        return (ArrayList<String>)igd.getAll();
    }
    /**
     * Metodo que navega hacia el listado de viviendas segun la búsqueda del usuario
     * @return String
     */
    public String goListado(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx.getExternalContext().getSessionMap().get("bus")==null){
            ctx.getExternalContext().getSessionMap().put("bus", getBus());
        } else {
            ctx.getExternalContext().getSessionMap().replace("bus", getBus());
        }
        return "listadoViviendas";
    }
    /**
     * Método que recoge los mensajes pendientes de un usuario
     * @return ArrayList
     */
    public ArrayList<Contacto> mostrarMensajes(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx.getExternalContext().getSessionMap().get("usuario")!=null){
            DAOFactory df = DAOFactory.getDAOFactory();
            IGenericoDAO igd = df.getGenericoDAO();
            Usuarios u = (Usuarios )ctx.getExternalContext().getSessionMap().get("usuario");
            ArrayList<Contacto> cto = (ArrayList<Contacto>) igd.get("Contacto where idArrendador="+u.getId());
            if(!cto.isEmpty()){
                return cto;
            }
        }
        return null;
    }
    /**
     * Método que borra los mensajes leidos por el usuario
     */
    public void borrarMensajes(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        Usuarios u = (Usuarios )ctx.getExternalContext().getSessionMap().get("usuario");
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        for(int i=0;i<getArrContacto().size();i++){
        igd.delete(getArrContacto().get(i));
        }
    }
}
