package com.project.guia.solidario.services;

import com.project.guia.solidario.dto.CategoriaDTO;
import com.project.guia.solidario.dto.OngDTO;
import com.project.guia.solidario.dto.OngFilterDTO;
import com.project.guia.solidario.entities.OngEntity;
import com.project.guia.solidario.mapper.OngMapper;
import com.project.guia.solidario.repositories.CategoriaRepository;
import com.project.guia.solidario.repositories.OngRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OngService {

    private final OngRepository ongRepository;
    private final CategoriaRepository categoriaRepository;
    private final OngMapper ongMapper;

    public OngDTO create(OngDTO dto) {
        if (!categoriaRepository.existsById(dto.categoria().id())) {
            throw new IllegalArgumentException("Categoria inválida");
        }
        OngEntity ong = ongMapper.toEntity(dto);
        OngEntity saved = ongRepository.save(ong);
        return enrichCategoria(ongMapper.toDto(saved));
    }

    public Page<OngDTO> findByFilters(OngFilterDTO filter, Pageable pageable) {
        Page<OngEntity> ongs = ongRepository.findByFilters(filter, pageable);
        return ongs.map(ong -> enrichCategoria(ongMapper.toDto(ong)));
    }

    private OngDTO enrichCategoria(OngDTO dto) {
        // Se categoria vier sem nome, busca e preenche
        if (dto.categoria() != null && dto.categoria().nome() == null) {
            return categoriaRepository.findById(dto.categoria().id())
                    .map(cat -> new OngDTO(
                            dto.id(),
                            dto.nome(),
                            dto.avaliacao(),
                            new CategoriaDTO(cat.getId(), cat.getNome()),
                            dto.endereco()
                    ))
                    .orElse(dto);
        }
        return dto;
    }
}

