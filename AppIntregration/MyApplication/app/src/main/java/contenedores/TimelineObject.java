package contenedores;

/**
 * Created by Anaro on 04/03/2015.
 */
public class TimelineObject {

    public String Thumbnail;
    public String titulo;
    public String texto;
    public int tipo;

    public TimelineObject(String thumbnail, int tipo, String texto, String titulo) {
        Thumbnail = thumbnail;
        this.tipo = tipo;
        this.texto = texto;
        this.titulo = titulo;
    }

    public TimelineObject(int tipo, String texto, String titulo, String thumbnail) {
        this.tipo = tipo;
        this.texto = texto;
        this.titulo = titulo;
        Thumbnail = thumbnail;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }



}
