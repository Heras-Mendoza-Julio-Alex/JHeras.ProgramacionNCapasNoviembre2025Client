package JHeras.ProgramacionNCapasNoviembre2025.ML;

public class Municipio {

    private int idMunicipio;
    private String nombre;
    public Estado Estado;
    
    public Municipio(){}
    
    public Municipio(int idMunicipio, String nombre){
        this.idMunicipio=idMunicipio;
        this.nombre=nombre;
    }

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Estado getEstado() {
        return Estado;
    }

    public void setEstado(Estado Estado) {
        this.Estado = Estado;
    }
    
}
