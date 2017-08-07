package id;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;



public class GenerateId {
	
	public static List<String> l;
	public static int prefixLength;
	public static int empOrHR=0;
	public static String jobId="";
	public static String emailId="";
	GetPropertyValues properties;
	
	 //  Database credentials
	   private String USER;
	   private String PASS;
	   private String JDBC_DRIVER;  
	   private String DB_URL;
	public GenerateId()  throws IOException {
		l = new ArrayList<String>();
		
		GetPropertyValues properties = new GetPropertyValues();
		
		USER = properties.getPropValues("USER").trim();
		if( USER == null)throw new IOException("Invalid property in config.properties");
		
		PASS = properties.getPropValues("PASS");
		if( PASS == null)throw new IOException("Invalid property in config.properties");

		
		JDBC_DRIVER = properties.getPropValues("JDBC_DRIVER").trim();
		if( JDBC_DRIVER == null)throw new IOException("Invalid property in config.properties");
		
		DB_URL = properties.getPropValues("DB_URL").trim();
		if( DB_URL == null)throw new IOException("Invalid property in config.properties");

	}
	public int insertInDB(String firstName, String lastName, String company, int empOrHR, String emailId) throws SQLException {
		 Connection conn = null;
		   Statement stmt = null;
		   int count = 0;
		   try{
		      //STEP 2: Register JDBC driver
		      
		      try {
		    	  Class.forName(JDBC_DRIVER);
		    	}
		    	catch(ClassNotFoundException ex) {
		    	   System.out.println("Error: unable to load driver class!");
		    	   System.exit(1);
		    	}

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		      

		      //STEP 4: Execute a query
		      System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "INSERT INTO contacts(firstName,lastName,company,HR) values('"+firstName+"','"+lastName+"','"+company+"',"+empOrHR+","+emailId+")";
		    System.out.println(sql);
		      try{
		    	  count = stmt.executeUpdate(sql);
		      }
		      catch(SQLException se)
		      {
		    	  System.out.println("Already exists !");
		      }
		     
		      

		      stmt.close();
		      conn.close();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
			   
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try

		   }//end try
		      System.out.println("DB insert result: "+count);
			   return count;		   	
		   }
	
	private void addSuffixAndPutInList(String c) {
		
		c = c.replaceAll("\\s+","");
		System.out.println(c);
		
		int len = l.size();
		
		for(int i=0;i<len;i++)
		{
			l.add(l.get(i)+ "@" + c +"."+ "com");
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
		
		//jon.d@abc.com
		
		sb.setLength(0);

		sb.append(first);
		sb.append('.');
		sb.append(last.charAt(0));
		l.add(sb.toString());
		
		//doe.jon@abc.com
		
		sb.setLength(0);
		sb.append(last);
		sb.append('.');
		sb.append(first);
		l.add(sb.toString());
		
	//	addNums();
		
		prefixLength = l.size();
		
		addSuffixAndPutInList(c);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner in = new Scanner(System.in);
		System.out.println("Email ID Generator\n\n");

		System.out.println("First Name");
		String firstName = in.nextLine();

		System.out.println("Last Name");
		String lastName = in.nextLine();

		System.out.println("Company Name");
		String company = in.nextLine();
		
		
		System.out.println("Enter 1 for HR, 2 for Employee, 3 to enter email ID");
		empOrHR = in.nextInt();
		
		if(empOrHR == 2)
		{
			System.out.println("Enter job id");
			in.nextLine();
			jobId = in.nextLine();
			 
		}
		if(empOrHR == 3)
		{
			System.out.println("Enter email id");
			in.nextLine();
			emailId = in.nextLine();
			
			try{
			GenerateId genId = new GenerateId();
			int dbResult = 0;
			int mailAgain = 0;
			
			genId.generateNames(firstName, lastName, company);
			try {
				dbResult = genId.insertInDB(firstName, lastName, company, empOrHR,emailId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(dbResult != 1)
			{
				System.out.println("Already contacted. Want to mail again ? Enter 1 for Yes. ");
				mailAgain = in.nextInt();
				if(mailAgain != 1)
				{
					in.close();
				return;
				}
			}
			SendFileEmail em = new SendFileEmail();
			em.sendMail(emailId,firstName, company,1,jobId);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
		}
		else
		{
		System.out.println("Job id = "+jobId);
		try{
			GenerateId genId = new GenerateId();
		
		int dbResult = 0;
		int mailAgain = 0;
		
		genId.generateNames(firstName, lastName, company);
//		try {
//			dbResult = genId.insertInDB(firstName, lastName, company, empOrHR,emailId);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(dbResult != 1)
//		{
//			System.out.println("Already contacted. Want to mail again ? Enter 1 for Yes. ");
//			mailAgain = in.nextInt();
//			if(mailAgain != 1)
//			{
//				in.close();
//			return;
//			}
//		}
		//Send email to generated ids
			for(int i= prefixLength;i<l.size();i++)
			{
				System.out.println(l.get(i));
				
				SendFileEmail em = new SendFileEmail();
				em.sendMail(l.get(i),firstName, company,empOrHR,jobId);
				
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally{
			in.close();
		}
	}
}

}
