package com.jahanbabu.deskerademo.util;

public interface AppConstants {
  long SPLASH_DURATION_MILLIS = 3 * 1000;
  String JPG_EXTENSION = ".jpg";
  String PROVIDER = ".provider";
  String IMAGE_FOLDER = "/DeskeraProfile/";
  String IMAGE_TYPE = "image/*";
  String ARG_UNIT = "arg_unit";
  String ARG_TABLE_ITEM_ID = "ARG_TABLE_ITEM_ID";
  String ARG_TABLE_ITEM = "ARG_TABLE_ITEM_NAME";
  String DESKERA_DATE_FORMAT = "dd/M/yyyy";

  long SECONDS_TO_MILLIS = 1000;
  long MINUTES_TO_MILLIS = SECONDS_TO_MILLIS * 60;
  long HOURS_TO_MILLIS = MINUTES_TO_MILLIS * 60;
  long DAYS_TO_MILLIS = HOURS_TO_MILLIS * 24;
  String ARG_DATE_IN_MILLS = "ARG_DATE_IN_MILLS";

  interface RequestCodes {
    int REQUEST_CAMERA = 1234;
    int REQUEST_GALLERY = 9162;
    int PERMISSIONS_REQUEST_WRITE_STORAGE = 778;
    int REQUEST_TEMPERATURE_UNIT = 323;
  }
}
