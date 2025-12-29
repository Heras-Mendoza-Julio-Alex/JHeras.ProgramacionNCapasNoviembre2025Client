package JHeras.ProgramacionNCapasNoviembre2025.ML;

//import jakarta.validation.constraints.Min;

public class Rol {
//    @Min(value = 1,message = "El valor minimo debe ser 1")
    private int idRol;
    private String nombre;
    
  public int getIdRol(){
     return idRol;
 }
 
 public void setIdRol(int idRol){
     this.idRol=idRol;
 }
 
 public String getNombre(){
     return nombre;
 }
 
 public void setNombre(String nombre){
     this.nombre=nombre;
 }
 
 
}
