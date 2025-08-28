package com.project.guia.solidario.controller;

import com.project.guia.solidario.dto.OngDTO;
import com.project.guia.solidario.dto.OngFilterDTO;
import com.project.guia.solidario.services.OngService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ongs")
@RequiredArgsConstructor
public class OngController {

    private final OngService ongService;

    @PostMapping
    public ResponseEntity<OngDTO> create(@RequestBody OngDTO dto) {
        return ResponseEntity.ok(ongService.create(dto));
    }

    @GetMapping("/search/by-filters")
    public ResponseEntity<Page<OngDTO>> findByOngsByFilter(@ModelAttribute OngFilterDTO filterDTO, Pageable pageable) {
        return ResponseEntity.ok(ongService.findByFilters(filterDTO, pageable));
    }
}
