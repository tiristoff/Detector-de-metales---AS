package com.example.usuario.trabajosensores;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class EstadisticasActivity extends Activity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estadisticas);

        Estadisticas estadisticas = Estadisticas.getInstance();
        TextView txt = this.findViewById(R.id.txt1);
        TextView txt2 = this.findViewById(R.id.txt2);
        TextView txt3 = this.findViewById(R.id.txt3);
        TextView txt4 = this.findViewById(R.id.txt5);
        TextView txt5 = this.findViewById(R.id.txt6);
        TextView txt6 = this.findViewById(R.id.txt7);
        TextView txt7 = this.findViewById(R.id.txt8);
        TextView txtPasos = findViewById(R.id.txtPasos);
        TextView txtMetros = findViewById(R.id.txtMetros);
        TextView txtMetales = findViewById(R.id.txtMetales);
        TextView txtPasosSEM = this.findViewById(R.id.txtPasosSinEncontrarMetal);
        TextView txtLatitud = this.findViewById(R.id.txtLatitud);
        TextView txtLongitud = this.findViewById(R.id.txtLongitud);
        ImageView pasos = this.findViewById(R.id.pasos);
        ImageView metal = this.findViewById(R.id.metal);

        metal.setImageResource(R.drawable.iron);
        pasos.setImageResource(R.drawable.foot);
        //CAMBIAR FUENTE
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        final DecimalFormat df = new DecimalFormat("#.0", symbols);

        txtPasos.setText(String.valueOf(estadisticas.getPasos()));
        txtMetales.setText(String.valueOf(estadisticas.getMetalesEncontrados()));
        txtPasosSEM.setText(String.valueOf(estadisticas.getPasosSinEncontrarMetal()));
        txtMetros.setText(df.format(estadisticas.aMetros(estadisticas.getPasosSinEncontrarMetal())));
        txtLatitud.setText(df.format(estadisticas.getLatitudUltimoMetal()));
        txtLongitud.setText(df.format(estadisticas.getLongitudUltimoMetal()));



        String ruta = "fonts/BebasNeue-Regular.ttf";
        Typeface fuente = Typeface.createFromAsset(getAssets(),ruta);

        txtPasos.setTypeface(fuente);
        txtMetros.setTypeface(fuente);
        txt.setTypeface(fuente);
        txt2.setTypeface(fuente);
        txt3.setTypeface(fuente);
        txt4.setTypeface(fuente);
        txt5.setTypeface(fuente);
        txt6.setTypeface(fuente);
        txt7.setTypeface(fuente);
        txtLatitud.setTypeface(fuente);
        txtLongitud.setTypeface(fuente);
        txtMetales.setTypeface(fuente);
        txtPasosSEM.setTypeface(fuente);

        Button btnMapa = this.findViewById(R.id.btnMapa);
        btnMapa.setTypeface(fuente);
        View.OnClickListener lanzarMapa = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        };
        btnMapa.setOnClickListener(lanzarMapa);
    }
}
