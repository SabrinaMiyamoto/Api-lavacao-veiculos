package com.lavacao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lavacao.model.Marca;
import com.lavacao.repository.MarcaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MarcaController.class)
class MarcaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MarcaRepository marcaRepository;

    private Marca marca() {
        Marca m = new Marca();
        m.setId(1);
        m.setNome("Volkswagen");
        return m;
    }

    @Test
    void deveCriarMarca() throws Exception {
        when(marcaRepository.save(any(Marca.class))).thenReturn(marca());

        mockMvc.perform(post("/marcas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(marca())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Volkswagen"));
    }

    @Test
    void deveBuscarMarcaPorId() throws Exception {
        when(marcaRepository.findById(1)).thenReturn(Optional.of(marca()));

        mockMvc.perform(get("/marcas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Volkswagen"));
    }

    @Test
    void deveRetornar404AoBuscarMarcaInexistente() throws Exception {
        when(marcaRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/marcas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarMarcas() throws Exception {
        when(marcaRepository.findAll()).thenReturn(List.of(marca()));

        mockMvc.perform(get("/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Volkswagen"));
    }

    @Test
    void deveAtualizarMarca() throws Exception {
        Marca m = marca();
        m.setNome("Volkswagen do Brasil");
        when(marcaRepository.existsById(1)).thenReturn(true);
        when(marcaRepository.save(any(Marca.class))).thenReturn(m);

        mockMvc.perform(put("/marcas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(m)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Volkswagen do Brasil"));
    }

    @Test
    void deveRetornar404AoAtualizarMarcaInexistente() throws Exception {
        when(marcaRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(put("/marcas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(marca())))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRemoverMarca() throws Exception {
        when(marcaRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/marcas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoRemoverMarcaInexistente() throws Exception {
        when(marcaRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(delete("/marcas/99"))
                .andExpect(status().isNotFound());
    }
}
