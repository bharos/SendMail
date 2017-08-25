package id;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileOperations {

	private String inputFile;
	private String outputFile;
	private Set<String> recordsWithoutSpaces;
	List<String> records;

	public FileOperations(String inputFile, String outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.recordsWithoutSpaces = new HashSet<String>();
		records = new ArrayList<String>();
	}

	private List<String> readRecords(String fileName) throws IOException {
		String line = null;
		List<String> records = new ArrayList<String>();
		// wrap a BufferedReader around FileReader
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

		// use the readLine method of the BufferedReader to read one line at a
		// time.
		// the readLine method returns null when there is nothing else to read.
		while ((line = bufferedReader.readLine()) != null) {
			String record = line.trim();
			records.add(record);
		}

		// close the BufferedReader when we're done
		bufferedReader.close();
		return records;
	}

	public List<String> readRecords() throws IOException {
		
		storeAlreadyMailedInfo();
		// Store the records to write to outputFile(avoid reading again from
		// inputFile)
		
		records = readRecords(inputFile);
		records.removeIf(record -> containsRecord(record));

		return records;
	}

	private void storeAlreadyMailedInfo() {
		List<String> alreadyMailedRecords;
		try {
			alreadyMailedRecords = readRecords(this.outputFile);
			for (String alreadyMailedRecord : alreadyMailedRecords) {
				String recordWithoutSpaces = alreadyMailedRecord.replaceAll("\\s+", "");
				recordsWithoutSpaces.add(recordWithoutSpaces);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void writeRecords() throws IOException {

		// Create fileWriter to append records to the output file
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputFile, true));

		for (String record : records) {
			writer.append(record);
			writer.newLine();
		}

		writer.close();
	}

	public Boolean containsRecord(String... info) {
		String appendedInfo = "";

		for (String r : info)
			appendedInfo += r;

		String recordWithoutSpaces = appendedInfo.replaceAll("\\s+", "");

		return recordsWithoutSpaces.contains(recordWithoutSpaces);

	}

}
