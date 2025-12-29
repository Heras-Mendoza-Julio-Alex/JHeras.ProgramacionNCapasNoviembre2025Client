package JHeras.ProgramacionNCapasNoviembre2025.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@RequestMapping("/demo")
public class DemoController {
   
    @GetMapping
    public String Hola(){
        return "Hola";
    }
    
    //hola usuario con path: {nombre}
    @GetMapping("/hola/{nombre}")
    public String HolaP(@PathVariable String nombre, Model model){
        model.addAttribute("nombreusuario", nombre);
        return "index";
    }
    
    @GetMapping("/Suma")
    public String Suma(@RequestParam int NumeroUno, @RequestParam int NumeroDos, Model model) {

        int resultado = NumeroUno + NumeroDos;

        model.addAttribute("resultado", resultado);

        return "Suma";
    }
}
