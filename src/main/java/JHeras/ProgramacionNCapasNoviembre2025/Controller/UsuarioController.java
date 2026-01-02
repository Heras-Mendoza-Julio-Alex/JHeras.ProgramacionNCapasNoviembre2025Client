package JHeras.ProgramacionNCapasNoviembre2025.Controller;

import JHeras.ProgramacionNCapasNoviembre2025.ML.Direccion;
import JHeras.ProgramacionNCapasNoviembre2025.ML.ErrorCarga;
import JHeras.ProgramacionNCapasNoviembre2025.ML.Estado;
import JHeras.ProgramacionNCapasNoviembre2025.ML.Pais;
import JHeras.ProgramacionNCapasNoviembre2025.ML.Result;
import JHeras.ProgramacionNCapasNoviembre2025.ML.Rol;
import JHeras.ProgramacionNCapasNoviembre2025.ML.Usuario;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private static final String urlBase = "http://localhost:8080/api/";

    @GetMapping
    public String GetAll(Model model) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Usuario>>> responseEntity
                = restTemplate.exchange(urlBase + "usuario", HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Usuario>>>() {
                });

        ResponseEntity<Result<List<Rol>>> responseEntityRol
                = restTemplate.exchange(urlBase + "/rol", HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Rol>>>() {
                });

        if (responseEntity.getStatusCode().value() == 200) {
            Result result = responseEntity.getBody();
            Result resultRol = responseEntityRol.getBody();
            model.addAttribute("Usuarios", result.object);
            model.addAttribute("usuarioBusqueda", new Usuario());
            model.addAttribute("Roles", resultRol.object);
        }

        return "Usuario";
    }

    @GetMapping("form")
    public String Form(Model model) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Rol>>> responseEntityRol
                = restTemplate.exchange(urlBase + "/rol", HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Rol>>>() {
                });

        Result resultRol = responseEntityRol.getBody();
        model.addAttribute("Roles", resultRol.object);

        ResponseEntity<Result<List<Pais>>> responseEntityPais
                = restTemplate.exchange(urlBase + "/pais", HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Pais>>>() {
                });

        Result resultPais = responseEntityPais.getBody();
        model.addAttribute("Paises", resultPais.object);

        Usuario usuario = new Usuario();
        usuario.Direcciones = new ArrayList<>();
        usuario.Direcciones.add(new Direccion());

        model.addAttribute("Usuario", usuario);
        return "FormUsu";
    }

    @PostMapping("/add")
    public String addUsuario(@Valid @ModelAttribute("Usuario") Usuario usuario, BindingResult bindingResult,
            @RequestParam("imagenFile") MultipartFile imagenFile, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("Usuario", usuario);
            return "/FormUsu"; // Vuelve al formulario si hay errores
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // Parte 1: El objeto Usuario 
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> usuarioPart = new HttpEntity<>(usuario, jsonHeaders);
        body.add("usuario", usuarioPart);

        // Parte 2: El archivo (si existe)
        if (imagenFile != null && !imagenFile.isEmpty()) {
            body.add("Imagen", imagenFile.getResource());
        }

        // 3. Crear la petición completa
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                    urlBase + "usuario/add",
                    HttpMethod.POST,
                    requestEntity,
                    Result.class
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/usuario";
    }

    @GetMapping("detail/{IdUsuario}")
    public String Detail(@PathVariable("IdUsuario") int IdUsuario, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<Usuario>> responseEntity
                = restTemplate.exchange(urlBase + "usuario/" + IdUsuario, HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<Usuario>>() {
                });

        ResponseEntity<Result<List<Rol>>> responseEntityRol
                = restTemplate.exchange(urlBase + "/rol", HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Rol>>>() {
                });

        Result resultRol = responseEntityRol.getBody();

        ResponseEntity<Result<List<Pais>>> responseEntityPais
                = restTemplate.exchange(urlBase + "/pais", HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Pais>>>() {
                });

        Result resultPais = responseEntityPais.getBody();

        if (responseEntity.getStatusCode().value() == 200) {
            Result result = responseEntity.getBody();

            model.addAttribute("Roles", resultRol.object);

            model.addAttribute("Paises", resultPais.object);

            model.addAttribute("usuario", result.object);
        }

        return "UsuarioDetail";
    }

    @PostMapping("/editar")
    public String updateUsuario(
            //            @Valid
            @ModelAttribute("Usuario") Usuario usuario,
            BindingResult bindingResult,
            Model model) {

//        if (bindingResult.hasErrors()) {
//            model.addAttribute("Usuario", usuario);
//            return "/FormUsu"; // Vuelve al formulario si hay errores
//        }
        int idUsuario = usuario.getIdUsuario();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);

        try {
            String urlUpdate = urlBase + "usuario/" + idUsuario;

            ResponseEntity<Result> response = restTemplate.exchange(
                    urlUpdate,
                    HttpMethod.PUT,
                    requestEntity,
                    Result.class
            );

            if (response.getBody() != null && response.getBody().Correct) {
                System.out.println("Usuario actualizado correctamente sin imagen");
            }

        } catch (Exception e) {
            System.err.println("Error al llamar a la API de Update: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/usuario/detail/" + idUsuario;
    }

    @PostMapping("/search")
    public String BuscarUsuarios(@ModelAttribute("UsuariosBusqueda") Usuario usuario, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Rol>>> responseEntityRol
                = restTemplate.exchange(urlBase + "/rol", HttpMethod.GET,
                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Rol>>>() {
                });

        Result resultRol = responseEntityRol.getBody();

        HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario);

        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(
                urlBase + "usuario/getAllDinamico",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Result<Usuario>>() {
        }
        );

        Result result = responseEntity.getBody();

        model.addAttribute("usuarioBusqueda", new Usuario());

        model.addAttribute("Roles", resultRol.object);

        model.addAttribute("Usuarios", result.Objects);
        return "Usuario";
    }

    @PostMapping("/updateImagen")
    public String updateImagen(@RequestParam("idUsuario") int idUsuario,
            @RequestParam("imagenFile") MultipartFile imagenFile,
            Model model) {

        RestTemplate restTemplate = new RestTemplate();

        try {
            String imagenBase64 = Base64.getEncoder().encodeToString(imagenFile.getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.add("X-HTTP-Method-Override", "PATCH");
            HttpEntity<String> requestEntity = new HttpEntity<>(imagenBase64, headers);

            ResponseEntity<Result> responseEntity = restTemplate.exchange(
                    urlBase + "usuario/" + idUsuario + "/imagen",
                    HttpMethod.POST,
                    requestEntity,
                    Result.class
            );

            if (responseEntity.getStatusCode().value() == 200) {
                Result result = responseEntity.getBody();

                if (result != null && result.Correct) {
                    // Aquí podrías agregar un mensaje de éxito si lo necesitas
                    System.out.println("Imagen actualizada: " + result.Correct);
                }
            }

        } catch (Exception e) {
            System.err.println("Error en Patch: " + e.getMessage());
        }

        return "redirect:/usuario/detail/" + idUsuario;
    }

    @PostMapping("addDireccion/{IdUsuario}")
    public String addDireccion(@RequestBody Direccion direccion, @PathVariable("IdUsuario") int IdUsuario,
            Model model) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Direccion> requestEntity = new HttpEntity<>(direccion, headers);

        if (direccion.getIdDireccion() == 0) {
            try {
                String urlUpdate = urlBase + "direccion/add/" + IdUsuario;

                ResponseEntity<Result> response = restTemplate.exchange(
                        urlUpdate,
                        HttpMethod.POST,
                        requestEntity,
                        Result.class
                );

                if (response.getBody() != null && response.getBody().Correct) {
                    System.out.println("Direccion añadida");
                }

            } catch (Exception e) {
                System.err.println("Error al llamar a la API de add direccion: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                String urlUpdate = urlBase + "direccion/" + IdUsuario + "/" + direccion.getIdDireccion();

                ResponseEntity<Result> response = restTemplate.exchange(
                        urlUpdate,
                        HttpMethod.PUT,
                        requestEntity,
                        Result.class
                );

                if (response.getBody() != null && response.getBody().Correct) {
                    System.out.println("Direccion actualizada");
                }

            } catch (Exception e) {
                System.err.println("Error al llamar a la API de edit direccion: " + e.getMessage());
                e.printStackTrace();
            }

        }

        return "redirect:/usuario/detail/" + IdUsuario;
    }

    @GetMapping("CargaMasiva")
    public String CargaMasiva() {
        return "CargaMasiva";
    }

    @PostMapping("CargaMasiva")
    public String CargaMasiva(@RequestParam("archivo") MultipartFile archivo, Model model) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String urlCarga = urlBase + "/usuario/cargamasiva";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("archivo", archivo.getResource());
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, new HttpHeaders());

            Result resultado = restTemplate.postForObject(urlCarga, request, Result.class);

            if (resultado != null) {
                model.addAttribute("result", resultado);

                if (resultado.Correct) {
                    model.addAttribute("token", resultado.object);
                    model.addAttribute("listaErrores", new ArrayList<>());
                } else {
                    model.addAttribute("listaErrores", resultado.Objects);
                    model.addAttribute("token", null);
                }
            }
        } catch (Exception e) {
            model.addAttribute("Error: " + e.getMessage());
        }
        return "CargaMasiva";
    }

    @PostMapping("/CargaMasiva/procesar")
    public String procesarConfirmado(@RequestParam("token") String token, Model model) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String urlAPI = urlBase +"/usuario/cargamasiva/procesar";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.postForEntity(urlAPI, entity, String.class);

            model.addAttribute("respuestaServidor", response.getBody());

        } catch (Exception e) {
            model.addAttribute("respuestaServidor", "Error al conectar con el servidor: " + e.getMessage());
        }
        return "CargaMasiva";
    }

