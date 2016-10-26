package com.example.curso.mywebservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class registraralumno extends AppCompatActivity {
    EditText eT_nombre,eT_apellido,eT_dni,eT_edad;
    Button btnguardar,btnsalir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraralumno);


        eT_nombre=(EditText) findViewById(R.id.eT_nombre) ;
        eT_apellido=(EditText) findViewById(R.id.eT_apellido) ;
        eT_dni=(EditText) findViewById(R.id.eT_dni) ;
        eT_edad=(EditText) findViewById(R.id.eT_edad) ;

        btnguardar=(Button)findViewById(R.id.btnguardar) ;
        btnsalir=(Button)findViewById(R.id.btnsalir) ;



        btnguardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSInsertar tarea = new TareaWSInsertar();
                tarea.execute(
                        eT_nombre.getText().toString(),
                        eT_apellido.getText().toString(),
                        eT_dni.getText().toString(),
                        eT_edad.getText().toString());
            }
        });

        btnsalir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Tarea Asíncrona para llamar al WS de inserción en segundo plano
    private class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://192.168.0.101:8020/api/alumnos/crup/1?");
            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                //dato.put("Id", Integer.parseInt(txtId.getText().toString()));
                dato.put("ALuNom", String.valueOf(params[0]));
                dato.put("AluApe", String.valueOf(params[1]));
                dato.put("AluDni", String.valueOf(params[2]));
                dato.put("AluEdad", Integer.parseInt(params[3]));

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                if(!respStr.equals("true"))
                    resul = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                //lblResultado.setText("Insertado OK.");
                Toast.makeText(getApplicationContext(),"Ingreso Correcto",Toast.LENGTH_SHORT).show();

                eT_nombre.setText("");
                 eT_apellido.setText("");
                 eT_dni.setText("");
                        eT_edad.setText("");
                eT_nombre.requestFocus();

            }
        }
    }

}
