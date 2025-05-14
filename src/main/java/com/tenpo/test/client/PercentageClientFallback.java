package com.tenpo.test.client;

import com.tenpo.test.dto.percentage.PercentageDto;


public class PercentageClientFallback implements PercentageClient {

	@Override
	public PercentageDto getCurrent() {
		return null;
	}
}
