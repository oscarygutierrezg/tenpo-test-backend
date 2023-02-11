package com.tenpo.test.service;


import io.github.bucket4j.Bucket;

public interface RateLimitService {

	void validate(Bucket bucket);
}
