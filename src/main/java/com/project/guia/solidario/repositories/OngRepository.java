package com.project.guia.solidario.repositories;

import com.project.guia.solidario.entities.OngEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OngRepository extends MongoRepository<OngEntity, String>, OngCustomRepository {
}