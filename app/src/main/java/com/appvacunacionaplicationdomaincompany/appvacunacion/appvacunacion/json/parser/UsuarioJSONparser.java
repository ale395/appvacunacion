package com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.json.parser;

import android.util.Log;

import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.modelos.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 24/10/2017.
 */

public class UsuarioJSONparser {
    public static Usuario parse(String content)/* throws UnsupportedEncodingException*/ {

        try {

            /*JSONArray jsonArray = new JSONArray(content);
            List<Usuario> usuarioList = new ArrayList<>();

            for(int i = 0; i<jsonArray.length(); i++){*/
                JSONObject jsonObject = new JSONObject(content);//jsonArray.getJSONObject(i);
                Usuario user = new Usuario();

                user.setIdUsuario(jsonObject.getInt("id"));
                user.setNombreUsuario(jsonObject.getString("nombre"));
                user.setEmailUsuario(jsonObject.getString("correo"));

                //usuarioList.add(user);

            /*} catch (JSONException e1) {
            e1.printStackTrace();
        }*/
        return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