//    @GetMapping("/{IdDireccion}")
//    public String DetailDireccion(@PathVariable("IdDireccion") int IdDireccion, Model model) {
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        ResponseEntity<Result<Direccion>> responseEntity
//                = restTemplate.exchange(urlBase + "usuario/" + IdDireccion, HttpMethod.GET,
//                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<Direccion>>() {
//                });
//
//        if (responseEntity.getStatusCode().value() == 200) {
//            Result result = responseEntity.getBody();
//            model.addAttribute("Direccion", result.object);
//        }
//
//        return "UsuarioDetail";
//    }
//    @GetMapping("/GetByIdDireccion/{IdDireccion}")
//    @ResponseBody
//    public Result GetByIdDireccion(@PathVariable int IdDireccion) {
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        ResponseEntity<Result<Usuario>> responseEntity
//                = restTemplate.exchange(urlBase + "usuario/" + IdDireccion, HttpMethod.GET,
//                        HttpEntity.EMPTY, new ParameterizedTypeReference<Result<Usuario>>() {
//                });
//
//        Result result = direccionJPADAOImplementation.getById(IdDireccion);
//
//        return result;
//    }
//    @GetMapping("delete/{IdUsuario}")
//    public String Delete(@PathVariable("IdUsuario") int IdUsuario, RedirectAttributes redirectAttributes) {
//        //Result resultDelete = usuarioDAOImplementation.DeleteById(IdUsuario);
//        Result resultDelete = usuarioJPADAOImplementation.delete(IdUsuario);
//
////        Result resultDelete = new Result();
////        resultDelete.Correct = true;
//        if (resultDelete.Correct) {
//            resultDelete.object = "El usuario " + IdUsuario + " se elimino de forma correcta";
//        } else {
//            resultDelete.object = "No fue posible eliminar";
//        }
//
//        redirectAttributes.addFlashAttribute("resultDelete", resultDelete);
//        return "redirect:/usuario";
//    }
//    
//    @GetMapping("formEdit")
//    public String FormEditable(Model model) {
//
//        Result result = RolDAOImplementation.getAll();
//        model.addAttribute("Roles", result.Objects);
//
//        Result Presult = paisDAOImplementation.getAll();
//        model.addAttribute("Paises", Presult.Objects);
//
//        Usuario usuario = new Usuario();
//        usuario.Direcciones = new ArrayList<>();
//        usuario.Direcciones.add(new Direccion());
//
//        model.addAttribute("Usuario", usuario);
//        return "formEditable";
//    }
//
//
//
//
//
//    @GetMapping("deleteDireccion/{idUsuario}/{idDireccion}")
//    public String DeleteDireccion(
//            @PathVariable("idUsuario") int idUsuario,
//            @PathVariable("idDireccion") int idDireccion,
//            RedirectAttributes redirectAttributes) {
//
//        Result resultDelete = direccionJPADAOImplementation.delete(idDireccion);
//
//        if (resultDelete.Correct) {
//            resultDelete.object = "La direccion: " + idDireccion + " se elimino de forma correcta";
//        } else {
//            resultDelete.object = "No fue posible eliminar";
//        }
//
//        redirectAttributes.addFlashAttribute("resultDelete", resultDelete);
//        return "redirect:/usuario/detail/" + idUsuario;
//    }
//
//    @GetMapping("getEstadosByPais/{idPais}")
//    @ResponseBody
//    public Result EstadosByPais(@PathVariable("idPais") int idPais) {
//        Result resultEstados = EstadoDAOImplementation.getById(idPais);
//        return resultEstados;
//
//    }
//
//    @GetMapping("getMunicipioByEstado/{idEstado}")
//    @ResponseBody
//    public Result MunicipioByPais(@PathVariable("idEstado") int idEstado) {
//        Result resultMunicipios = MunicipioDAOImplementation.getMunByID(idEstado);
//        return resultMunicipios;
//    }
//
//    @GetMapping("getColoniaByMunicipio/{idMunicipio}")
//    @ResponseBody
//    public Result ColoniaByMunicipio(@PathVariable("idMunicipio") int idMunicipio) {
//        Result resultMunicipios = ColoniaDAOImplementation.getById(idMunicipio);
//        return resultMunicipios;
//    }
//
//    @GetMapping("/formEditable")
//    public String 
//           Form(
//        @RequestParam(required = false) Integer idUsuario,
//        @RequestParam(required = false) Integer IdDireccion,
//        Model model
//    ) {
//        if (IdDireccion == null) { // editar usuario
//            Result result = usuarioDAOImplementation.GetByIDU(idUsuario);
//
//            Result resultRoles = RolDAOImplementation.getAll();
//            model.addAttribute("Roles", resultRoles.Objects);
//
//            Usuario usuario = (Usuario) result.object;
//            usuario.Direcciones = new ArrayList<>();
//            usuario.Direcciones.add(new Direccion());
//            usuario.Direcciones.get(0).setIdDireccion(-1);
//
//            model.addAttribute("Usuario", result.object);
//
//            return "FormUsu";
//        } else if (IdDireccion == 0) { //Agregar direccion
//            //Formulario de direccion sin datos
//            Result result = usuarioDAOImplementation.GetByIDU(idUsuario);
//
//            model.addAttribute("Paises", paisDAOImplementation.getAll().Objects);
//
//            Usuario usuario = (Usuario) result.object;
//            if (usuario.Direcciones == null) {
//                usuario.Direcciones = new ArrayList<>();
//                Direccion direccion = new Direccion();
//                direccion.setIdDireccion(0);
//                usuario.Direcciones.add(direccion);
//            }
//
//            model.addAttribute("Usuario", result.object);
//
//           
//        } else {// Editar Direccion
//            //Retornar formulario direccion con datos
//
//            Result result = DireccionDAOImplementation.GetByIDUD(IdDireccion, idUsuario);
//            model.addAttribute("Paises", paisDAOImplementation.getAll().Objects);
//            model.addAttribute("Usuario", result.object);
//
//           
//        }
//        return "formEditable";
//    }
//
//    @PostMapping("/formEditable")
//    public String Form(
//            @ModelAttribute Usuario usuario, @RequestParam("imagenFile") MultipartFile imagen, Model model
//    ) throws IOException {
//        
//        if(imagen != null){
//            String extencion = imagen.getOriginalFilename().split("\\.")[1];
//            //imagen.png
//            //[imagen,png]
//            if(extencion.equals("png") || extencion.equals("jpg") || extencion.equals("jpeg")){
//               byte[] bytes = imagen.getBytes();
//              String base64Image = Base64.getEncoder().encodeToString(bytes);
//                usuario.setImagen(base64Image);
//                
//            }
//            
//        }
//
//        if (usuario.getIdUsuario() == 0) {
//          //  ModelMapper modelMapper = new ModelMapper();
//
//         //   JHeras.ProgramacionNCapasNoviembre2025.JPA.Usuario usuarioJPA = modelMapper.map(usuario, JHeras.ProgramacionNCapasNoviembre2025.JPA.Usuario.class);
//
//            Result resultadd = usuarioDAOImplementation.Add(usuario);
//
//            
//        } else if (usuario.Direcciones.get(0).getIdDireccion() == -1) {
//            //Actualizar usuario //probar sp
//            //usuarioDAOImplementation.UpdateUsuarioid(usuario);
//            int idusuario = usuario.getIdUsuario();
//            //         Result result = usuarioJPADAOImplementation.edit(usuario);
//
//            return "redirect:/usuario/Detail/" + idusuario;
//
//        } else if (usuario.Direcciones.get(0).getIdDireccion() == 0) {
//            //Añadir Direccion //agregar sp
//
//        } else {
//            //Actualizar Direccion //probar sp
//            //         Result result = usuarioJPADAOImplementation.edit(usuario);
//        }
//     
//        return "formEditable";
//
//    }
//
//
//    @PostMapping("CargaMasiva")
//    public String CargaMasiva(@ModelAttribute MultipartFile archivo, Model model, HttpSession session) throws IOException {
//        String extencion = archivo.getOriginalFilename().split("\\.")[1];
//
//        String path = System.getProperty("user.dir");
//        String pathArchivo = "src\\main\\resources\\archivos";
//        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//
//        String rutaabsoluta = path + "/" + pathArchivo + "/" + fecha + archivo.getOriginalFilename();
//
//        archivo.transferTo(new File(rutaabsoluta));
////        Path rutaPath = Paths.get(rutaabsoluta);
////
////        Files.createDirectories(rutaPath.getParent());
////
////        Files.copy(archivo.getInputStream(), rutaPath, StandardCopyOption.REPLACE_EXISTING);
//
////        try {
////            String content = Files.readString(Paths.get(rutaabsoluta));
////            System.out.println(content); // Process the entire content
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        List<Usuario> usuarios = new ArrayList<>();
//
//        if (extencion.equals("txt")) {
//            //usuarios = LecturaArchivo(archivo);
//            //usuarios=Files.readString(Paths.get(rutaabsoluta));
//            //usuarios=LecturaArchivo(content);
//            usuarios = LecturaArchivo(new File(rutaabsoluta));
//            session.setAttribute("archivoCargaMasiva", rutaabsoluta);
//
//        } else {
//
//            //usuarios = LecturaArchivoExcel(archivo);
//            usuarios = LecturaArchivoExcel(new File(rutaabsoluta));
//            session.setAttribute("archivoCargaMasiva", rutaabsoluta);
//
//        }
//
//        List<ErrorCarga> errores = ValidarDatos(usuarios);
//
//        if (errores != null || errores.isEmpty()) {
//            //Retornar vista sin errores
//            //model.addAttribute("Iserror", false);
//            model.addAttribute("listaErrores", errores);
//
//        } else {
//            //retornar la lista de errores a la vista
//            //model.addAttribute("Iserror",true);
//            model.addAttribute("listaErrores", errores);
//
//        }
//
//        return "CargaMasiva";
//
//    }
//
////    public List<Usuario> LecturaArchivoExcel(MultipartFile archivo) {
//    public List<Usuario> LecturaArchivoExcel(File archivo) {
//        List<Usuario> usuarios = new ArrayList<>();
//
//        try (XSSFWorkbook workbook = new XSSFWorkbook(archivo)) {
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            for (Row row : sheet) {
//                Usuario usuario = new Usuario();
//                usuario.setNombre(row.getCell(0).toString());
//                usuario.setApellidoPaterno(row.getCell(1).toString());
//                usuario.setApellidoMaterno(row.getCell(2).toString());
//                usuario.setTelefono(row.getCell(3).toString());
//                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
//
////                String date=row.getCell(4).getDateCellValue().toString();                
////                Date fecha=formato.parse(date);   
////                
////                usuario.setFechanacimiento(fecha);
////                usuario.setFechanacimiento(row.getCell(4).getLocalDateTimeCellValue().toString().split("T")[0]);
//                String fecha = row.getCell(4).toString();
//                Date fechaN = formato.parse(fecha);
//
//                usuario.setFechanacimiento(fechaN);
//
//                usuario.setUsername(row.getCell(5).toString());
//                usuario.setEmail(row.getCell(6).toString());
//                usuario.setSexo(row.getCell(7).toString());
//                usuario.setCelular(row.getCell(8).toString());
//
//                usuario.setCurp(row.getCell(9).toString());
//                usuario.setPassword(row.getCell(10).toString());
//
//                usuario.Rol = new Rol();
////                double idR=row.getCell(11).getNumericCellValue();
////                int valorIdR=(int) idR;
////                usuario.Rol.setIdRol(valorIdR);
//                //int idR=(int) row.getCell(11).toString();
//                int idr = Integer.parseInt(row.getCell(11).toString());
//
//                usuario.Rol.setIdRol(idr);
//
//                usuarios.add(usuario);
//            }
//        } catch (Exception ex) {
//            System.out.println("error: " + ex);
//            return usuarios;
//        }
//
//        return usuarios;
//
//    }
//
////    public List<Usuario> LecturaArchivo(MultipartFile archivo) {
////    public List<Usuario> LecturaArchivo(String content){
//    public List<Usuario> LecturaArchivo(File archivo) {
//
//        List<Usuario> usuarios = new ArrayList<>();
//
////        try (InputStream inputStream = archivo.getInputStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
////        try (BufferedReader bufferedReader = new BufferedReader(new StringReader(content))) {
//        try (InputStream inputStream = new FileInputStream(archivo); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
//            bufferedReader.readLine();
//            String line = "";
//            while ((line = bufferedReader.readLine()) != null) {
//                String[] datos = line.split("\\|");
//
//                Usuario usuario = new Usuario();
//                usuario.setNombre(datos[0]);
//                usuario.setApellidoPaterno(datos[1]);
//                usuario.setApellidoMaterno(datos[2]);
//                usuario.setTelefono(datos[3]);
//
//                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
//                java.util.Date fecha = formato.parse(datos[4]);
//
//                usuario.setFechanacimiento(fecha);
//
//                usuario.setUsername(datos[5]);
//                usuario.setEmail(datos[6]);
//                usuario.setSexo(datos[7]);
//                usuario.setCelular(datos[8]);
//                usuario.setCurp(datos[9]);
//                usuario.setPassword(datos[10]);
//
//                usuario.Rol = new Rol();
//                usuario.Rol.setIdRol(Integer.parseInt(datos[11]));
//
//                usuarios.add(usuario);
//            }
//
//        } catch (Exception ex) {
//            return usuarios;
//        }
//
//        return usuarios;
//    }
//
//    public List<ErrorCarga> ValidarDatos(List<Usuario> usuarios) {
//
//        List<ErrorCarga> erroresCarga = new ArrayList<>();
//        int LineaError = 0;
//
//        for (Usuario usuario : usuarios) {
//            List<ObjectError> errors = new ArrayList<>();
//            LineaError++;
//
//            BindingResult bindingResult = validatorService.validateObjects(usuario);
//            if (bindingResult.hasErrors()) {
//                errors.addAll(bindingResult.getAllErrors());
//            }
//
//            if (usuario.Rol != null) {
//                BindingResult bindingRol = validatorService.validateObjects(usuario.Rol);
//                if (bindingRol.hasErrors()) {
//                    errors.addAll(bindingRol.getAllErrors());
//                }
//            }
//
//            for (ObjectError error : errors) {
//                FieldError fieldError = (FieldError) error;
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.Linea = LineaError;
//                errorCarga.Campo = fieldError.getField();
//                errorCarga.Descripcion = fieldError.getDefaultMessage();
//                erroresCarga.add(errorCarga);
//            }
//        }
//        return erroresCarga;
//    }
//
//    @GetMapping("/CargaMasiva/procesar")
//    public String ProcesarArchivo(HttpSession sesion, RedirectAttributes redirectAttributes) {
//        String path = sesion.getAttribute("archivoCargaMasiva").toString();
//
//        File archivo = new File(path);
//
//        String extension = archivo.getName().substring(archivo.getName().lastIndexOf('.') + 1);;
//
//        List<Usuario> usuarios = new ArrayList<>();
//        if (extension.equals("txt")) {
//            usuarios = LecturaArchivo(archivo);
//
//        } else {
//            usuarios = LecturaArchivoExcel(archivo);
//        }
//
//        Result result = usuarioDAOImplementation.AddCargaMasiva(usuarios);
//        if (result.Correct) {
//            result.object = "Se realizo el registro";
//        } else {
//            result.object = "No se realizo la operación";
//        }
//
//        redirectAttributes.addFlashAttribute("result", result);
//
//        return "redirect:/usuario/CargaMasiva";
//        //sesion.removeAttribute("archivoCargaMasiva");
//    }
//
}
