import java.io.*;

public class GeonamesFileReader {

  public FileReadResult readFile(String filePath) throws IOException {
    int lineNumber = 0;
    LineNumberReader reader = new LineNumberReader(new FileReader(filePath));
    try {
      String line;
      while (null != (line = reader.readLine())) {
        lineNumber++;
        String[] tokens = line.split("\t");
        if (5 < tokens.length) {
          int id = Integer.parseInt(tokens[0]);
          double lat = Double.parseDouble(tokens[4]);
          double lon = Double.parseDouble(tokens[5]);
          if (lat < -90 || 90 < lat) {
            System.err.println(String.format("Feature %d has an invalid latitude!", id));
          }
          if (lon < -180 || 180 < lon) {
            System.err.println(String.format("Feature %d has an invalid longitude!", id));
          }
        }
      }
    } finally {
      reader.close();
    }

    return new FileReadResult(lineNumber);
  }

  class FileReadResult {

    private final int lineCount;

    FileReadResult(int lineCount) {
      this.lineCount = lineCount;
    }

    int getLineCount() {
      return lineCount;
    }
  }
}
