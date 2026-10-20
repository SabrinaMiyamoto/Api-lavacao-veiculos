package com.lavacao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lavacao.model.Cliente;
import com.lavacao.model.Cor;
import com.lavacao.model.Modelo;
import com.lavacao.model.Veiculo;
import com.lavacao.repository.ClienteRepository;
import com.lavacao.repository.CorRepository;
import com.lavacao.repository.ModeloRepository;
import com.lavacao.repository.VeiculoRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VeiculoController.class)
class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VeiculoRepository veiculoRepository;

    @MockBean
    private CorRepository corRepository;

    @MockBean
    private ModeloRepository modeloRepository;

    @MockBean
    private ClienteRepository clienteRepository;

    private Cor cor() {
        Cor c = new Cor();
        c.setId(1);
        c.setNome("Prata");
        return c;
    }

    private Modelo modelo() {
        Modelo m = new Modelo();
        m.setId(1);
        m.setDescricao("Gol");
        return m;
    }

    private Cliente cliente() {
        Cliente c = new Cliente();
        c.setId(1);
        c.setNome("Joao");
        return c;
    }

    private Veiculo veiculo() {
        Veiculo v = new Veiculo();
        v.setId(1);
        v.setPlaca("ABC1D23");
        v.setObservacoes("Sem observacoes");
        v.setCor(cor());
        v.setModelo(modelo());
        v.setProprietario(cliente());
        return v;
    }

    @Test
    void deveCriarVeiculo() throws Exception {
        when(corRepository.findById(1)).thenReturn(Optional.of(cor()));
        when(modeloRepository.findById(1)).thenReturn(Optional.of(modelo()));
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente()));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo());

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculo())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.placa").value("ABC1D23"))
                .andExpect(jsonPath("$.cor.id").value(1))
                .andExpect(jsonPath("$.modelo.id").value(1))
                .andExpect(jsonPath("$.proprietario.id").value(1));
    }

    @Test
    void deveRetornar400AoCriarVeiculoSemCor() throws Exception {
        Veiculo v = veiculo();
        v.setCor(null);

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(v)))
                .andExpect(status().isBadRequest());

        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    void deveRetornar400AoCriarVeiculoSemModelo() throws Exception {
        Veiculo v = veiculo();
        v.setModelo(null);

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(v)))
                .andExpect(status().isBadRequest());

        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    void deveRetornar400AoCriarVeiculoSemProprietario() throws Exception {
        Veiculo v = veiculo();
        v.setProprietario(null);

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(v)))
                .andExpect(status().isBadRequest());

        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    void deveRetornar404AoCriarVeiculoComCorInexistente() throws Exception {
        when(corRepository.findById(99)).thenReturn(Optional.empty());

        Veiculo v = veiculo();
        v.getCor().setId(99);

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(v)))
                .andExpect(status().isNotFound());

        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    void deveBuscarVeiculoPorId() throws Exception {
        when(veiculoRepository.findById(1)).thenReturn(Optional.of(veiculo()));

        mockMvc.perform(get("/veiculos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa").value("ABC1D23"));
    }

    @Test
    void deveRetornar404AoBuscarVeiculoInexistente() throws Exception {
        when(veiculoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/veiculos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarVeiculo() throws Exception {
        Veiculo v = veiculo();
        v.setPlaca("XYZ9A87");
        when(veiculoRepository.existsById(1)).thenReturn(true);
        when(corRepository.findById(1)).thenReturn(Optional.of(cor()));
        when(modeloRepository.findById(1)).thenReturn(Optional.of(modelo()));
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente()));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(v);

        mockMvc.perform(put("/veiculos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(v)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa").value("XYZ9A87"));
    }

    @Test
    void deveRetornar404AoAtualizarVeiculoInexistente() throws Exception {
        when(veiculoRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(put("/veiculos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculo())))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRemoverVeiculo() throws Exception {
        when(veiculoRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/veiculos/1"))
                .andExpect(status().isNoContent());

        verify(veiculoRepository).deleteById(1);
    }

    @Test
    void deveRetornar404AoRemoverVeiculoInexistente() throws Exception {
        when(veiculoRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(delete("/veiculos/99"))
                .andExpect(status().isNotFound());
    }
}
