package com.lavacao.controller;

import com.lavacao.model.Marca;
import com.lavacao.model.Modelo;
import com.lavacao.repository.MarcaRepository;
import com.lavacao.repository.ModeloRepository;
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
@RequestMapping("/modelos")
public class ModeloController {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;

    public ModeloController(ModeloRepository modeloRepository, MarcaRepository marcaRepository) {
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
    }

    @PostMapping
    public ResponseEntity<Modelo> criar(@RequestBody Modelo modelo) {
        resolverMarca(modelo);
        Modelo salvo = modeloRepository.save(modelo);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modelo> buscarPorId(@PathVariable Integer id) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modelo nao encontrado"));
        return ResponseEntity.ok(modelo);
    }

    @GetMapping
    public ResponseEntity<List<Modelo>> listar() {
        return ResponseEntity.ok(modeloRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Modelo> atualizar(@PathVariable Integer id, @RequestBody Modelo modelo) {
        if (!modeloRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Modelo nao encontrado");
        }
        resolverMarca(modelo);
        modelo.setId(id);
        return ResponseEntity.ok(modeloRepository.save(modelo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        if (!modeloRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Modelo nao encontrado");
        }
        modeloRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void resolverMarca(Modelo modelo) {
        if (modelo.getMarca() == null || modelo.getMarca().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Marca e obrigatoria");
        }
        Marca marca = marcaRepository.findById(modelo.getMarca().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca nao encontrada"));
        modelo.setMarca(marca);
    }
}
