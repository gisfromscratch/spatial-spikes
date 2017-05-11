//package de.edu.geonames.spatial;

// Using the default package for simple running from atom IDE

import java.io.*;

public class GeonamesSpatialApp {

  private static final String defaultFilePath = "../../data/Cities15000.txt";

  public static void main(String[] args) {
    String geonamesFilePath = null;
    boolean readFileArg = false;
    for (String arg : args) {
      if (0 == "-f".compareTo(arg)) {
        readFileArg = true;
      } else {
        if (readFileArg) {
          geonamesFilePath = arg;
          readFileArg = false;
        }
      }
    }

    if (null == geonamesFilePath) {
      geonamesFilePath = defaultFilePath;
    }

    System.out.println("Geonames");

    GeonamesFileReader reader = new GeonamesFileReader();
    try {
      GeonamesFileReader.FileReadResult result = reader.readFile(geonamesFilePath);
      System.out.println(String.format("%d lines read", result.getLineCount()));
    } catch (IOException ex) {
      System.err.println(ex.getMessage());
    }
  }
}
