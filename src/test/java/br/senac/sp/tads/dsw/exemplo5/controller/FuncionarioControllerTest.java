package br.senac.sp.tads.dsw.exemplo5.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.senac.sp.tads.dsw.exemplo5.model.Departamento;
import br.senac.sp.tads.dsw.exemplo5.model.Funcionario;
import br.senac.sp.tads.dsw.exemplo5.repository.DepartamentoRepository;
import br.senac.sp.tads.dsw.exemplo5.repository.FuncionarioRepository;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;



import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FuncionarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Departamento departamentoPadrao;

    @BeforeEach 
    void setUp() {

        Departamento departamento = new Departamento();
        departamento.setNome("Leandro Pessoa ");
        departamento.setOrcamento(3600.00);
        departamentoPadrao = departamentoRepository.save(departamento);
    }

            @Test 
        void deveCriarFuncionarioComSucesso() throws Exception {

        }

        @Test 
        void deveRetornarErro400AoCriarFuncionarioComDataFutura() throws Exception {

        }

        @Test 
        void deveListarTodosOsFuncionarios() throws Exception {

            
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Leandro");
        funcionario.setTrabalhoRemoto(true);
        funcionarioRepository.save(funcionario);

        mockMvc.perform(get("/api/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        }

        @Test
        void deveBuscarFuncionarioPorId() throws Exception {
           
        
        Funcionario funcionario = new Funcionario();
        funcionario.setDepartamento(departamentoPadrao);
        funcionario.setNome("Leandro");
        funcionario.setDataContratacao(LocalDate.of(2010, 12, 25));
        funcionario.setTrabalhoRemoto(true);
        funcionarioRepository.save(funcionario);
    

        mockMvc.perform(get("/api/funcionarios/" + funcionario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(funcionario.getId())) // verifica se o ID existe
                .andExpect(jsonPath("$.nome").value("Leandro"))
                .andExpect(jsonPath("$.trabalhoRemoto").value(true));     

        }

        @Test 
        void deveAtualizarFuncionario() throws Exception {

        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Leandro");
        funcionario.setDataContratacao(LocalDate.of(2010, 10, 25));
        funcionario.setTrabalhoRemoto(false);
        funcionario.setDepartamento(departamentoPadrao);
        Funcionario funcionarioAtualizado = funcionarioRepository.save(funcionario);

       funcionarioAtualizado.setNome("Lucas");

        String json = objectMapper.writeValueAsString(funcionarioAtualizado);

        mockMvc.perform(
                    put("/api/funcionarios/" + funcionarioAtualizado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                )
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.nome").value("Lucas"))
                .andExpect(jsonPath("$.dataContratacao").value("2010-10-25"));     
        }

        @Test 
        void deveApagarFuncionario() throws Exception {

        Funcionario funcionario = new Funcionario();
        funcionario.setDepartamento(departamentoPadrao);
        funcionario.setNome("Leandro");
        funcionario.setDataContratacao(LocalDate.of(2010, 12, 25));
        funcionario.setTrabalhoRemoto(true);
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        mockMvc.perform(delete("/api/funcionarios/" + funcionarioSalvo.getId()))
                .andExpect(status().isNoContent()); 
    }
            
        }


