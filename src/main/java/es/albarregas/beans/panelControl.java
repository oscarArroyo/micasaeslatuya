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
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


/**
 *
 * @author Oscar
 */

@ManagedBean(name = "panelControl")
@ViewScoped
public class panelControl implements Serializable{
    private Preferencias pref;
    
    private ArrayList<Preferencias> listaPref;
    private ArrayList<Preferencias> prefSeleccionadas;

    public ArrayList<Preferencias> getPrefSeleccionadas() {
        return prefSeleccionadas;
    }

    public void setPrefSeleccionadas(ArrayList<Preferencias> prefSeleccionadas) {
        this.prefSeleccionadas = prefSeleccionadas;
    }

    public ArrayList<Preferencias> getListaPref() {
        return listaPref;
    }

    public void setListaPref(ArrayList<Preferencias> listaPref) {
        this.listaPref = listaPref;
    }

    public Preferencias getPref() {
        return pref;
    }

    public void setPref(Preferencias pref) {
        this.pref = pref;
    }
    @PostConstruct
    public void init(){
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        setListaPref((ArrayList<Preferencias>)igd.get("Preferencias"));
    }
    public void insertPreferenciasUsuario(){
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        //System.out.println("this.value "+value);
        
    }
}
