package com.tempo.test.reposiroty;

public interface StringRedisRepository {

     void add(String key, String value);

     String getBy(String key);
}
