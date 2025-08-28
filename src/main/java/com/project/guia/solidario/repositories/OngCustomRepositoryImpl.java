package com.project.guia.solidario.repositories;

import com.project.guia.solidario.dto.OngFilterDTO;
import com.project.guia.solidario.entities.OngEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OngCustomRepositoryImpl implements OngCustomRepository {

    public static final String CATEGORIA = "categoria";
    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OngEntity> findByFilters(OngFilterDTO filter, Pageable pageable) {

        List<Criteria> criteriaList = new ArrayList<>();

        // Nome da ONG
        if (filter.nome() != null && !filter.nome().isBlank()) {
            criteriaList.add(Criteria.where("nome").regex(filter.nome(), "i"));
        }

        // Avaliação
        if (filter.avaliacao() != null) {
            criteriaList.add(Criteria.where("avaliacao").is(filter.avaliacao()));
        }

        // Cidade
        if (filter.cidade() != null && !filter.cidade().isBlank()) {
            criteriaList.add(Criteria.where("endereco.cidade").regex(filter.cidade(), "i"));
        }

        // Bairro
        if (filter.bairro() != null && !filter.bairro().isBlank()) {
            criteriaList.add(Criteria.where("endereco.bairro").regex(filter.bairro(), "i"));
        }

        // Categoria (match pelo nome da categoria)
        if (filter.categoriaNome() != null && !filter.categoriaNome().isBlank()) {
            criteriaList.add(Criteria.where("categoria.nome").regex(filter.categoriaNome(), "i"));
        }

        // Localização
        if (filter.latitude() != null && filter.longitude() != null && filter.distancia() != null) {
            Point point = new Point(filter.longitude(), filter.latitude()); // ordem: lng, lat
            Distance distance = new Distance(filter.distancia(), Metrics.KILOMETERS);
            criteriaList.add(Criteria.where("endereco.geolocalizacao")
                    .nearSphere(point)
                    .maxDistance(distance.getNormalizedValue()));
        }

        Criteria finalCriteria = criteriaList.isEmpty()
                ? new Criteria()
                : new Criteria().andOperator(criteriaList);

        List<AggregationOperation> operations = new ArrayList<>();

        // 1) Lookup para "join" com categoria
        operations.add(Aggregation.lookup(CATEGORIA, "categoria_id", "_id", CATEGORIA));

        // 2) "desmontar" array categoria -> objeto
        operations.add(Aggregation.unwind(CATEGORIA, true));

        // 3) aplicar filtros
        operations.add(Aggregation.match(finalCriteria));

        // 4) ordenar + paginação
        operations.add(Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()));
        operations.add(Aggregation.limit(pageable.getPageSize()));

        // Pipeline
        Aggregation aggregation = Aggregation.newAggregation(operations);

        // Execução
        List<OngEntity> ongs = mongoTemplate.aggregate(aggregation, "ong", OngEntity.class).getMappedResults();

        // Count total (sem skip/limit)
        Aggregation countAgg = Aggregation.newAggregation(
                Aggregation.lookup(CATEGORIA, "categoria_id", "_id", CATEGORIA),
                Aggregation.unwind(CATEGORIA, true),
                Aggregation.match(finalCriteria),
                Aggregation.count().as("total")
        );

        long total = mongoTemplate.aggregate(countAgg, "ong", Map.class)
                .getUniqueMappedResult() != null
                ? ((Integer) Objects.requireNonNull(mongoTemplate.aggregate(countAgg, "ong", Map.class)
                                    .getUniqueMappedResult()).get("total")).longValue()
                : 0L;

        return new PageImpl<>(ongs, pageable, total);
    }

}
