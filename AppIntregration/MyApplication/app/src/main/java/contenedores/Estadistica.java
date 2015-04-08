
package contenedores;

public class Estadistica{
    private String Humedad;
    private String Luminosidad;
    private String Temperatura;
    private String Timestamp;

    public String getHumedad(){
        return this.Humedad;
    }
    public void setHumedad(String humedad){
        this.Humedad = humedad;
    }
    public String getLuminosidad(){
        return this.Luminosidad;
    }
    public void setLuminosidad(String luminosidad){
        this.Luminosidad = luminosidad;
    }
    public String getTemperatura(){
        return this.Temperatura;
    }
    public void setTemperatura(String temperatura){
        this.Temperatura = temperatura;
    }
    public String getTimestamp(){
        return this.Timestamp;
    }
    public void setTimestamp(String timestamp){
        this.Timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Estadistica{" +
                "Humedad='" + Humedad + '\'' +
                ", Luminosidad='" + Luminosidad + '\'' +
                ", Temperatura='" + Temperatura + '\'' +
                ", Timestamp='" + Timestamp + '\'' +
                '}';
    }




}

