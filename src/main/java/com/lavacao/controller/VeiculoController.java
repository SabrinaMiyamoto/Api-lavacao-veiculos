package com.lavacao.controller;

import com.lavacao.model.Cliente;
import com.lavacao.model.Cor;
import com.lavacao.model.Modelo;
import com.lavacao.model.Veiculo;
import com.lavacao.repository.ClienteRepository;
import com.lavacao.repository.CorRepository;
import com.lavacao.repository.ModeloRepository;
import com.lavacao.repository.VeiculoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoRepository veiculoRepository;
    private final CorRepository corRepository;
    private final ModeloRepository modeloRepository;
    private final ClienteRepository clienteRepository;

    public VeiculoController(VeiculoRepository veiculoRepository,
                             CorRepository corRepository,
                             ModeloRepository modeloRepository,
                             ClienteRepository clienteRepository) {
        this.veiculoRepository = veiculoRepository;
        this.corRepository = corRepository;
        this.modeloRepository = modeloRepository;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    public ResponseEntity<Veiculo> criar(@RequestBody Veiculo veiculo) {
        resolverAssociacoes(veiculo);
        Veiculo salvo = veiculoRepository.save(veiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscarPorId(@PathVariable Integer id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado"));
        return ResponseEntity.ok(veiculo);
    }

    @GetMapping
    public ResponseEntity<List<Veiculo>> listar() {
        return ResponseEntity.ok(veiculoRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> atualizar(@PathVariable Integer id, @RequestBody Veiculo veiculo) {
        if (!veiculoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado");
        }
        resolverAssociacoes(veiculo);
        veiculo.setId(id);
        return ResponseEntity.ok(veiculoRepository.save(veiculo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        if (!veiculoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado");
        }
        veiculoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void resolverAssociacoes(Veiculo veiculo) {
        if (veiculo.getCor() == null || veiculo.getCor().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cor e obrigatoria");
        }
        if (veiculo.getModelo() == null || veiculo.getModelo().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modelo e obrigatorio");
        }
        if (veiculo.getProprietario() == null || veiculo.getProprietario().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Proprietario e obrigatorio");
        }

        Cor cor = corRepository.findById(veiculo.getCor().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cor nao encontrada"));
        veiculo.setCor(cor);

        Modelo modelo = modeloRepository.findById(veiculo.getModelo().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modelo nao encontrado"));
        veiculo.setModelo(modelo);

        Cliente proprietario = clienteRepository.findById(veiculo.getProprietario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado"));
        veiculo.setProprietario(proprietario);
    }
}
