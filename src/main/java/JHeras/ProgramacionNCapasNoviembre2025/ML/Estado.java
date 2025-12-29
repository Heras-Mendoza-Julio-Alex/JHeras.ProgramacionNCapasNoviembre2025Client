package JHeras.ProgramacionNCapasNoviembre2025.ML;

import java.util.List;

public class Estado {
    private int idEstado;
    private String nombre;
//    public List<Pais> Paises;
    
    public Pais Pais;
    
    
    public Estado(){
    
}
    public Estado(int idEstado, String nombre){
        this.idEstado=idEstado;
        this.nombre=nombre;
    }
    
  public int getIdEstado(){
     return idEstado;
 }
 
 public void setIdEstado(int idEstado){
     this.idEstado=idEstado;
 }
 
 public String getNombre(){
     return nombre;
 }
 
 public void setNombre(String nombre){
     this.nombre=nombre;
 }

//    public List<Pais> getPaises() {
//        return Paises;
//    }
//
//    public void setPaises(List<Pais> Paises) {
//        this.Paises = Paises;
//    }

    public Pais getPais() {
        return Pais;
    }

    public void setPais(Pais Pais) {
        this.Pais = Pais;
    }
   
}
