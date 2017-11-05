package com.appvacunacionaplicationdomaincompany.appvacunacion;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.adapters.HijosAdapter;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.json.parser.HijosJSONparser;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.json.parser.UsuarioJSONparser;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.modelos.Hijo;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

import static com.appvacunacionaplicationdomaincompany.appvacunacion.LoginActivity.dirWebServerHijos;
import static com.appvacunacionaplicationdomaincompany.appvacunacion.LoginActivity.ipServer;
import static com.appvacunacionaplicationdomaincompany.appvacunacion.LoginActivity.portServer;

/**
 * Created by Admin on 4/11/2017.
 */

public class ListaHijos extends AppCompatActivity {
    Usuario usuario;//PADRE
    String usuarioStringJSON;
    List<Hijo> hijosList;
    HijosAdapter hijosAdapter;
    ListView listViewHijos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_hijos);

        usuarioStringJSON = getIntent().getStringExtra("usuarioStringJSON");
        usuario = UsuarioJSONparser.parse(usuarioStringJSON);

        listViewHijos= (ListView)findViewById(R.id.listViewHijos);

        hijosList = new ArrayList<>();

        validarHijos(usuario.getIdUsuario());
    }

    //////////////////////////////////
    public void validarHijos(int idPadre){
        pedirDatosHijos("http://"+ipServer+":"+portServer+dirWebServerHijos+idPadre);
    };

    public void mostrarLista (Context context, List<Hijo> ListHijos){
        hijosAdapter = new HijosAdapter(ListHijos, context);
        listViewHijos.setAdapter(hijosAdapter);

    }

    public void pedirDatosHijos(String uri){
       AsynTaskHijos task = new AsynTaskHijos();
        task.execute(uri);
    }

    public boolean isOnLine(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }

    private class AsynTaskHijos extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null){
                Log.e("Error:", "Nulo");
                Toast.makeText(getApplicationContext(), "Retorno Nulo OnPostExecute!", Toast.LENGTH_SHORT).show();
            }else{
                if (s.equals("C")){
                    Toast.makeText(getApplicationContext(), "No se pudo conectar al Web Service", Toast.LENGTH_LONG).show();
                }else {
                    hijosList = HijosJSONparser.parse(s);
                    if (hijosList.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Lista Vacía!", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(getApplicationContext(), ListaHijos.class);
                        intent.putExtra("usuarioStringJSON", s);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/
                    }else{
                        mostrarLista(getApplication(), hijosList);
                    }
                }
            }
        }
    }
}