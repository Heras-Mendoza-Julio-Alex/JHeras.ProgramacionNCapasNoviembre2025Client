package JHeras.ProgramacionNCapasNoviembre2025.ML;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;


public class Usuario {   
    private int idUsuario;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")    
    @Pattern(regexp = "[a-zA-Z]+", message = "Ingresa solo letras")    
    private String Nombre;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")    
    @Pattern(regexp = "[a-zA-Z]+", message = "Ingresa solo letras")    
    private String ApellidoPaterno;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")    
    @Pattern(regexp = "[a-zA-Z]+", message = "Ingresa solo letras")    
    private String ApellidoMaterno;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")    
    @Pattern(regexp = "^\\d{10}$", message = "Ingresa un numero valido")
    private String Telefono;
    
    @NotNull(message = "El campo es requerido")
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
    private Date fechanacimiento;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")    
       
    private String Username;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")    
    @Pattern(regexp = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$", message = "Ingresa un correo valido")    
    private String Email;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido") 
    private String Sexo;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")    
    @Pattern(regexp = "^\\d{10}$", message = "Ingresa un numero valido")
    private String Celular;
        
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")    
    @Pattern(regexp = "^[A-Z]{4}[0-9]{6}[A-Z]{7}[0-9]{1}$", message = "Ingresa una curp valida")
    private String Curp;
    
    @NotEmpty(message = "El campo es requerido")
    @NotNull(message = "El campo es requerido")    
    @Pattern(regexp = "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,}$", message = "Ingresa una contraseña de mínimo 8 dígitos con al menos una mayúscula y número.")
    private String Password;
    
    private Number estatus;
    
    private String Imagen;
    
    public Rol Rol;  
    public List<Direccion> Direcciones;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String ApellidoPaterno) {
        this.ApellidoPaterno = ApellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String ApellidoMaterno) {
        this.ApellidoMaterno = ApellidoMaterno;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public Date getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String Sexo) {
        this.Sexo = Sexo;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }

    public String getCurp() {
        return Curp;
    }

    public void setCurp(String Curp) {
        this.Curp = Curp;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
    
   
  
    
    public Rol getRol(){
        return Rol;
    }
    
    public void setRol(Rol Rol){
        this.Rol=Rol;
    }
    
    public List<Direccion> getDirecciones(){
        return Direcciones;
    }
    
    public void setDirecciones(List<Direccion> Direcciones){
        this.Direcciones=Direcciones;
    }

    public Number getEstatus() {
        return estatus;
    }

    public void setEstatus(Number estatus) {
        this.estatus = estatus;
    }
    
    public String getImagen(){
       return Imagen;
    }
    
    public void setImagen(String Imagen){
        this.Imagen=Imagen;
    }
        
}
