package com.project.guia.solidario.mapper;

import com.project.guia.solidario.dto.CategoriaDTO;
import com.project.guia.solidario.dto.EnderecoDTO;
import com.project.guia.solidario.dto.OngDTO;
import com.project.guia.solidario.entities.CategoriaEntity;
import com.project.guia.solidario.entities.OngEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface OngMapper {

    @Mapping(target = "endereco.latitude", source = "endereco.geolocalizacao.y")
    @Mapping(target = "endereco.longitude", source = "endereco.geolocalizacao.x")
    OngDTO toDto(OngEntity ong);

    default Page<OngDTO> toDto(Page<OngEntity> page) {
        return page.map(this::toDto);
    }

    // Categoria <-> CategoriaDTO
    CategoriaDTO toDto(CategoriaEntity categoria);
    CategoriaEntity toEntity(CategoriaDTO categoriaDTO);

    @Mapping(target = "endereco.geolocalizacao", expression = "java(new GeoJsonPoint(enderecoDTO.longitude(), enderecoDTO.latitude()))")
    OngEntity toEntity(OngDTO ongDTO);

    @InheritInverseConfiguration
    @Mapping(target = "geolocalizacao", expression = "java(new GeoJsonPoint(enderecoDTO.longitude(), enderecoDTO.latitude()))")
    OngEntity.Endereco toEntity(EnderecoDTO enderecoDTO);
}
