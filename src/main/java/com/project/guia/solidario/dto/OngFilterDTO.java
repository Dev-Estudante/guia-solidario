package com.project.guia.solidario.dto;

public record OngFilterDTO(
    String nome,
    Double avaliacao,
    String cidade,
    String bairro,
    String categoriaNome,
    Double latitude,
    Double longitude,
    Double distancia
) {}
