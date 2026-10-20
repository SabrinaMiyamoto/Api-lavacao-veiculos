package com.lavacao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lavacao.model.Cliente;
import com.lavacao.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteRepository clienteRepository;

    private Cliente cliente() {
        Cliente c = new Cliente();
        c.setId(1);
        c.setNome("Joao da Silva");
        c.setCelular("48999999999");
        c.setEmail("joao@email.com");
        c.setDataCadastro(LocalDate.of(2026, 10, 20));
        return c;
    }

    @Test
    void deveCriarCliente() throws Exception {
        Cliente c = cliente();
        when(clienteRepository.save(any(Cliente.class))).thenReturn(c);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Joao da Silva"));
    }

    @Test
    void deveBuscarClientePorId() throws Exception {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente()));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Joao da Silva"));
    }

    @Test
    void deveRetornar404AoBuscarClienteInexistente() throws Exception {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarClientes() throws Exception {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente()));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Joao da Silva"));
    }

    @Test
    void deveAtualizarCliente() throws Exception {
        Cliente c = cliente();
        c.setNome("Joao Atualizado");
        when(clienteRepository.existsById(1)).thenReturn(true);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(c);

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Joao Atualizado"));
    }

    @Test
    void deveRetornar404AoAtualizarClienteInexistente() throws Exception {
        when(clienteRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(put("/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente())))
                .andExpect(status().isNotFound());

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveRemoverCliente() throws Exception {
        when(clienteRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteRepository).deleteById(1);
    }

    @Test
    void deveRetornar404AoRemoverClienteInexistente() throws Exception {
        when(clienteRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(delete("/clientes/99"))
                .andExpect(status().isNotFound());

        verify(clienteRepository, never()).deleteById(99);
    }
}
