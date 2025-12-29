package JHeras.ProgramacionNCapasNoviembre2025.ML;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Direccion {

    private int idDireccion;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")        
    private String Calle;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")    
    @Pattern(regexp = "^\\d{10}$", message = "Ingresa un numero valido")
    private String NumeroInterior;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")    
    @Pattern(regexp = "^\\d{10}$", message = "Ingresa un numero valido")
    private String NumeroExterior;
    
    public Colonia Colonia;
    
    //public Usuario Usuario;

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

    public Colonia getColonia() {
        return Colonia;
    }
    
    public void setColonia(Colonia Colonia){
        this.Colonia=Colonia;
    }

//    public Usuario getUsuario() {
//        return Usuario;
//    }
//
//    public void setUsuario(Usuario Usuario) {
//        this.Usuario = Usuario;
//    }
    
}
