package org.alfa.controller.tariff;


import lombok.RequiredArgsConstructor;
import org.alfa.service._dto.TariffDto;
import org.alfa.service.tariff.TariffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tariffs")
public class TariffControllerImpl implements TariffController {

    private final TariffService tariffService;

    @GetMapping()
    @Override
    public ResponseEntity<List<TariffDto>> getAllArticles() {
        return ResponseEntity.ok(tariffService.getAllTariffs());
    }
}
