package com.tenpo.test.service.impl;

import com.tenpo.test.client.PercentageClient;
import com.tenpo.test.dto.percentage.PercentageDto;
import com.tenpo.test.service.PercentageService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PercentageServiceImpl implements PercentageService {

    private final PercentageClient percentageClient;

    @Override
    @Cacheable(value = "percentages", key = "#root.method.name", unless = "#result == null")
    public PercentageDto getCurrentCacheable() {
        return  percentageClient.getCurrent();
    }

    @Override
    public PercentageDto getCurrent() {
        return  percentageClient.getCurrent();
    }
}
