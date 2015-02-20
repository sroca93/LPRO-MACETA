package contenedores;

/**
 * Created by simon on 2/20/15.
 */
public class Planta {
    private int ID;
    private String Especie;
    // private FotoActual; TODO formato FotoActual ?¿
    private String[] Timeline;
    private int Dueno;

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

    public int getDueno() {
        return Dueno;
    }

    public void setDueno(int dueno) {
        Dueno = dueno;
    }
}
