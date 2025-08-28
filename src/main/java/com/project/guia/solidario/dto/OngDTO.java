package com.project.guia.solidario.dto;

public record OngDTO(
        String id,
        String nome,
        Double avaliacao,
        CategoriaDTO categoria,
        EnderecoDTO endereco
) {}
