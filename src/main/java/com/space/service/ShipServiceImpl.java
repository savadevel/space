package com.space.service;

import com.space.model.FilterOptions;
import com.space.model.Ship;
import com.space.repository.ShipRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShipServiceImpl implements ShipService {

    private final ShipRepository shipRepository;

    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Long getCountOfShips(FilterOptions filterOptions) {
        return shipRepository.count();
    }

    @Override
    public List<Ship> getShips(FilterOptions fo) {
        return shipRepository.findAll(PageRequest.of(
                fo.getPageNumber(),
                fo.getPageSize(),
                Sort.by(fo.getOrder().getFieldName())))
                .getContent();
    }
}
