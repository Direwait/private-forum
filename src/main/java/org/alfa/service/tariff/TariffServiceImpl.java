package org.alfa.service.tariff;

import lombok.RequiredArgsConstructor;
import org.alfa.database.TariffRepository;
import org.alfa.service._mapper.TariffMapper;
import org.alfa.service._dto.TariffDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffMapper tariffMapper;
    private final TariffRepository tariffRepository;


    @Override
    public List<TariffDto> getAllTariffs() {
        return tariffRepository.findAll()
                .stream()
                .map(tariffMapper::modelToDto)
                .toList();
    }

    @Override
    public TariffDto getById(UUID tariffId) {
        return tariffMapper.modelToDto(tariffRepository.getReferenceById(tariffId));
    }


}
