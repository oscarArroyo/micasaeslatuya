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
 * @version 1
 */

@ManagedBean(name = "panelControl")
@ViewScoped
public class panelControl implements Serializable{
    private Preferencias pref;
    private ArrayList<Preferencias> listaPref;
    private ArrayList<PrefCliente> prefSeleccionadas;
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

    public ArrayList<PrefCliente> getPrefSeleccionadas() {
        return prefSeleccionadas;
    }

    public void setPrefSeleccionadas(ArrayList<PrefCliente> prefSeleccionadas) {
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
    /**
     * Método de carga inicial de datos
     */
    @PostConstruct
    public void init(){
        setListaPref(listado());
        setCheck(Boolean.FALSE);
        setPrefSeleccionadas(cargarPreferencias());
        setPref(new Preferencias());
        setId(0);
    }
    /**
     * Método que inserta una preferencia que el usuario ha seleccionado o la borra
     * @param id id de la preferencia seleccionada
     */
    public void insertPreferenciasUsuario(int id){
        FacesContext ctx = FacesContext.getCurrentInstance();
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        Clientes cli =(Clientes)ctx.getExternalContext().getSessionMap().get("cliente");
        ArrayList<PrefCliente> consultaPref = (ArrayList<PrefCliente>) igd.get("PrefCliente where idCliente="+cli.getId()+ " AND idPreferencia= "+id );
        if(consultaPref.size()>0){
            setCheck(Boolean.FALSE);
        }else{
            setCheck(Boolean.TRUE);
        }
        if(getCheck()){ 
            PrefCliente prefCliente = new PrefCliente();
            prefCliente.setIdPreferencia(id);
            prefCliente.setValor('s');
            prefCliente.setIdCliente(cli.getId());
            igd.add(prefCliente);
            setListaPref(listado());
        }else{
            ArrayList<PrefCliente> borrarPref = (ArrayList<PrefCliente>)igd.get("PrefCliente where idCliente="+cli.getId()+" and idPreferencia="+id);
            igd.delete(borrarPref.get(0));
            setListaPref(listado());
        }
    }
 
    /**
     * Método que carga las Preferencias de un usuario
     * @return ArrayList
     */
    public ArrayList<PrefCliente> cargarPreferencias(){
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        FacesContext ctx = FacesContext.getCurrentInstance();
        Clientes cli = (Clientes) ctx.getExternalContext().getSessionMap().get("cliente");
        ArrayList<PrefCliente> pc = (ArrayList<PrefCliente>)igd.get("PrefCliente pc where  pc.idCliente = "+cli.getId());
        return pc;
    }
    /**
     * Método que carga las Preferencias
     * @return ArrayList
     */
    public ArrayList<Preferencias> listado(){
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        ArrayList<Preferencias> listado = (ArrayList<Preferencias>)igd.get("Preferencias");
        FacesContext ctx = FacesContext.getCurrentInstance();
        Clientes cli = (Clientes) ctx.getExternalContext().getSessionMap().get("cliente");
        ArrayList<PrefCliente> pc = (ArrayList<PrefCliente>)igd.get("PrefCliente pc where pc.idCliente = "+cli.getId());
        for(int i=0;i<listado.size();i++){
            for(int j=0;j<pc.size();j++){
                if(pc.get(j).getIdPreferencia()==listado.get(i).getId()){
                    listado.get(i).setSeleccionado(true);
                    break;
                }else{
                    listado.get(i).setSeleccionado(false);
                }
            }
        }
        return listado;
    }
    /**
     *´Método que añade una nueva preferencia
     */
    public void addPreferencia(){
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        igd.add(getPref());
    }
}
