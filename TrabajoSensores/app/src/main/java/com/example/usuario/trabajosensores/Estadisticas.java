package com.example.usuario.trabajosensores;

public class Estadisticas {

    private int pasosTotales;
    private int pasosSinEncontrarMetal=0;
    private int metalesEncontrados=0;
    private double longitudUltimoMetal=0;
    private double latitudUltimoMetal=0;
    private static Estadisticas estadisticas = null;

    private Estadisticas(){
        this.pasosTotales=0;
    }

    public static Estadisticas getInstance() {
        if(estadisticas==null){
            estadisticas = new Estadisticas();
        }

        return estadisticas;
    }

    public int getPasos(){
        return this.pasosTotales;
    }

    public void setPasos(int pasos){
        this.pasosTotales=pasos;
    }

    public int getPasosSinEncontrarMetal(){
        return this.pasosSinEncontrarMetal;
    }

    public void setPasosSinEncontrarMetal(int pasos){
        this.pasosSinEncontrarMetal=pasos;
    }

    public void reestablecerPasos(){
        this.pasosSinEncontrarMetal = 0;
    }

    public void setLongitudUltimoMetal(double longitud){
        this.longitudUltimoMetal = longitud;
    }

    public double getLongitudUltimoMetal() {
        return longitudUltimoMetal;
    }

    public void setLatitudUltimoMetal(double latitudUltimoMetal) {
        this.latitudUltimoMetal = latitudUltimoMetal;
    }

    public double getLatitudUltimoMetal() {
        return latitudUltimoMetal;
    }

    public void setMetalesEncontrados(int metales){
        this.metalesEncontrados = metales;
    }

    public int getMetalesEncontrados() {
        return metalesEncontrados;
    }

    public double aMetros(int pasos){
        double metros = (double) pasos * 0.7;
        return metros;
    }


}
