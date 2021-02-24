package com.example.convertidordivisas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.convertidordivisas.MainActivity.pais;

public class mod_valorActivity extends AppCompatActivity {

    private TextView tv_nombreMoneda;
    private TextView tv_valorActual;
    private EditText et_nuevo;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modica_valores);
        tv_nombreMoneda = findViewById(R.id.tv_nomrbeMoneda);
        tv_valorActual  = findViewById(R.id.tv_valorActual);
        et_nuevo = findViewById(R.id.et_nuevoValor);

        Intent intent = getIntent();
        String texto = intent.getExtras().getString("text");
        String nombreMOneda = texto.substring(0,3);
        String valorActual = texto.substring(15);

        tv_nombreMoneda.setText(nombreMOneda);
        tv_valorActual.setText(valorActual);
    }

    public void guardarValores (View view){

        String moneda = tv_nombreMoneda.getText().toString();
        Double valorRecibido = Double.parseDouble(et_nuevo.getText().toString());

        pais.remove(moneda);
        pais.put(moneda,valorRecibido);

        //cierra la act y devulve al menu principal.
        mod_valorActivity.this.finish();

    }

    public void cancelar(View view){
        mod_valorActivity.this.finish();
    }
}//class
