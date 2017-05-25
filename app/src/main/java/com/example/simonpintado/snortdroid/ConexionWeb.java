package com.example.simonpintado.snortdroid;

/**
 * Created by Simon Pintado on 15/05/2017.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 06/05/2016.
 */
public class ConexionWeb extends AsyncTask<URL,Void,String>  {
    List<String[]> variables;
    MainActivity puntero; //recibe mensajes
    //formato de POST
    public ConexionWeb(MainActivity p){ //parametrizado de mensaje
        variables=new ArrayList<String[]>();
        puntero=p; //puntero
    }

    public void agregarVariables(String identificador,String dato){ //agregado de duplas, nombre y dato NOMBRE [0] dato[1]
        //Se crea la dupla clave en celdilla 0 y valor en celdilla1
        String[] temp={identificador,dato};
        //se guarda en el LIST para al final ser concatenado en formato POST
        variables.add(temp);
    }

    public void limpiarVariables(){
        variables=new ArrayList<String[]>();
    }
    @Override
    protected String doInBackground(URL... params) {
        //1. crear la secuancia POST.
        //2. conectarse con el SERVIDOR.
        //3. Preparar la conexion para simular un Navegador.
        //4. Crear un flujo de salida de datos (es decir, enviar la info).
        //5. Crear un flujo de ENTRADA de datos( es decir, LEER la info).
        //6. Mostrar la respuesta leida desde el servidor.

        String POST="";
        String respuesta="";

        for(int i=0;i<variables.size();i++){ //recorrido de elementos en variables
            //Recuperas el contenido de la posicion
            String[] temp=variables.get(i);
            try {
                //se codifican los datos Identificador[0] y dato[1]
                POST+=temp[0]+"="+ URLEncoder.encode(temp[1],"UTF-8")+" ";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }//catch
        }//for
        //clave=valor&clave=valor&
        POST=POST.trim(); //Quita los espacios
        POST=POST.replace(" ", "&"); //remplaza todos los espacios en blanco por &

        HttpURLConnection conexion=null; //freeBar
        try{
            conexion=(HttpURLConnection)params[0].openConnection();
            //preparar la conexion para simular el navegador
            conexion.setDoOutput(true); //Activa el modo de envio POST
            conexion.setFixedLengthStreamingMode(POST.length()); //Indico cantidad de LETRAS a ENVIAR
            conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); //pone la conexion como si fuera un formulario por tanto simula ser un navegador

            //Creando salida de datos
            OutputStream flujoSalidaDatos=new BufferedOutputStream(conexion.getOutputStream());

            flujoSalidaDatos.write(POST.getBytes()); //AQUI es donde en verdad se manda el POST
            flujoSalidaDatos.flush();
            flujoSalidaDatos.close();

            if(conexion.getResponseCode()==200){ //el servidor acepto los datos
                InputStreamReader input =new InputStreamReader(conexion.getInputStream(),"UTF-8");
                BufferedReader flujoEntradaDatos =new BufferedReader(input);
                String linea="";

                do{
                    linea=flujoEntradaDatos.readLine();
                    if(linea!=null){
                        respuesta+=linea; //se acumulan todas las lineas de respuesta
                    }
                }while(linea!=null); //PONER ==
            }else{
                respuesta="Error_404_1"; //PHP no respondio Error 404_1

            }

        }catch(UnknownHostException e){
            return "Error_404_2"; //Host no esta activado Error 404_2
        } catch(IOException e){
            return "Error_404_3"; //Flujo de E/S no funciono! Error 404_3
        }finally {
            //no se pudo realizar el openConection
            if(conexion!=null){
                conexion.disconnect();
            }
        }
        return respuesta;
    }//doInBackground

    protected void onPostExecute(String respuesta){
        //mandar llamar un metodo del activity
        if(puntero.objeto==1) {
            puntero.mensajes(respuesta);
        }
        /*
        if(puntero.objeto==2){

            puntero.ponerEtiqueta(respuesta);
        }
        */
    }


}//clsss
