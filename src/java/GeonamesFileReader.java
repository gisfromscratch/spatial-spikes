import java.io.*;

public class GeonamesFileReader {

  public FileReadResult readFile(String filePath) throws IOException {
    int lineNumber = 0;
    LineNumberReader reader = new LineNumberReader(new FileReader(filePath));
    try {
      String line;
      while (null != (line = reader.readLine())) {
        lineNumber++;
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
