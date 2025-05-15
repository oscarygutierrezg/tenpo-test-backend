package com.tenpo.test.service;

import com.tenpo.test.dto.percentage.PercentageDto;

public interface PercentageService {

	PercentageDto getCurrentCacheable();

	PercentageDto getCurrent();
}
