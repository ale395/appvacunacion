package com.appvacunacionaplicationdomaincompany.appvacunacion;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.json.parser.UsuarioJSONparser;
import com.appvacunacionaplicationdomaincompany.appvacunacion.appvacunacion.modelos.Usuario;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public static final String ipServer = "192.168.0.21";
    public static final String portServer = "8080";
    public static final String dirWebServerUsuario = "/control_vacunas_web_service/webresources/pkg_entidad.usuario/usuario/";
    public static final String dirWebServerHijos = "/control_vacunas_web_service/webresources/pkg_entidad.hijo/padre/";

    Button btnLogin;
    EditText editText;
    Usuario thisUsuario;
    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = (EditText)findViewById(R.id.txtCorreo);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        texto = (TextView)findViewById(R.id.texto);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnLine()){
                    if (editText.getText().toString() != null && editText.getText().toString().length() > 0){

                        validarUsuario(editText.getText().toString());

                    }else{
                        Toast.makeText(getApplicationContext(), "Introduzca un usuario válido!",  Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Sin Conexión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarUsuario(String email){
        pedirDatosUsuario("http://"+ipServer+":"+portServer+dirWebServerUsuario+email);
    };

    public void pedirDatosUsuario(String uri){
        AsynTaskUsuario task = new AsynTaskUsuario();
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

    private class AsynTaskUsuario extends AsyncTask<String, String, String> {

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
                texto.setText("Nulo");
                Toast.makeText(getApplicationContext(), "El usuario no esta registrado en la base de datos!", Toast.LENGTH_SHORT).show();
            }else{
                if (s.equals("C")){
                    Toast.makeText(getApplicationContext(), "No se pudo conectar al Web Service", Toast.LENGTH_LONG).show();
                }else {
                    thisUsuario = UsuarioJSONparser.parse(s);
                    if (thisUsuario != null){
                        Intent intent = new Intent(getApplicationContext(), ListaHijos.class);
                        intent.putExtra("usuarioStringJSON", s);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        }
    }
}