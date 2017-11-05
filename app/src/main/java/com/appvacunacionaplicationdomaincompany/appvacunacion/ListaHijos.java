package com.appvacunacionaplicationdomaincompany.appvacunacion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.json.parser.UsuarioJSONparser;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.modelos.Usuario;

/**
 * Created by Admin on 4/11/2017.
 */

public class ListaHijos extends AppCompatActivity {
    TextView welcomeText;
    Usuario usuario;//PADRE
    String usuarioStringJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_hijos);

        usuarioStringJSON = getIntent().getStringExtra("usuarioStringJSON");
        usuario = UsuarioJSONparser.parse(usuarioStringJSON);

        welcomeText = (TextView)findViewById(R.id.welcomeText);

        welcomeText.append(usuario.toString());
    }
}