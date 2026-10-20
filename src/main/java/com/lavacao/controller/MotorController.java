package com.lavacao.controller;

import com.lavacao.model.Motor;
import com.lavacao.repository.MotorRepository;
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
@RequestMapping("/motores")
public class MotorController {

    private final MotorRepository motorRepository;

    public MotorController(MotorRepository motorRepository) {
        this.motorRepository = motorRepository;
    }

    @PostMapping
    public ResponseEntity<Motor> criar(@RequestBody Motor motor) {
        Motor salvo = motorRepository.save(motor);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Motor> buscarPorId(@PathVariable Integer id) {
        Motor motor = motorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Motor nao encontrado"));
        return ResponseEntity.ok(motor);
    }

    @GetMapping
    public ResponseEntity<List<Motor>> listar() {
        return ResponseEntity.ok(motorRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Motor> atualizar(@PathVariable Integer id, @RequestBody Motor motor) {
        if (!motorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Motor nao encontrado");
        }
        motor.setId(id);
        return ResponseEntity.ok(motorRepository.save(motor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        if (!motorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Motor nao encontrado");
        }
        motorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
