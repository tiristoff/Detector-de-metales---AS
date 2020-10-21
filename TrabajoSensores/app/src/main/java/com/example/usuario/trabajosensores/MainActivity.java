package com.example.usuario.trabajosensores;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    int cont = 0;
    double magnitude;
    double latitud = 0;
    double longitud = 0;
    LineGraphSeries<DataPoint> datos;
    private Handler mHandler = new Handler();

    private MediaPlayer pip;
    private int loop = 0;
    boolean interruptor = true;
    boolean metalEncontrado = false;
    Estadisticas estadisticas = Estadisticas.getInstance();
    LocationManager locationManager;

    public void onCreate(Bundle savedInstanceState) {
        final Activity esta = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView valorX = this.findViewById(R.id.ValorTotal2);
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        final Sensor magnetismo = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        SensorManager sm2 = (SensorManager) getSystemService(SENSOR_SERVICE);
        final Sensor o = sm2.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Formato de los decimales
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        final DecimalFormat df = new DecimalFormat("#.0", symbols);

        final ProgressBar pg = this.findViewById(R.id.progresBar1);


        final GraphView grafico = findViewById(R.id.graph);
        final ImageButton cmdImg = this.findViewById(R.id.ImagenSonido);
        datos = new LineGraphSeries<>();

        grafico.getViewport().setMinX(0);
        grafico.getViewport().setMaxX(100);
        grafico.getViewport().setXAxisBoundsManual(true);

        grafico.getViewport().setMinY(0);
        grafico.getViewport().setMaxY(350);
        grafico.getViewport().setYAxisBoundsManual(true);
        cmdImg.setBackgroundResource(R.drawable.speaker);
        pip = MediaPlayer.create(getApplicationContext(), R.raw.pip);
        //pip.setVolume(50,50);
        grafico.addSeries(datos);

        //Cambiar fuentes
        String  ruta = "fonts/BebasNeue-Regular.ttf";
        Typeface fuente = Typeface.createFromAsset(getAssets(),ruta);

        datos.setThickness(10);

        TextView txtTeslas = this.findViewById(R.id.ValorTotal);
        TextView teslas = this.findViewById(R.id.teslas);
        valorX.setTypeface(fuente);
        txtTeslas.setTypeface(fuente);
        teslas.setTypeface(fuente);


        //Listener de imagen
        View.OnClickListener listerCmd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((loop % 2) == 0) {
                    cmdImg.setBackgroundResource(R.drawable.speakeroff);
                    interruptor = false;

                } else if ((loop % 2 != 0)) {
                    cmdImg.setBackgroundResource(R.drawable.speaker);
                    interruptor = true;
                }
                loop++;
            }
        };
        cmdImg.setOnClickListener(listerCmd);

        //Listener de sensor de magnetismo
        SensorEventListener listener = new SensorEventListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                //Halla el modulo del campo magnetico
                magnitude = Math.sqrt((x * x) + (y * y) + (z * z));
                valorX.setText(df.format(magnitude));
                int progress = (int) magnitude;
                pg.setProgress(progress * 100 / 350);

                cont++;

                if (magnitude > 225) {
                    if (!metalEncontrado) {
                        estadisticas.setMetalesEncontrados(estadisticas.getMetalesEncontrados() + 1);
                        estadisticas.reestablecerPasos();

                        LocationListener listenerx = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {

                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        };
                        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, listenerx);
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        estadisticas.setLatitudUltimoMetal(latitude);
                        estadisticas.setLongitudUltimoMetal(longitude);
                        metalEncontrado = true;
                    }
                } else {
                    metalEncontrado = false;
                }
                //Frecuencia de pitido
                if (interruptor) {
                    if (magnitude > 0 && magnitude <= 50) {
                        if (cont % 17 == 0) {
                            pip.start();
                        }
                    } else if (magnitude > 50 && magnitude <= 100) {
                        if (cont % 11 == 0) {
                            pip.start();
                        }
                    } else if (magnitude > 100 && magnitude <= 150) {
                        if (cont % 5 == 0) {
                            pip.start();
                        }
                    } else {
                        pip.start();
                    }
                }

                addRandomDataPoint((int) magnitude);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sm.registerListener(listener, magnetismo, SensorManager.SENSOR_DELAY_NORMAL);


        //Listener de sensor de pasos
        SensorEventListener listener2 = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (magnitude < 225) {
                    estadisticas.setPasos((int) event.values[0]);
                    estadisticas.setPasosSinEncontrarMetal(estadisticas.getPasosSinEncontrarMetal() + 1);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sm2.registerListener(listener2, o, SensorManager.SENSOR_DELAY_NORMAL);


        Button btnEstadisticas = this.findViewById(R.id.btnEstadisticas);
        btnEstadisticas.setTypeface(fuente);
        View.OnClickListener lanzarEstadisticas = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EstadisticasActivity.class);
                startActivity(intent);
            }
        };
        btnEstadisticas.setOnClickListener(lanzarEstadisticas);


    }

    public void addRandomDataPoint(final int magnitud) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                datos.appendData(new DataPoint(cont + 5, magnitud), true, 1000);

            }
        }, 100);
    }

}