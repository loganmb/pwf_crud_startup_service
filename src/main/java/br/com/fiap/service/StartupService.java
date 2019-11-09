package br.com.fiap.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import br.com.fiap.model.StartupJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.entity.Startup;
import br.com.fiap.model.EnderecoJson;
import br.com.fiap.repository.StartupRepository;

@RestController
@RequestMapping(path = "/startup")
public class StartupService {

    @Autowired
    private StartupRepository startupRepository;

    @Transactional
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> add(@Valid @RequestBody String payload) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            StartupJson startupJson = mapper.convertValue(payload, StartupJson.class);
            Startup startup = new Startup();

            startup.setNomeFantasia(startupJson.getNomeFantasia());
            startup.setRazaoSocial(startupJson.getRazaoSocial());
            startup.setCnjp(startupJson.getCnjp());
            startup.setEmail(startupJson.getEmail());
            startup.setDataCadastro(startupJson.getDataCadastro());
            startup.setRua(startupJson.getEndereco().getRua());
            startup.setBairro(startupJson.getEndereco().getBairro());
            startup.setNumero(startupJson.getEndereco().getNumero());
            startup.setCidade(startupJson.getEndereco().getCidade());
            startup.setEstado(startupJson.getEndereco().getEstado());
            startup.setCep(startupJson.getEndereco().getCep());
            startup.setPais(startupJson.getEndereco().getPais());

