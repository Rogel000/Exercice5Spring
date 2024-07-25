package org.example.exercice5spring.service;

import org.example.exercice5spring.dao.FurnitureRepository;
import org.example.exercice5spring.entity.Furniture;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FurnitureService {
    private final FurnitureRepository repository;

    public FurnitureService(FurnitureRepository repository) {
        this.repository = repository;
    }

    public Furniture saveFurniture(Furniture furniture) {
        return repository.save(furniture);
    }
    public List<Furniture> getAllFurnitures() {
        return repository.findAll();
    }
    public Furniture getFurnitureById(Long id) {
        return repository.findById(id).orElse(null);
    }
    public void deleteFurnitureById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }
        repository.deleteById(id);
    }

}
