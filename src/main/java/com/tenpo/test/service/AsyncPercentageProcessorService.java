package com.tenpo.test.service;


import com.tenpo.test.model.enums.Status;

public interface AsyncPercentageProcessorService {

	void saveHistory(Object response, Status status);
}
