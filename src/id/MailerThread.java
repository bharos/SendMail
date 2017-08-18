package id;

import java.io.IOException;

public class MailerThread extends Thread {
  
	String id;
	String firstName;
	String company;
	int empOrHR;
	String jobId;
	public MailerThread(String id, String firstName, String company,int empOrHR,String jobId) {
		this.id = id;
		this.firstName = firstName;
		this.company = company;
		this.empOrHR = empOrHR;
		this.jobId = jobId;
	}
		public void run(){  
//		System.out.println("thread is running..."+id);  
		try {
			SendFileEmail em = new SendFileEmail();
			em.sendMail(id,firstName, company,empOrHR,jobId);
			System.out.println("Mail sent to "+id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		} 

}
