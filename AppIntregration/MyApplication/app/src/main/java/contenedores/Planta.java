package contenedores;

import java.io.Serializable;

/**
 * Created by simon on 2/20/15.
 */
public class Planta implements Serializable {
    private int idPlanta;
    private String Especie;
    // private FotoActual; TODO formato FotoActual ?¿
    private String[] Timeline;
    private String Dueno;

    //Pendientes de pactar en el diagrama de clases
    private String nombrePlanta;
    private float ValoracionMedia;
    private String Tipo;

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    private String Thumbnail;

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }



    public String getNombrePlanta() {
        return nombrePlanta;
    }

    public void setNombrePlanta(String nombrePlanta) {
        this.nombrePlanta = nombrePlanta;
    }

    public float getValoracionMedia() {
        return ValoracionMedia;
    }

    public void setValoracionMedia(int valoracionMedia) {
        ValoracionMedia = valoracionMedia;
    }

    public int getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(int idPlanta) {
        this.idPlanta = idPlanta;
    }

    public String getEspecie() {
        return Especie;
    }

    public void setEspecie(String especie) {
        Especie = especie;
    }

    public String[] getTimeline() {
        return Timeline;
    }

    public void setTimeline(String[] timeline) {
        Timeline = timeline;
    }

    public String getDueno() {
        return Dueno;
    }

    public void setDueno(String dueno) {
        Dueno = dueno;
    }

    @Override
    public String toString() {
        return "Planta{" +
                "ValoracionMedia=" + ValoracionMedia +
                ", nombrePlanta='" + nombrePlanta + '\'' +
                ", Dueno='" + Dueno + '\'' +
                ", Tipo='" + Tipo + '\'' +
                '}';
    }
}
