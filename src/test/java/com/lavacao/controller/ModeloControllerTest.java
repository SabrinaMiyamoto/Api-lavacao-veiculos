package com.lavacao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lavacao.model.Marca;
import com.lavacao.model.Modelo;
import com.lavacao.model.enums.ECategoria;
import com.lavacao.repository.MarcaRepository;
import com.lavacao.repository.ModeloRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ModeloController.class)
class ModeloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ModeloRepository modeloRepository;

    @MockBean
    private MarcaRepository marcaRepository;

    private Marca marca() {
        Marca m = new Marca();
        m.setId(1);
        m.setNome("Volkswagen");
        return m;
    }

    private Modelo modelo() {
        Modelo mo = new Modelo();
        mo.setId(1);
        mo.setDescricao("Gol");
        mo.setCategoria(ECategoria.PADRAO);
        mo.setMarca(marca());
        return mo;
    }

    @Test
    void deveCriarModelo() throws Exception {
        when(marcaRepository.findById(1)).thenReturn(Optional.of(marca()));
        when(modeloRepository.save(any(Modelo.class))).thenReturn(modelo());

        mockMvc.perform(post("/modelos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelo())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Gol"))
                .andExpect(jsonPath("$.marca.id").value(1));
    }

    @Test
    void deveRetornar400AoCriarModeloSemMarca() throws Exception {
        Modelo mo = modelo();
        mo.setMarca(null);

        mockMvc.perform(post("/modelos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mo)))
                .andExpect(status().isBadRequest());

        verify(modeloRepository, never()).save(any(Modelo.class));
    }

    @Test
    void deveRetornar404AoCriarModeloComMarcaInexistente() throws Exception {
        when(marcaRepository.findById(99)).thenReturn(Optional.empty());

        Modelo mo = modelo();
        mo.getMarca().setId(99);

        mockMvc.perform(post("/modelos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mo)))
                .andExpect(status().isNotFound());

        verify(modeloRepository, never()).save(any(Modelo.class));
    }

    @Test
    void deveBuscarModeloPorId() throws Exception {
        when(modeloRepository.findById(1)).thenReturn(Optional.of(modelo()));

        mockMvc.perform(get("/modelos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Gol"));
    }

    @Test
    void deveRetornar404AoBuscarModeloInexistente() throws Exception {
        when(modeloRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/modelos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarModelo() throws Exception {
        Modelo mo = modelo();
        mo.setDescricao("Gol G6");
        when(modeloRepository.existsById(1)).thenReturn(true);
        when(marcaRepository.findById(1)).thenReturn(Optional.of(marca()));
        when(modeloRepository.save(any(Modelo.class))).thenReturn(mo);

        mockMvc.perform(put("/modelos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Gol G6"));
    }

    @Test
    void deveRetornar404AoAtualizarModeloInexistente() throws Exception {
        when(modeloRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(put("/modelos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelo())))
                .andExpect(status().isNotFound());
    }
}
