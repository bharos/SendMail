package id;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileOperations {

	private List<String> records = new ArrayList<String>();

	private String inputFile;
	private String outputFile;

	public FileOperations(String inputFile, String outputFile) throws Exception {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

	public List<String> readRecords() throws Exception {
		String line = null;

		// wrap a BufferedReader around FileReader
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));

		// use the readLine method of the BufferedReader to read one line at a
		// time.
		// the readLine method returns null when there is nothing else to read.
		while ((line = bufferedReader.readLine()) != null) {
			records.add(line.trim());
		}

		// close the BufferedReader when we're done
		bufferedReader.close();
		return records;
	}

}
