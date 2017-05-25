package com.example.simonpintado.snortdroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private EditText editHost, editcode,editUser;
    private Button btnConectar;
    ConexionWeb conexion;
    int objeto;
    String items[];
    Switch  aSwitch;
    SharedPreferences  sharedPreferences;
    Context context;
    String host,user;
    boolean sw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Preferences
        final Context context= getApplicationContext();
        sharedPreferences= getSharedPreferences("dataHost",context.MODE_PRIVATE);
        //Load components
        components();


        //Creating sharedPreferences file in mode private
        //the values are DEFAULT
        sharedPreferences=getPreferences(context.MODE_PRIVATE);
        host= sharedPreferences.getString("host","");
        user=sharedPreferences.getString("user","");
        sw=sharedPreferences.getBoolean("sw",false);

        //Recovering the data
        editHost.setText(host);
        editUser.setText(user);
        aSwitch.setChecked(sw);






        btnConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Save SharedPreferences
                sharedPreferences=getPreferences(context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("host",editHost.getText().toString());
                editor.putString("user",editUser.getText().toString());
                editor.putBoolean("sw",sw);
                editor.commit(); editor.clear();


    if(editUser.getText().toString().isEmpty() || editHost.getText().toString().isEmpty() || editcode.getText().toString().isEmpty()
            || editUser.getText().toString().isEmpty() && editHost.getText().toString().isEmpty() && editcode.getText().toString().isEmpty() )
    {
        Toast.makeText(MainActivity.this,R.string.Incomplete_fields,Toast.LENGTH_SHORT).show();
        System.out.println("falta algun campo");

    } else {



        conexion = new ConexionWeb(MainActivity.this);
        conexion.limpiarVariables();
        conexion.agregarVariables("host", editHost.getText().toString());
        conexion.agregarVariables("user", editUser.getText().toString());
        conexion.agregarVariables("pass", editcode.getText().toString());


        try {
            conexion.execute(new URL("http://simonpruebacideti.ddns.net/HostLocal_Android/capturaPersona_simon.php"));
            objeto = 1;

        } catch (MalformedURLException e) {
            AlertDialog.Builder m = new AlertDialog.Builder(MainActivity.this);
            m.setTitle("Error en Consultar").setMessage("La direccion web no esta bien escrita o bien, no existe servidor/hosting ").show();

        }//catch
     }//else
    }//onClick
        }); //onClickListener

    }//OnCreate




    //Receive the response of the remote server
    public void mensajes (String str){
       ;
        String arreglophp = str;
        items = arreglophp.split(",");
            AlertDialog.Builder me = new AlertDialog.Builder(MainActivity.this);
            me.setTitle(R.string.connect_server).setMessage(MainActivity.this.getString(R.string.info_connection)+"\n"+"\n"+
                    "Host: "+editHost.getText().toString()+"\n"+"User: "+editUser.getText().toString()+"\n"+"Password: "+editcode.getText().toString())
                    .setPositiveButton(R.string.yes_connect, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(MainActivity.this, ConsultaLista.class);
                            i.putExtra("arreglo", items);
                            startActivity(i);

                        }
                    })
                    .setNegativeButton(R.string.no_change, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
    }


    private void components(){
        editHost=(EditText)findViewById(R.id.edithost);
        editcode=(EditText)findViewById(R.id.editcode);
        editUser=(EditText)findViewById(R.id.editUser);
        btnConectar=(Button)findViewById(R.id.btnConectar);
        aSwitch=new Switch(this);
        aSwitch.setText("        "+MainActivity.this.getString(R.string.switchMessage)); //*********************************************
    }


    //Options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
               ajustes();
                return true;
            case R.id.item2:
                Intent i =new Intent(MainActivity.this,About.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }//switch
    }//onOptionsItemSelected


    public void ajustes(){

        //Toast.makeText(MainActivity.this,valueSw,Toast.LENGTH_SHORT).show();
        //Save SharedPreferences
        sharedPreferences=getPreferences(context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();

        AlertDialog.Builder me = new AlertDialog.Builder(MainActivity.this);
        me.setTitle(R.string.settings)
                .setView(aSwitch)
                .setMessage(R.string.message_Alert)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    if(aSwitch.isChecked()){
                        editor.putBoolean("sw",true);
                        editor.commit();

                    }else{
                        editor.putBoolean("sw",false);
                        editor.commit();
                    }

                    }//onclick
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
editor.clear();
    }

}//class

//https://androidstudiofaqs.com/tutoriales/como-usar-git-en-android-studio