            startupRepository.save(startup);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"Mensagem\":\"Cliente adicionado com sucesso\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"Mensagem\":\"Ocorreu um erro\", \"Exceção\":" + e.getMessage() + "}";

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional
    @RequestMapping(path = "/update/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<String> updateClienteById(@RequestBody String payload,
                                                    @PathVariable("id") String id) {

        try {

            List<Startup> startupList = startupRepository.findById(id);

            ObjectMapper mapper = new ObjectMapper();
            StartupJson startupJson = mapper.convertValue(payload, StartupJson.class);

            startupList.forEach(startup -> {
                startup.setStartupId(Integer.parseInt(startupJson.getStartupId()));
                startup.setNomeFantasia(startupJson.getNomeFantasia() == null || startupJson.getNomeFantasia().isEmpty()
                        ? startup.getNomeFantasia()
                        : startupJson.getNomeFantasia());
                startup.setEmail(startupJson.getEmail() == null || startupJson.getEmail().isEmpty()
                        ? startup.getEmail()
                        : startupJson.getEmail());
                startup.setDataCadastro(startupJson.getDataCadastro() == null || startupJson.getDataCadastro().isEmpty()
                        ? startup.getDataCadastro()
                        : startupJson.getDataCadastro());
                if (startupJson.getEndereco() != null) {
                    startup.setRua(startupJson.getEndereco().getRua() == null || startupJson.getEndereco().getRua().isEmpty()
                            ? startup.getRua()
                            : startupJson.getEndereco().getRua());
                    startup.setBairro(startupJson.getEndereco().getBairro() == null || startupJson.getEndereco().getBairro().isEmpty()
                            ? startup.getBairro()
                            : startupJson.getEndereco().getBairro());
                    startup.setNumero(startupJson.getEndereco().getNumero() == null || startupJson.getEndereco().getNumero().isEmpty()
                            ? startup.getNumero()
                            : startupJson.getEndereco().getNumero());
                    startup.setCidade(startupJson.getEndereco().getCidade() == null || startupJson.getEndereco().getCidade().isEmpty()
                            ? startup.getCidade()
                            : startupJson.getEndereco().getCidade());
                    startup.setEstado(startupJson.getEndereco().getEstado() == null || startupJson.getEndereco().getEstado().isEmpty()
                            ? startup.getEstado()
                            : startupJson.getEndereco().getEstado());
                    startup.setCep(startupJson.getEndereco().getCep() == null || startupJson.getEndereco().getCep().isEmpty()
                            ? startup.getCep()
                            : startupJson.getEndereco().getCep());
                    startup.setPais(startupJson.getEndereco().getPais() == null || startupJson.getEndereco().getPais().isEmpty()
                            ? startup.getPais()
                            : startupJson.getEndereco().getPais());
                }

                startupRepository.save(startup);
            });

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"Mensagem\":\"Cliente atualizado com sucesso\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"Mensagem\":\"Ocorreu um erro\", \"Exceção\":" + e.getMessage() + "}";

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deleteByCpf(@PathVariable String id) {

        try {

            List<Startup> startupList = startupRepository.findByDocument(id);

            startupList.forEach(startup -> {
                startupRepository.deleteById(startup.getStartupId());
            });

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"Mensagem\":\"Cliente excluido com sucesso\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"Mensagem\":\"Ocorreu um erro\", \"Exceção\":" + e.getMessage() + "}";

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional(readOnly = true)
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<StartupJson> getAllStartups() {

        List<StartupJson> startupsJson = new ArrayList<>();

        startupRepository.findAll().forEach(startup -> {
            StartupJson startupJson = new StartupJson();
            EnderecoJson enderecoJson = new EnderecoJson();

            startupJson.setCnjp(startup.getCnjp());
            startupJson.setDataCadastro(startup.getDataCadastro());
            startupJson.setEmail(startup.getEmail());
            startupJson.setNomeFantasia(startup.getNomeFantasia());
            startupJson.setStartupId(startup.getStartupId().toString());

            enderecoJson.setBairro(startup.getBairro());
            enderecoJson.setCep(startup.getCep());
            enderecoJson.setCidade(startup.getCidade());
            enderecoJson.setEstado(startup.getEstado());
            enderecoJson.setNumero(startup.getNumero());
            enderecoJson.setPais(startup.getPais());
            enderecoJson.setRua(startup.getRua());
            startupJson.setEndereco(enderecoJson);

            startupsJson.add(startupJson);
        });

        return startupsJson;

    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/nome/{nome_fantasia}", method = RequestMethod.GET)
    @ResponseBody
    public List<StartupJson> findByName(@PathVariable String nome_fantasia) {

        List<StartupJson> startupsJson = new ArrayList<>();

        startupRepository.findByName(nome_fantasia).forEach(startup -> {
            StartupJson startupJson = new StartupJson();
            EnderecoJson enderecoJson = new EnderecoJson();

            startupJson.setCnjp(startup.getCnjp());
            startupJson.setDataCadastro(startup.getDataCadastro());
            startupJson.setEmail(startup.getEmail());
            startupJson.setNomeFantasia(startup.getNomeFantasia());
            startupJson.setStartupId(startup.getStartupId().toString());

            enderecoJson.setBairro(startup.getBairro());
            enderecoJson.setCep(startup.getCep());
            enderecoJson.setCidade(startup.getCidade());
            enderecoJson.setEstado(startup.getEstado());
            enderecoJson.setNumero(startup.getNumero());
            enderecoJson.setPais(startup.getPais());
            enderecoJson.setRua(startup.getRua());
            startupJson.setEndereco(enderecoJson);

            startupsJson.add(startupJson);
        });

        return startupsJson;
    }


    @Transactional(readOnly = true)
    @RequestMapping(value = "/cnpj/{cnpj}", method = RequestMethod.GET)
    @ResponseBody
    public List<StartupJson> findByDocument(@PathVariable String cnpj) {

        List<StartupJson> startupJsons = new ArrayList<>();


        startupRepository.findByDocument(cnpj).forEach(startup -> {
            StartupJson startupJson = new StartupJson();
            EnderecoJson enderecoJson = new EnderecoJson();

            startupJson.setCnjp(startup.getCnjp());
            startupJson.setDataCadastro(startup.getDataCadastro());
            startupJson.setEmail(startup.getEmail());
            startupJson.setNomeFantasia(startup.getNomeFantasia());
            startupJson.setStartupId(startup.getStartupId().toString());

            enderecoJson.setBairro(startup.getBairro());
            enderecoJson.setCep(startup.getCep());
            enderecoJson.setCidade(startup.getCidade());
            enderecoJson.setEstado(startup.getEstado());
            enderecoJson.setNumero(startup.getNumero());
            enderecoJson.setPais(startup.getPais());
            enderecoJson.setRua(startup.getRua());
            startupJson.setEndereco(enderecoJson);

            startupJsons.add(startupJson);
        });

        return startupJsons;
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/{id}/usuarios")
    @ResponseBody
    public List<String> findUsersByStartup(@PathVariable String id) {

        List<String> uuids = startupRepository.findUsersByStartup(id);

        return uuids;
    }

    @Transactional(readOnly = true)
    @PostMapping(value = "/{id}/usuarios/add")
    @ResponseBody
    public ResponseEntity addUserOnStartup(@PathVariable String id, @RequestBody String body) {

		try {

			ObjectMapper mapper = new ObjectMapper();
			String uuid = mapper.convertValue(body, String.class);
			Startup startup = new Startup();

			Set<String> uuids = new HashSet<>();

			uuids.add(uuid);

			startup.setUuids(uuids);

			startup.setStartupId(Integer.parseInt(id));
			startup.setUsuarios(startupJson.getUsuarios());

			startupRepository.save(startup);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String responseBody = "{\"Mensagem\":\"Cliente adicionado com sucesso\"}";

			return new ResponseEntity<>(responseBody, headers, HttpStatus.CREATED);

		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			String responseBody = "{\"Mensagem\":\"Ocorreu um erro\", \"Exceção\":" + e.getMessage() + "}";

			return new ResponseEntity<>(responseBody, headers, HttpStatus.BAD_REQUEST);
		}

    }

}
