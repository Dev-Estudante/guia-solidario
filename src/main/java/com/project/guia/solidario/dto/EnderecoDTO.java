package com.project.guia.solidario.dto;

public record EnderecoDTO(
        String logradouro,
        String cep,
        String bairro,
        String cidade,
        Integer numero,
        Double latitude,
        Double longitude
) {}