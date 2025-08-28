package com.project.guia.solidario.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "ong")
@CompoundIndexes({
     @CompoundIndex(name = "idx_location", def = "{'endereco.geolocalizacao':'2dsphere'}")
})
public class OngEntity {
    @MongoId
    private String id;
    private String nome;
    private Double avaliacao;

    private Endereco endereco;

    private CategoriaEntity categoria;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Endereco {
        private String logradouro;
        private String cep;
        private String bairro;
        private String cidade;
        private Integer numero;
        private GeoJsonPoint geolocalizacao; // GeoJSON Point correto para 2dsphere
    }
}
