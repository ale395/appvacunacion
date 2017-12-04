package com.appvacunacionaplicationdomaincompany.appvacunacion;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.appvacunacionaplicationdomaincompany.appvacunacion.LoginActivity.notificacionID;

/**
 * Created by Admin on 15/11/2017.
 */

public class FiltroActivity extends AppCompatActivity {
    Spinner spinner;
    Button btnAplicar;
    Button btnCancelar;
    RadioButton rbAscendente;
    RadioButton rbDescendente;
    String[] items;
    int hijoID;
    String nombreHijo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        spinner = (Spinner)findViewById(R.id.spinnerCombo);
        btnAplicar = (Button)findViewById(R.id.btnAplicarFiltro);
        btnCancelar = (Button)findViewById(R.id.btnCancelarFiltro);
        rbAscendente = (RadioButton)findViewById(R.id.radioAsc);
        rbDescendente = (RadioButton)findViewById(R.id.radioDesc);
        hijoID = getIntent().getIntExtra("idHijo", hijoID);
        nombreHijo = getIntent().getStringExtra("nombreHijo");

        items = getResources().getStringArray(R.array.lista_columnas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text, items);
        adapter.setDropDownViewResource(R.layout.spinner_text);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), items[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goVacunasView(spinner.getSelectedItemPosition(), 0);
            }
        });

        btnAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbDescendente.isChecked()){
                    goVacunasView(spinner.getSelectedItemPosition(), 1);
                }else {
                    goVacunasView(spinner.getSelectedItemPosition(), 0);
                }
            }
        });
    }

    private void goVacunasView(int columna, int orden){
        Intent intent = new Intent(this, ListaVacunas.class);
        intent.putExtra("columnaOrden", columna);
        intent.putExtra("tipoOrden", orden);
        intent.putExtra("idHijo", hijoID);
        intent.putExtra("nombreHijo", nombreHijo);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP/* | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK*/);
        startActivity(intent);
    }
}