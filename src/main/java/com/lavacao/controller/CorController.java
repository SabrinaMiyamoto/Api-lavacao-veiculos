package com.lavacao.controller;

import com.lavacao.model.Cor;
import com.lavacao.repository.CorRepository;
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
@RequestMapping("/cores")
public class CorController {

    private final CorRepository corRepository;

    public CorController(CorRepository corRepository) {
        this.corRepository = corRepository;
    }

    @PostMapping
    public ResponseEntity<Cor> criar(@RequestBody Cor cor) {
        Cor salva = corRepository.save(cor);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cor> buscarPorId(@PathVariable Integer id) {
        Cor cor = corRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cor nao encontrada"));
        return ResponseEntity.ok(cor);
    }

    @GetMapping
    public ResponseEntity<List<Cor>> listar() {
        return ResponseEntity.ok(corRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cor> atualizar(@PathVariable Integer id, @RequestBody Cor cor) {
        if (!corRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cor nao encontrada");
        }
        cor.setId(id);
        return ResponseEntity.ok(corRepository.save(cor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        if (!corRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cor nao encontrada");
        }
        corRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
