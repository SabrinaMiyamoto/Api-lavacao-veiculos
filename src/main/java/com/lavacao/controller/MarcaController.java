package com.lavacao.controller;

import com.lavacao.model.Marca;
import com.lavacao.repository.MarcaRepository;
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
@RequestMapping("/marcas")
public class MarcaController {

    private final MarcaRepository marcaRepository;

    public MarcaController(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @PostMapping
    public ResponseEntity<Marca> criar(@RequestBody Marca marca) {
        Marca salva = marcaRepository.save(marca);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marca> buscarPorId(@PathVariable Integer id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca nao encontrada"));
        return ResponseEntity.ok(marca);
    }

    @GetMapping
    public ResponseEntity<List<Marca>> listar() {
        return ResponseEntity.ok(marcaRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marca> atualizar(@PathVariable Integer id, @RequestBody Marca marca) {
        if (!marcaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca nao encontrada");
        }
        marca.setId(id);
        return ResponseEntity.ok(marcaRepository.save(marca));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        if (!marcaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca nao encontrada");
        }
        marcaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
