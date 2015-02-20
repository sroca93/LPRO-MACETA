package contenedores;

import java.util.Arrays;

/**
 * Created by simon on 2/20/15.
 */
public class Planta {
    private int ID;
    private String Especie;
    // private FotoActual; TODO formato FotoActual ?¿
    private String[] Timeline;
    private String Dueno;

    //Pendientes de pactar en el diagrama de clases
    private String nombrePlanta;
    private int ValoracionMedia;

    public String getNombrePlanta() {
        return nombrePlanta;
    }

    public void setNombrePlanta(String nombrePlanta) {
        this.nombrePlanta = nombrePlanta;
    }

    public int getValoracionMedia() {
        return ValoracionMedia;
    }

    public void setValoracionMedia(int valoracionMedia) {
        ValoracionMedia = valoracionMedia;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
                '}';
    }
}
