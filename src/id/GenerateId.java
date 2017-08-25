package id;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateId {

	public static List<String> l;
	public static int prefixLength;
	public static int empOrHR = 0;
	public static String jobId = "";
	public static String emailId = "";

	GetPropertyValues properties;
	private FileOperations fileOperations;
	private static int emailCounter = 0;
	private static int recipientCount = 0;
	private static int inputType = 0;
	private static String inputFile;
	private static String outputFile;
	// Database credentials
	private String USER;
	private String PASS;
	private String JDBC_DRIVER;
	private String DB_URL;

	public GenerateId() throws IOException {
		l = new ArrayList<String>();

		GetPropertyValues properties = new GetPropertyValues();

		USER = properties.getPropValues("USER").trim();
		if (USER == null)
			throw new IOException("Invalid property in config.properties");

		PASS = properties.getPropValues("PASS");
		if (PASS == null)
			throw new IOException("Invalid property in config.properties");

		JDBC_DRIVER = properties.getPropValues("JDBC_DRIVER").trim();
		if (JDBC_DRIVER == null)
			throw new IOException("Invalid property in config.properties");

		DB_URL = properties.getPropValues("DB_URL").trim();
		if (DB_URL == null)
			throw new IOException("Invalid property in config.properties");

		try {
			inputType = Integer.parseInt(properties.getPropValues("inputType").trim());
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (inputType == 2) {
			try {
				inputFile = properties.getPropValues("recruiterInfoFilename").trim();
				outputFile = properties.getPropValues("alreadyMailedRecruiterInfoFilename").trim();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fileOperations = new FileOperations(inputFile, outputFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public int insertInDB(String firstName, String lastName, String company, int empOrHR, String emailId)
			throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		int count = 0;
		try {
			// STEP 2: Register JDBC driver

			try {
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException ex) {
				System.out.println("Error: unable to load driver class!");
				System.exit(1);
			}

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "INSERT INTO contacts(firstName,lastName,company,HR) values('" + firstName + "','" + lastName + "','"
					+ company + "'," + empOrHR + "," + emailId + ")";
			System.out.println(sql);
			try {
				count = stmt.executeUpdate(sql);
			} catch (SQLException se) {
				System.out.println("Already exists !");
			}

			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try

		} // end try
		System.out.println("DB insert result: " + count);
		return count;
	}

	private void addSuffixAndPutInList(String c) {

		c = c.replaceAll("\\s+", "");

		int len = l.size();

		for (int i = 0; i < len; i++) {
			l.add(l.get(i) + "@" + c + "." + "com");
		}

	}

	private void addNumsInMiddle() {

		StringBuilder sb = new StringBuilder();
		int len = l.size();

		for (int x = 0; x < len; x++) {
			for (int i = 1; i <= 1; i++) {
				// jon.(1-9).doe@abc.com
				sb.setLength(0);
				sb.append(l.get(x));
				sb.append('.');
				sb.append(String.valueOf(i));

				l.add(sb.toString());
			}
		}

	}

	private void addNums() {
		StringBuilder sb = new StringBuilder();

		int len = l.size();

		for (int x = 0; x < len; x++) {
			for (int i = 1; i <= 1; i++) {
				// jon.doe1-9@abc.com

				sb.setLength(0);
				sb.append(l.get(x));
				sb.append(String.valueOf(i));

				l.add(sb.toString());

				// jon.doe.1-9@abc.com

				sb.setLength(0);
				sb.append(l.get(x));
				sb.append('.');
				sb.append(String.valueOf(i));

				l.add(sb.toString());

			}
		}
	}

	private void generateNames(String first, String last, String c) {

		first = first.toLowerCase().trim();
		last = last.toLowerCase().trim();
		c = c.toLowerCase();

		// JDoe@abc.com
		StringBuilder sb = new StringBuilder();

		sb.append(first.charAt(0));
		sb.append(last);

		l.add(sb.toString());

		// Jon.Doe@abc.com
		sb.setLength(0);

		sb.append(first);
		sb.append('.');
		sb.append(last);
		l.add(sb.toString());

		// Jon@abc.com
		sb.setLength(0);

		sb.append(first);
		l.add(sb.toString());

		// DJon@abc.com

		sb.setLength(0);

		sb.append(last.charAt(0));
		sb.append(first);
		l.add(sb.toString());

		// jon.d@abc.com

		sb.setLength(0);

		sb.append(first);
		sb.append('.');
		sb.append(last.charAt(0));
		l.add(sb.toString());

		// doe.jon@abc.com

		sb.setLength(0);
		sb.append(last);
		sb.append('.');
		sb.append(first);
		l.add(sb.toString());

		// addNums();

		prefixLength = l.size();

		addSuffixAndPutInList(c);
	}

	private void incrementEmailCounter() {

		// increase count by number of different ids generated for current
		// recipient
		emailCounter += l.size();
	}

	private int getSleepInterval() {

		if(inputType == 1)return 0;
		
		// After each recipient, sleep for 10 seconds, after 3 recipients sleep
		// for 20 seconds

		if (recipientCount>0 && recipientCount % 3 == 0)
			return 20000;

		return 10000;
	}


	private void generateIdsAndSendMail(String firstName, String lastName, String company) throws IOException {

		int dbResult = 0;
		int mailAgain = 0;

		generateNames(firstName, lastName, company);
		// try {
		// dbResult = genId.insertInDB(firstName, lastName, company,
		// empOrHR,emailId);
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// if(dbResult != 1)
		// {
		// System.out.println("Already contacted. Want to mail again ?
		// Enter 1 for Yes. ");
		// mailAgain = in.nextInt();
		// if(mailAgain != 1)
		// {
		// in.close();
		// return;
		// }
		// }
		// Send email to generated ids
		List<MailerThread> threadList = new ArrayList<MailerThread>();
		for (int i = prefixLength; i < l.size(); i++) {
			System.out.println(l.get(i));

			// SendFileEmail em = new SendFileEmail();
			// em.sendMail(l.get(i),firstName, company,empOrHR,jobId);

			MailerThread thread = new MailerThread(l.get(i), firstName, company, empOrHR, jobId);
			threadList.add(thread);
			 thread.start();
			emailCounter++;

		}
		// Clear the list for the next recipient
		l.clear();
		for (MailerThread thread : threadList) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		recipientCount++;
		incrementEmailCounter();
		System.out.println("emailCounter = " + emailCounter);

		int sleepInterval = getSleepInterval();
		System.out.println("SleepInterval = " + sleepInterval);
		try {
			Thread.sleep(sleepInterval);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMails() {
		String firstName = "", lastName = "", company = "";
		Scanner in = new Scanner(System.in);

		if (inputType == 1) { // config to read input from input stream

			System.out.println("Email ID Generator\n\n");

			System.out.println("First Name");
			firstName = in.nextLine();

			System.out.println("Last Name");
			lastName = in.nextLine();

			System.out.println("Company Name");
			company = in.nextLine();

			System.out.println("Enter 1 for HR, 2 for Employee, 3 to enter email ID");
			empOrHR = in.nextInt();

			if (empOrHR == 2) {
				System.out.println("Enter job id");
				in.nextLine();
				jobId = in.nextLine();

			}
			if (empOrHR == 3) {
				System.out.println("Enter email id");
				in.nextLine();
				emailId = in.nextLine();
			}
			if (empOrHR == 3) {
				SendFileEmail em;
				try {
					em = new SendFileEmail();
					em.sendMail(emailId, firstName, company, empOrHR, jobId);
					// Sent mail to input emailId, now exit
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {

			if (inputType == 1) {
				generateIdsAndSendMail(firstName, lastName, company);
			} else if (inputType == 2) {
				try {
					List<String> recipientInfoList = fileOperations.readRecords();

					for (String recipientInfo : recipientInfoList) {
						Pattern r = Pattern.compile("(.*?)\\s+(.*?)\\s+(.*)");
						Matcher m = r.matcher(recipientInfo);

						if (m.find()) {
							generateIdsAndSendMail(m.group(1), m.group(2), m.group(3));
						} else {
							throw new Exception("Regex not matched for record :" + recipientInfo);
						}

					}

					fileOperations.writeRecords();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
	}

	public static void main(String[] args) {

		GenerateId genId;
		try {
			genId = new GenerateId();
			genId.sendMails();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
