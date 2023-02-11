package com.tempo.test.service;


import io.github.bucket4j.Bucket;

public interface RateLimitService {

	void validate(Bucket bucket);
}
