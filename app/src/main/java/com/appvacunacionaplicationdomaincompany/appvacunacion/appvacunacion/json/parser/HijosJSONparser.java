package com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.json.parser;

import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.modelos.Hijo;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.modelos.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 5/11/2017.
 */

public class HijosJSONparser {

    public static List<Hijo> parse(String content){
        try {
            JSONArray jsonArray = new JSONArray(content);
            List<Hijo> hijosList = new ArrayList<>();

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Hijo hijo = new Hijo();

                hijo.setId(jsonObject.getInt("id"));
                hijo.setNombre(jsonObject.getString("nombre"));
                hijo.setEdad(jsonObject.getInt("edad"));
                hijo.setSexo(jsonObject.getString("sexo"));

                hijosList.add(hijo);
            }
            return hijosList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}