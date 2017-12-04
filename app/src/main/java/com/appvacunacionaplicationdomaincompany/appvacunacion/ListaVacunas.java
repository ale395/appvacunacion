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
import java.util.Collections;
import java.util.Comparator;
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
    int columnaOrden;
    int tipoOrden;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vacunas);

        btnFiltrar = (Button)findViewById(R.id.btnFiltrar);
        txtNombreHijo = (TextView) findViewById(R.id.txtNombreHijo);

        hijoID = getIntent().getIntExtra("idHijo", hijoID);
        nombreHijo = getIntent().getStringExtra("nombreHijo");
        columnaOrden = getIntent().getIntExtra("columnaOrden", columnaOrden);
        tipoOrden = getIntent().getIntExtra("tipoOrden", tipoOrden);

        listViewVacunas= (ListView)findViewById(R.id.listViewVacunas);

        vacunasList = new ArrayList<>();

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FiltroActivity.class);
                intent.putExtra("idHijo", hijoID);
                intent.putExtra("nombreHijo", nombreHijo);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    private class ComparadorVacunasPorNombre implements Comparator<Vacuna> {
        private boolean asc;

        ComparadorVacunasPorNombre(boolean asc) {
            this.asc = asc;
        }
        @Override
        public int compare(Vacuna o1, Vacuna o2) {
            int ret;
            if (asc) {
                ret = o1.getNombreVacuna().compareTo(o2.getNombreVacuna());
            } else {
                ret = o2.getNombreVacuna().compareTo(o1.getNombreVacuna());
            }
            return ret;
        }
    }

    private class ComparadorVacunasPorFecha implements Comparator<Vacuna> {
        private boolean asc;

        ComparadorVacunasPorFecha(boolean asc) {
            this.asc = asc;
        }
        @Override
        public int compare(Vacuna o1, Vacuna o2) {
            int ret;
            if (asc) {
                ret = o1.getFechaAplicacion().compareTo(o2.getFechaAplicacion());
            } else {
                ret = o2.getFechaAplicacion().compareTo(o1.getFechaAplicacion());
            }
            return ret;
        }
    }

    private class ComparadorVacunasPorAplicacion implements Comparator<Vacuna> {
        private boolean asc;

        ComparadorVacunasPorAplicacion(boolean asc) {
            this.asc = asc;
        }
        @Override
        public int compare(Vacuna o1, Vacuna o2) {
            int ret;
            if (asc) {
                ret = o2.getAplicada().compareTo(o1.getAplicada());
            } else {
                ret = o1.getAplicada().compareTo(o2.getAplicada());
            }
            return ret;
        }
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
                    Toast.makeText(getApplicationContext(), "No se pudo conectar al Web Service!", Toast.LENGTH_LONG).show();
                }else {
                    Log.e("Vacunas", s);
                    vacunasList = VacunasJSONparser.parse(s);
                    if (vacunasList.isEmpty()){
                        Toast.makeText(getApplicationContext(), "No se han encontrado Vacunas!", Toast.LENGTH_SHORT).show();
                    }else{
                        if (columnaOrden == 0){
                            //ordenar por nombre vacuna
                            if (tipoOrden == 0){
                                Collections.sort(vacunasList, new ComparadorVacunasPorNombre(true));//ASCENDENTE
                            }else{
                                Collections.sort(vacunasList, new ComparadorVacunasPorNombre(false));//DESCENDENTE
                            }
                        }else {
                            if (columnaOrden == 1){
                                //ordenar por fecha de aplicación
                                if (tipoOrden == 0){
                                    Collections.sort(vacunasList, new ComparadorVacunasPorFecha(true));//ASCENDENTE
                                }else{
                                    Collections.sort(vacunasList, new ComparadorVacunasPorFecha(false));//DESCENDENTE
                                }
                            }else{
                                //ordenar por aplicada o no aplicada
                                if (tipoOrden == 0){
                                    Collections.sort(vacunasList, new ComparadorVacunasPorAplicacion(true));//ASCENDENTE
                                }else{
                                    Collections.sort(vacunasList, new ComparadorVacunasPorAplicacion(false));//DESCENDENTE
                                }
                            }
                        }
                        mostrarLista(getApplication(), vacunasList);
                    }
                }
            }
        }
    }
}