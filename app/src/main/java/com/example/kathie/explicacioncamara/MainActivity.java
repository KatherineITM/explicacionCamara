package com.example.kathie.explicacioncamara;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //Ruta donde se van a guardar las fotos
    private final String ruta_fotos_interna = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";
    //Archivo que se crea con la ruta establecida
    private File file_interna = new File(ruta_fotos_interna);


    //Botones
    private Button botonCamara;
    private Button botonGaleria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Botones
        botonCamara = (Button) findViewById(R.id.btnTomarFoto);
        botonGaleria = (Button)findViewById(R.id.btnAbrirGaleria);

        //crea la carpeta si no existe
        file_interna.mkdirs();
        mensaje("interna");

        //Acciones o lógica de los botones
        //botonGaleia
        botonGaleria.setOnClickListener(new View.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(View v){
                                                int SELECT_PICTURE = 1;
                                                Intent intent = new Intent();
                                                intent.setType("image/*");
                                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                                startActivityForResult(Intent.createChooser(intent,
                                                        "Select Picture"), SELECT_PICTURE);
                                            }
                                        }
        );

        //botonCamara
        botonCamara.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String file ;
                File mi_foto ;

                file = ruta_fotos_interna + getCode() + ".jpg";
                mi_foto = new File( file );
                try {
                    mi_foto.createNewFile();
                    mensaje("Foto guardada en memoria interna.");
                }catch (IOException e){
                    Log.e("ERROR ", "Error:" + e);
                }

                Uri uri = Uri.fromFile( mi_foto );

                //Abre la camara para tomar la foto*
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //Guarda la foto
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                //Retorna a la actividad y habilita el botón de galería*
                startActivityForResult(cameraIntent, 0);
                botonGaleria.setVisibility(View.VISIBLE);
            }
        });
    }

    //Método que me devuelve un código para la foto, junto con la hora y la fecha del sistema operativo
    @SuppressLint("SimpleDateFormat")
    private String getCode()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        //Crea la nueva fecha con el formato establecido en la línea anterior
        String date = dateFormat.format(new Date() );
        //Se establece el código de la foto
        String photoCode = "picture_" + date;
        return photoCode;
    }


    public void mensaje(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    }

