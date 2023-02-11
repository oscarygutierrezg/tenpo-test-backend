package com.tenpo.test.constants;


public class Constants {
 private Constants() {
  throw new IllegalStateException("Utility class");
 }

 public static final  String CURRENT_PERCENTAGE_VALUE = "PERCENTAGE_CURRENT_VALUE";
 public static final String TOO_MANY_REQUESTS_MSG = "Alcanzaste el máximo de requests permitidos por minuto, espera que pase un minuto y vuelve a intentar";
 public static final String PRECONDITION_FAILED_MSG = "No se pudo encontrar el porcenjate";
}
