package com.example.convertidordivisas;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListActivity extends AppCompatActivity {

    private ListView lv_lista;
    HashMap<String,Double> nombreCambio_hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Intent intent = getIntent();


        nombreCambio_hash = (HashMap<String,Double>)intent.getSerializableExtra("datos");
        lv_lista = findViewById(R.id.lv_divisas);

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cargarValores(nombreCambio_hash));
        lv_lista.setAdapter(adaptador);

        lv_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int posicion, long id) {

                //pruba para identificar que boton ha sido presionado
                //Toast.makeText(ListActivity.this, "Clicked"+cargarValores(nombreCambio_hash).get(posicion), Toast.LENGTH_LONG).show();

                //@llama a un nuevo activity donde se puede sobreescribir el valor
                Intent intent = new Intent(ListActivity.this, mod_valorActivity.class);
                String valorXposicion = cargarValores(nombreCambio_hash).get(posicion);
                intent.putExtra("text", valorXposicion);
                startActivity(intent);

                //cierra la actividad luego de llamar al proximo intent
                ListActivity.this.finish();
            }
        });

    }

    public List<String> cargarValores(HashMap<String,Double> nombreCambio_hash){

        List<String> listaStrings = new ArrayList<>();

        for (HashMap.Entry<String,Double>elementos : nombreCambio_hash.entrySet()){
            String llave_Hash = elementos.getKey();
            String valor_Hash  = elementos.getValue().toString();
            listaStrings.add(llave_Hash+"                "+valor_Hash+"\t x €");

        }

        return listaStrings;
    }

}//class
