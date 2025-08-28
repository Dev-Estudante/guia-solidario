package com.project.guia.solidario.repositories;

import com.project.guia.solidario.dto.OngFilterDTO;
import com.project.guia.solidario.entities.OngEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OngCustomRepository {
    Page<OngEntity> findByFilters(OngFilterDTO filter, Pageable pageable);
}