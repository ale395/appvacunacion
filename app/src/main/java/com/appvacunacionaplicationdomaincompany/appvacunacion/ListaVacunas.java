package com.appvacunacionaplicationdomaincompany.appvacunacion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.adapters.HijosAdapter;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.adapters.VacunasAdapter;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.json.parser.HijosJSONparser;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.json.parser.UsuarioJSONparser;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.json.parser.VacunasJSONparser;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.modelos.Hijo;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.modelos.Vacuna;

import java.util.ArrayList;
import java.util.List;

import static com.appvacunacionaplicationdomaincompany.appvacunacion.LoginActivity.dirWebServerHijos;
import static com.appvacunacionaplicationdomaincompany.appvacunacion.LoginActivity.dirWebServerVacunas;
import static com.appvacunacionaplicationdomaincompany.appvacunacion.LoginActivity.ipServer;
import static com.appvacunacionaplicationdomaincompany.appvacunacion.LoginActivity.portServer;

/**
 * Created by Admin on 16/11/2017.
 */

public class ListaVacunas extends AppCompatActivity {
    Button btnFiltrar;
    ListView listViewVacunas;
    String nombreHijo;
    int hijoID;
    Hijo hijo;
    List<Vacuna> vacunasList;
    VacunasAdapter vacunasAdapter;
    TextView txtNombreHijo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vacunas);

        btnFiltrar = (Button)findViewById(R.id.btnFiltrar);
        txtNombreHijo = (TextView) findViewById(R.id.txtNombreHijo);

        hijoID = getIntent().getIntExtra("idHijo", hijoID);
        nombreHijo = getIntent().getStringExtra("nombreHijo");

        listViewVacunas= (ListView)findViewById(R.id.listViewVacunas);

        vacunasList = new ArrayList<>();

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FiltroActivity.class);
                startActivity(intent);
            }
        });

        txtNombreHijo.setText(nombreHijo);
        obtenerVacunas(hijoID);
    }

    private void obtenerVacunas(int id) {
        pedirDatosVacunas("http://"+ipServer+":"+portServer+dirWebServerVacunas+id);
    }

    private void pedirDatosVacunas(String uri) {
        AsynTaskVacunas task = new AsynTaskVacunas();
        task.execute(uri);
    }

    public void mostrarLista (Context context, List<Vacuna> ListVacunas){
        vacunasAdapter = new VacunasAdapter(ListVacunas, context);
        listViewVacunas.setAdapter(vacunasAdapter);
    }

    private class AsynTaskVacunas extends AsyncTask<String, String, String> {

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
                //Toast.makeText(getApplicationContext(), "Retorno Nulo OnPostExecute!", Toast.LENGTH_SHORT).show();
            }else{
                if (s.equals("C")){
                    Toast.makeText(getApplicationContext(), "No se pudo conectar al Web Service", Toast.LENGTH_LONG).show();
                }else {
                    Log.e("Vacunas", s);
                    vacunasList = VacunasJSONparser.parse(s);
                    if (vacunasList.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Lista Vacía!", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(getApplicationContext(), ListaHijos.class);
                        intent.putExtra("usuarioStringJSON", s);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/
                    }else{
                        mostrarLista(getApplication(), vacunasList);
                    }
                }
            }
        }
    }
}