package com.joorpe.ejercicio_localidades;

import android.os.AsyncTask;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetAsync extends AsyncTask<String,String, String> {
    private static final String TAG = "TAG_" + GetAsync.class.getName();

    IAsyncGet iAsyncGet;
    String url;
    @Override
    protected String doInBackground(String... urls) {
        return Utilidades.ObtenerDatos(urls[0]);
    }

    public GetAsync(IAsyncGet iAsyncGet) {
        super();
        this.iAsyncGet = iAsyncGet;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject root = new JSONObject(s);
            String status = root.getString("STATUS");

            if(status.compareTo("OK")==0){
                iAsyncGet.onFinish(root.getJSONArray("DATA"));
            }else{
                Log.e(TAG,"Error al obtener el recuerso");
                iAsyncGet.onFinish(new JSONArray("[]"));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

    }

    public interface IAsyncGet{
        public void onFinish(JSONArray ubicaciones);
    }
}