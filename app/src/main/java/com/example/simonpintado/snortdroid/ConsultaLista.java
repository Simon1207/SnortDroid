package com.example.simonpintado.snortdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.support.v7.appcompat.R.styleable.MenuItem;

public class ConsultaLista extends AppCompatActivity {
private ListView lista;


    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_lista);
        lista=(ListView)findViewById(R.id.consultaLista);


        Bundle extras = getIntent().getExtras();
        String array[] = extras.getStringArray("arreglo");


        adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.fila_lista,R.id.nombre_fila_lista,array);
        lista.setAdapter(adapter);



    }//onCreate



}
