package com.tenpo.test.constants;


public class Constants {
 private Constants() {
  throw new IllegalStateException("Utility class");
 }

 public static final String PRECONDITION_FAILED_MSG = "No se pudo encontrar el porcenjate";
 public static final String PRECONDITION_FAILED_REDIS_MSG = "No se pudo conectar a redis";
}
