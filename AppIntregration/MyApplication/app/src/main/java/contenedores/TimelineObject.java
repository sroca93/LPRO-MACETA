package contenedores;

/**
 * Created by Anaro on 04/03/2015.
 */
public class TimelineObject {

    public String Thumbnail;

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String Timestamp;
    public String Texto2;
    public String Texto1;
    public int Tipo;

    public TimelineObject(String thumbnail, int tipo, String texto, String titulo, String timestamp) {
        Thumbnail = thumbnail;
        this.Tipo = tipo;
        this.Texto1 = titulo;
        this.Texto2 = texto;
        this.Timestamp=timestamp;
    }

    @Override
    public String toString() {
        return "TimelineObject{" +
                "Thumbnail='" + Thumbnail + '\'' +
                ", Texto2='" + Texto2 + '\'' +
                ", Texto1='" + Texto1 + '\'' +
                ", Tipo=" + Tipo +
                '}';
    }

    public TimelineObject(int tipo, String texto, String titulo, String thumbnail) {
        this.Tipo = tipo;
        this.Texto1 = titulo;
        this.Texto2 = texto;
        Thumbnail = thumbnail;
    }

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int tipo) {
        this.Tipo = tipo;
    }


    public String getTitulo() {
        return Texto1;
    }

    public void setTitulo(String titulo) {
        this.Texto1 = titulo;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getTexto() {
        return Texto2;
    }

    public void setTexto(String texto) {
        this.Texto2 = texto;
    }



}
