package dscont.com.dscont.pe.app_pomodoro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

        Button btn_Play;
        Button btn_Pause;
        Button btn_Stop;
        TextView tv_Tiempo_Transcurrido;
        EditText tb_Nombre_Pomodoro;
        ProgressBar pb_Tiempo_Progreso_Pomodoro;
        AlertDialog.Builder builder_Dialog_Completado;


        private long startTime = 0L;

        private Handler customHandler = new Handler();

        long timeInMilliseconds = 0L;
        long timeSwapBuff = 0L;
        long updatedTime = 0L;
        long stoptime = 0L;
        int Tiempo_Pomodoro;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            Bundle bundle = getIntent().getExtras();

            Tiempo_Pomodoro = bundle.getInt("Tiempo_Pomodoro");

            Toast.makeText(MainActivity.this,String.valueOf(Tiempo_Pomodoro) ,Toast.LENGTH_LONG).show();

            btn_Play = (Button)findViewById(R.id.btn_Play);
            tv_Tiempo_Transcurrido = (TextView)findViewById(R.id.tv_Tiempo_Transcurrido);
            tb_Nombre_Pomodoro = (EditText)findViewById(R.id.tb_Nombre_Pomodoro);
            pb_Tiempo_Progreso_Pomodoro = (ProgressBar)findViewById(R.id.pb_Tiempo_Progreso_Pomodoro);


            //Iniciamos el timer al crear la actividad
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);

            //Deshabilitamos el boton de play
            Deshabilitar_Boton(btn_Play);

        //Textos a mostrar en los snackbar
        String Mensaje_al_Pausear = "No es bueno poner en pausa un Pomodoro";
        String Mensaje_al_Detener = "No es bueno detener un Pomodoro, Realmente desea hacerlo?";

            builder_Dialog_Completado = new AlertDialog.Builder(MainActivity.this);
            DialogInterface.OnClickListener builder_Dialog_CompletadoClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.dismiss();
                            //regresar a la vista anterior
                            Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }
            };
            builder_Dialog_Completado.setTitle("Pomodoro Completado");
            builder_Dialog_Completado.setPositiveButton("Aceptar", builder_Dialog_CompletadoClickListener);

        final AlertDialog.Builder builder_Dialog_Pausear = new AlertDialog.Builder(MainActivity.this);
            DialogInterface.OnClickListener builder_Dialog_PausearClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            };
        builder_Dialog_Pausear.setTitle("Pomodoro en Pausa");
        builder_Dialog_Pausear.setMessage(Mensaje_al_Pausear).setPositiveButton("Aceptar", builder_Dialog_PausearClickListener);


        final AlertDialog.Builder builder_Dialog_Detener = new AlertDialog.Builder(MainActivity.this);
        DialogInterface.OnClickListener builder_Dialog_DetenerClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //regresar a la vista anterior
                        Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        };
        builder_Dialog_Detener.setTitle("Pomodoro Detenido");
        builder_Dialog_Detener.setMessage(Mensaje_al_Detener).setPositiveButton("Aceptar", builder_Dialog_DetenerClickListener);

        btn_Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se habilita el boton de pause y stop y se deshabilita el boton de play
                Habilitar_Boton(btn_Pause);
                Habilitar_Boton(btn_Stop);
                Deshabilitar_Boton(btn_Play);
                //Se inicia o reanuda el timer
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
            }
        });

        btn_Pause = (Button)findViewById(R.id.btn_Pause);
        btn_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder_Dialog_Pausear.show();
                //Se habilita el boton de play y se deshabilita el boton de pause
                Habilitar_Boton(btn_Play);
                Deshabilitar_Boton(btn_Pause);
                //Se pone en pausa el timer
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);

            }
        });

        btn_Stop = (Button)findViewById(R.id.btn_Stop);
        btn_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Se habilita el boton de play y se deshabilita los botones de stop y pause
                Habilitar_Boton(btn_Play);
                Deshabilitar_Boton(btn_Pause);
                Deshabilitar_Boton(btn_Stop);
                //Se pone en pausa el timer y se muestra el snackbar
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
                builder_Dialog_Detener.show();
            }
        });
    }
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + timeInMilliseconds;

                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                int milliseconds = (int) (updatedTime % 1000);
                tv_Tiempo_Transcurrido.setText("Tiempo transcurrido: " + mins + ":"
                        + String.format("%02d", secs) + ":"
                        + String.format("%03d", milliseconds));

                pb_Tiempo_Progreso_Pomodoro.setProgress((mins*60+secs)*100/(Tiempo_Pomodoro*60));
                if(mins<Tiempo_Pomodoro)
                {
                    customHandler.postDelayed(this, 0);
                }else
                {
                    //Mostrar mensaje de pomodoro terminado
                    builder_Dialog_Completado.setMessage(tb_Nombre_Pomodoro.getText().toString());
                    builder_Dialog_Completado.show();
                    final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.alert);
                    mp.start();
                }
        }
    };
    private void Deshabilitar_Boton(Button button)
    {
        button.setEnabled(false);
        button.getBackground().setAlpha(100);
    }
    private void Habilitar_Boton(Button button)
    {
        button.setEnabled(true);
        button.getBackground().setAlpha(255);
    }

}
