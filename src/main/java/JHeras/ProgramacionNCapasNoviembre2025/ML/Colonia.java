package JHeras.ProgramacionNCapasNoviembre2025.ML;

public class Colonia {

    private int idColonia;
    private String nombre;
    private String codigopostal;
    public Municipio municipio;
    
    public Colonia(){
    }
    
//    public Colonia(int idColonia,String nombre,String codigopostal){
//        this.idColonia=idColonia;
//        this.nombre=nombre;
//        this.codigopostal=codigopostal;
//    }
    

    public int getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(int idColonia) {
        this.idColonia = idColonia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getcodigopostal() {
        return codigopostal;
    }

    public void setcodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }
}