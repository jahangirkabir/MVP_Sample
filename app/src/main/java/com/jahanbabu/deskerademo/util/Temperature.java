package com.jahanbabu.deskerademo.util;

public enum Temperature {
  FAHRENHEIT("Fahrenheit"), CELSIUS("Celsius");
  private final String value;

  Temperature(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
