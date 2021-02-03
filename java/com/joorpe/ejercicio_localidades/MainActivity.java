package com.joorpe.ejercicio_localidades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG  ="TAG_"+MainActivity.class.getName();
    private final static String URL_ALL_COMUNIDADES = "https://onthestage.es/restapi/v1/allcomunidades";
    private final static String URL_PROVINCIA_BY_COMUNIDAD = "https://onthestage.es/restapi/v1/provinciasbycom/"; //Añadir ID de la comunidad
    private final static String URL_LOCALIDAD_BY_PROVINCIA = "https://onthestage.es/restapi/v1/localidadesbyprov/"; //Añadir ID de la provincia

    Spinner spComunidad, spProvincia, spLocalidad;
    SpinnerAdapter spinnerAdapterComunidad, spinnerAdapterProvincia, spinnerAdapterLocalidad;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        spComunidad = (Spinner) findViewById(R.id.spComunidad);
        spProvincia = (Spinner) findViewById(R.id.spProvincia);
        spLocalidad = (Spinner) findViewById(R.id.spLocalidad);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        spinnerAdapterComunidad = new SpinnerAdapter(this, new ArrayList<Ubicacion>());
        spinnerAdapterProvincia = new SpinnerAdapter(this, new ArrayList<Ubicacion>());
        spinnerAdapterLocalidad = new SpinnerAdapter(this, new ArrayList<Ubicacion>());

        spComunidad.setAdapter(spinnerAdapterComunidad);
        spProvincia.setAdapter(spinnerAdapterProvincia);
        spLocalidad.setAdapter(spinnerAdapterLocalidad);


        spComunidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Comunidad comunidad = (Comunidad) parent.getAdapter().getItem(position);
                getProvinciasByComunidad(comunidad.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Provincia provincia = (Provincia) spinnerAdapterProvincia.getItem(position);
                getLocalidadesByProvincia(provincia.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spLocalidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0 && !spinnerAdapterLocalidad.getItem(position).getDescripcion().equals(""))
                    Toast.makeText(MainActivity.this,"Has elegido " + spinnerAdapterLocalidad.getItem(position).getDescripcion(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        getComunidades();
    }

    private void getComunidades(){
        spProvincia.setVisibility(View.GONE);
        spLocalidad.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new GetAsync(new GetAsync.IAsyncGet() {
            @Override
            public void onFinish(JSONArray ubicaciones) {
                //Cargar localidades
                List<Ubicacion> comunidades = new ArrayList<Ubicacion>();
                for(int i = 0;i<ubicaciones.length();i++){
                    try {
                        JSONObject comunidadJSON = ubicaciones.getJSONObject(i);
                        int id = comunidadJSON.getInt("id");
                        String descripcion = comunidadJSON.getString("descripcion");

                        comunidades.add(new Comunidad(id, descripcion));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Se añade una comunidad al incio para que cuando salte OnItemSelectedListener no devuelva nada.
                comunidades.add(0,new Comunidad(-1,getString(R.string.choose_com)));
                spinnerAdapterComunidad.setUbicaciones(comunidades);
                progressBar.setVisibility(View.GONE);
            }
        }).execute(URL_ALL_COMUNIDADES );
    }

    private void getProvinciasByComunidad(int comunidad){
        progressBar.setVisibility(View.VISIBLE);
        new GetAsync(new GetAsync.IAsyncGet() {
            @Override
            public void onFinish(JSONArray ubicaciones) {

                //Cargar Provincias
                List<Ubicacion> provincias = new ArrayList<Ubicacion>();
                for(int i = 0;i<ubicaciones.length();i++){
                    try {
                        JSONObject provinciaJSON = ubicaciones.getJSONObject(i);
                        int id = provinciaJSON.getInt("id");
                        String descripcion = provinciaJSON.getString("descripcion");
                        int idComunidad = provinciaJSON.getInt("comunidad");

                        provincias.add(new Provincia(id, descripcion, idComunidad));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(provincias.size()>0){
                    spProvincia.setVisibility(View.VISIBLE);
                }
                provincias.add(0,new Provincia(-1,getString(R.string.choose_prov)));
                spinnerAdapterProvincia.setUbicaciones(provincias);

                ((SpinnerAdapter)spLocalidad.getAdapter()).notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        }).execute(URL_PROVINCIA_BY_COMUNIDAD + comunidad);
    }

    private void getLocalidadesByProvincia(int provincia){
        progressBar.setVisibility(View.VISIBLE);
        new GetAsync(new GetAsync.IAsyncGet() {
            @Override
            public void onFinish(JSONArray ubicaciones) {
                //Cargar Localidades
                List<Ubicacion> localidades = new ArrayList<Ubicacion>();
                for(int i = 0;i<ubicaciones.length();i++){
                    try {
                        JSONObject localidadJSON = ubicaciones.getJSONObject(i);
                        int id = localidadJSON.getInt("id");
                        String descripcion = localidadJSON.getString("descripcion");
                        int idProvincia = localidadJSON.getInt("provincia");

                        localidades.add(new Localidad(id, descripcion,idProvincia));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(localidades.size()>0){
                    spLocalidad.setVisibility(View.VISIBLE);
                }
                localidades.add(0,new Localidad(-1,getString(R.string.choose_loc)));
                spinnerAdapterLocalidad.setUbicaciones(localidades);
                spLocalidad.setSelection(0); //Lo pone en el primer elemento (Ubicacion inexistente: Elige una localidad)
                progressBar.setVisibility(View.GONE);
            }
        }).execute(URL_LOCALIDAD_BY_PROVINCIA + provincia);
    }
}