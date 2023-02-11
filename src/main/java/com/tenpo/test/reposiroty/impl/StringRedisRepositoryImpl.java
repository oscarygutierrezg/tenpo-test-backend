package com.tenpo.test.reposiroty.impl;

import com.tenpo.test.reposiroty.StringRedisRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

@AllArgsConstructor
public class StringRedisRepositoryImpl implements StringRedisRepository {

    private final StringRedisTemplate template;

    @Override
    public void add(String key, String value) {
        template.opsForValue().set(key, value);
    }

    @Override
    public String getBy(String key) {
        return template.opsForValue().get(key);
    }

}
