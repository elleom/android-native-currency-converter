package com.example.convertidordivisas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //creacion de variables/componentes
    private Double valorConversion = 0.0;
    private EditText et_valor;
    private TextView tv_currencyShort;
    static HashMap<String, Double> pais = new HashMap<String, Double>();
    private TextView tv_tasaConversion;
    private Button bt_calcula_reinicia;
    private Switch sw_euro;
    private Button bt_lista;

    //Key para el Map
    final String[] monedaPaises = {"DKK",
            "NOK",
            "BRL",
            "CNY",
            "USD",
            "GBP",
            "JPY",
            "SEK",
            "CAD",
            "RUB",
            "ZAR",
            "INR"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_valor = findViewById(R.id.et_valor);
        tv_currencyShort = findViewById(R.id.tv_currencyShort);
        tv_tasaConversion = findViewById(R.id.tv_tasaConversion);
        bt_calcula_reinicia = findViewById(R.id.bt_convertir);
        sw_euro=findViewById(R.id.sw_euro);
        bt_lista = findViewById(R.id.bt_lista);


        new conexionSegundoPlano().execute("https://api.exchangeratesapi.io/latest");

    }

    private class conexionSegundoPlano extends AsyncTask<String, Void, JSONObject>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bt_calcula_reinicia.setEnabled(false);
            bt_lista.setEnabled(false);

        }

        @Override
        protected JSONObject doInBackground(String... servidor) {
            // proceso de recuperacion de JSON
            JSONObject resultado = new JSONObject();
            HttpURLConnection conexion = null;

            //conexion al URL
            try {
                URL url = new URL(servidor[0]);
                conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestMethod("GET");
                conexion.connect();
                Log.e("OK","Conexion Establecida");

                //convertir el Stream de datos

                InputStream entrada = conexion.getInputStream(); //download
                BufferedReader lectura =
                        new BufferedReader(new InputStreamReader(entrada));

                StringBuffer buffer = new StringBuffer();

                String linea = "";
                while ((linea = lectura.readLine()) != null) {
                    buffer.append(linea + "\n");
                }

                resultado = new JSONObject(buffer.toString());

                lectura.close();
            } catch (Exception e) {
                    Log.e("dib_error",e.getMessage());
            } finally {
                conexion.disconnect();
                return resultado;
            }
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            // asignar el objeto JSON a HasMap "pais"
            try {

                for (int i=0;i < monedaPaises.length;i++  ){
                    String nombrePais = json.getJSONObject("rates").getString(monedaPaises[i]);
                    pais.put(monedaPaises[i],Double.parseDouble(nombrePais));
                }
//
            } catch (Exception e) {
                Log.e("oP_error", e.getMessage());
            }

            //JSON to HashMap de paises;
            bt_calcula_reinicia.setEnabled(true);
            bt_lista.setEnabled(true);
        }
    }


    public void iniciarSegundaActividad(View v){
        Intent intent = new Intent(this, ListActivity.class );
        intent.putExtra("datos", pais);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {

        try {
            //muestra la moneda seleccionada
            tv_currencyShort.setText(v.getContentDescription().toString());

            //establece el valor de conversion del euro contra la moneda seleccionada
            valorConversion = pais.get(v.getContentDescription().toString());

            //muestra la relacion de la moneda seleccionada contra el Euro

            String tasaconversion = pais.get(v.getContentDescription()).toString();
            String nombreMoneda = tv_currencyShort.getText().toString();
            tv_tasaConversion.setText("1€ = " + tasaconversion+" "+nombreMoneda);

            if (sw_euro.isChecked()){
                tv_currencyShort.setText("€");
            }

        } catch(NullPointerException e){
            Toast.makeText(MainActivity.this, "Servicio NO Disponible, inténtelo más tardee",Toast.LENGTH_LONG).show();
        }

    }

    public void calcula(View v) {

        //realiza la conversion
        if (bt_calcula_reinicia.getText().toString().equalsIgnoreCase("CONVERTIR")) {

            //muestra un msj (Toast) si no se a introducido ningun valor

            String etCompleta = et_valor.getText().toString();
            String monedaSeleccionada= tv_tasaConversion.getText().toString();

            if (monedaSeleccionada.matches("")){

                Toast toast = Toast.makeText(MainActivity.this,"Selecciona Moneda",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,250);
                toast.show();
            }
            else{

                if (etCompleta.matches("")){
                    Toast toast =
                    Toast.makeText(MainActivity.this,"Introduce Importe",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,250);
                    toast.show();

                }

                //se utiliza la clase "Double" y no el valor primitivo "double" para poder acceder al
                // metodo toString().

                else{

                    Double resultado;

                    double importeIngresado = Double.parseDouble(et_valor.getText().toString());

                    if (sw_euro.isChecked()){
                        resultado = importeIngresado * valorConversion;


                    }

                    else {
                        resultado = importeIngresado / valorConversion;
                        tv_currencyShort.setText("€");
                    }

                    et_valor.setText(resultado.toString());

                    bt_calcula_reinicia.setText("REINICIAR");
                }
            }

        //"reinicia" el App
        } else {
            et_valor.setText("");
            tv_currencyShort.setText("");
            tv_tasaConversion.setText("");
            bt_calcula_reinicia.setText("CONVERTIR");

        }
    }

    public void switchControl(View v){

        tv_tasaConversion.setText("");

        if (sw_euro.isChecked()){
            tv_currencyShort.setText("€");
        }
        else{
            tv_currencyShort.setText("");

        }

    }
} //class

