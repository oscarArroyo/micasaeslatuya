/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.utils;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import javax.faces.context.FacesContext;

/**
 *
 * @author Oscar
 */
public class Utils {

    //Devuelve la url completa de la vista que realiza la peticion
    public static String urlPeticion() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return ctx.getExternalContext().getRequestContextPath() + "/faces" + ctx.getExternalContext().getRequestPathInfo();
    }

    public static void redirectUrlPeticion(String url) throws IOException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getExternalContext().redirect(ctx.getExternalContext().getRequestContextPath() + "/faces" + url);
    }
    private final static String keyBuffer = "56af65d2";

    public static String encode(String palabra) throws Exception {
        try {
            byte[] bytes = palabra.getBytes();
            Base64.Encoder encoder = Base64.getEncoder();
            String encode = encoder.encodeToString(bytes);
            return encode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String decode(String palabra) throws Exception {
        try {
            byte[] bytes = Base64.getDecoder().decode(palabra);
            String decode = new String(bytes, StandardCharsets.UTF_8);
            return decode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
