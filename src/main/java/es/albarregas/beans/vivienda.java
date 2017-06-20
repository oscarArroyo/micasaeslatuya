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
import java.util.Date;
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
public class vivienda implements Serializable{
    private Viviendas viv;
    private ArrayList<Imagenes> img;
    private ArrayList<Preferencias> listaPreferencias;
    private ArrayList<PrefVivienda> prefViv;
    private Contacto cto;

    public Contacto getCto() {
        return cto;
    }

    public void setCto(Contacto cto) {
        this.cto = cto;
    }
    /**
     * Método de carga inicial de datos
     */
    @PostConstruct
    public void init(){
        setViv(cargarVivienda());
        setImg(cargarImagenes());
        setListaPreferencias(cargarPreferencias());
        setCto(new Contacto());
    }
    /**
     * Método que carga una vivienda determinada
     * @return Viviendas
     */
    private Viviendas cargarVivienda() {
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        FacesContext ctx = FacesContext.getCurrentInstance();
        int idV = (int)ctx.getExternalContext().getSessionMap().get("idv");
        ArrayList<Viviendas> v = (ArrayList<Viviendas>)igd.get("Viviendas where id="+idV);
        return v.get(0);
    }
    /**
     * Método que carga las imagenes de una vivienda determinada
     * @return ArrayList
     */
     private ArrayList<Imagenes> cargarImagenes() {
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        FacesContext ctx = FacesContext.getCurrentInstance();
        int idV = (int)ctx.getExternalContext().getSessionMap().get("idv");
        return (ArrayList<Imagenes>)igd.get("Imagenes where idV="+idV);
        
    }
     /**
      * Método que carga las preferencias de una vivienda
      * @return ArrayList
      */
     private ArrayList<Preferencias> cargarPreferencias(){
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        FacesContext ctx = FacesContext.getCurrentInstance();
        ArrayList<Preferencias> listaPref = (ArrayList<Preferencias>) igd.get("Preferencias");
        int idV = (int)ctx.getExternalContext().getSessionMap().get("idv");
        ArrayList<PrefVivienda> listaPrefViv = (ArrayList<PrefVivienda>) igd.get("PrefVivienda where idVivienda="+idV);
          for(int i=0;i<listaPref.size();i++){
             for(int j=0;j<listaPrefViv.size();j++){
                if(listaPrefViv.get(j).getIdPreferencia()==listaPref.get(i).getId()){
                    listaPref.get(i).setSeleccionado(true);
                    break;
                }else{
                    listaPref.get(i).setSeleccionado(false);
                }
            }
        }
        return listaPref;

     }
     /**
      * Método para añadir un registro en la tabla contacto
      */
    public void contacto(){
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        FacesContext ctx = FacesContext.getCurrentInstance();
        Usuarios usu = (Usuarios) ctx.getExternalContext().getSessionMap().get("usuario");
        getCto().setIdUsuario(usu.getId());
        int idV = (int)ctx.getExternalContext().getSessionMap().get("idv");
        ArrayList<Viviendas> v = (ArrayList<Viviendas>) igd.get("Viviendas where id="+idV);
        getCto().setIdArrendador(v.get(0).getIdCliente());
        getCto().setFecha(new Date());
        igd.add(cto);
    }
      
    
    public Viviendas getViv() {
        return viv;
    }

    public void setViv(Viviendas viv) {
        this.viv = viv;
    }

    public ArrayList<Imagenes> getImg() {
        return img;
    }

    public void setImg(ArrayList<Imagenes> img) {
        this.img = img;
    }

    public ArrayList<Preferencias> getListaPreferencias() {
        return listaPreferencias;
    }

    public void setListaPreferencias(ArrayList<Preferencias> listaPreferencias) {
        this.listaPreferencias = listaPreferencias;
    }

    public ArrayList<PrefVivienda> getPrefViv() {
        return prefViv;
    }

    public void setPrefViv(ArrayList<PrefVivienda> prefViv) {
        this.prefViv = prefViv;
    }

    
    
    
    
}
