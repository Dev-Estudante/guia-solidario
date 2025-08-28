package com.project.guia.solidario.repositories;

import com.project.guia.solidario.entities.CategoriaEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends MongoRepository<CategoriaEntity, String> {
    Optional<CategoriaEntity> findByNomeIgnoreCase(String nome);
}