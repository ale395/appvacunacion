package com.appvacunacionaplicationdomaincompany.appvacunacion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final String ipServer = "192.168.43.3";
    public static final String portServer = "8080";
    public static final String dirWebServerUsuario = "/control_vacunas_web_service/webresources/pkg_entidad.usuario/usuario/";
    public static final String dirWebServerHijos = "/control_vacunas_web_service/webresources/pkg_entidad.hijo/padre/";
    public static final String dirWebServerVacunas = "/control_vacunas_web_service/webresources/pkg_entidad.vacuna/hijo/";
    public static final String dirWebServerVacunasUsuario = "/control_vacunas_web_service/webresources/pkg_entidad.vacuna/hijo/";
    public static final int notificacionID = 123;
    private GoogleApiClient googleApiClient;
    private ProgressDialog progress;

    SignInButton btnGoogle;
    Usuario thisUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnGoogle = (SignInButton)findViewById(R.id.btnGoogle);
        progress = new ProgressDialog(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        btnGoogle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                progress.setMessage("Iniciando sesión...");
                progress.show();
                if (isOnLine()){
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(intent, 111);
                }else{
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Sin Conexión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            validarUsuario(result.getSignInAccount().getEmail());
        }else{
            progress.dismiss();
            //Toast.makeText(getApplicationContext(), "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
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
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "El usuario no esta registrado en la base de datos!", Toast.LENGTH_SHORT).show();
            }else{
                if (s.equals("C")){
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "No se pudo conectar al Web Service!", Toast.LENGTH_LONG).show();
                }else {
                    thisUsuario = UsuarioJSONparser.parse(s);
                    if (thisUsuario != null){
                        Intent intent = new Intent(getApplicationContext(), ListaHijos.class);
                        intent.putExtra("usuarioStringJSON", s);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        progress.dismiss();
                        startActivity(intent);
                    }
                }
            }
        }
    }
}