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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


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
    private Boolean check;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

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
        setCheck(Boolean.FALSE);
        setId(0);
    }
    public void insertPreferenciasUsuario(int id){
        FacesContext ctx = FacesContext.getCurrentInstance();
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        Clientes cli =(Clientes)ctx.getExternalContext().getSessionMap().get("cliente");
        if(getCheck()){ 
            PrefCliente prefCliente = new PrefCliente();
            prefCliente.setIdPreferencia(id);
            prefCliente.setValor('s');
            prefCliente.setIdCliente(cli.getId());
            igd.add(prefCliente);
        }else{
            ArrayList<PrefCliente> borrarPref = (ArrayList<PrefCliente>)igd.get("PrefCliente where idCliente="+cli.getId()+" and idPreferencia="+id);
            igd.delete(borrarPref.get(0));
        }
    }
}
