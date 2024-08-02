package org.example.exercice5spring.service;

import org.example.exercice5spring.dao.FurnitureRepository;
import org.example.exercice5spring.entity.Furniture;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FurnitureService {
    private final FurnitureRepository repository;

    public FurnitureService(FurnitureRepository repository) {
        this.repository = repository;
    }

    public void saveFurniture(Furniture furniture) {
        repository.save(furniture);
    }

    public List<Furniture> getAllFurnitures() {
        return repository.findAll();
    }

    public Furniture getFurnitureById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Furniture not found"));
    }

    @Transactional
    public void deleteFurnitureById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }
        if (!repository.existsById(id)) {
            throw new RuntimeException("Furniture not found");
        }
        repository.deleteById(id);
    }

    public void updateFurniture(Furniture furniture) {
        saveFurniture(furniture);
    }
}
