package contenedores;

/**
 * Created by simon on 2/20/15.
 */
public class Usuario {
    private int ID;
    private String Nombre;
    private int[] Plantas;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int[] getPlantas() {
        return Plantas;
    }

    public void setPlantas(int[] plantas) {
        Plantas = plantas;
    }
}
