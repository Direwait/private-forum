package org.alfa.controller.tariff;

import org.alfa.service._dto.TariffDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TariffController {

    ResponseEntity<List<TariffDto>> getAllArticles();
}
