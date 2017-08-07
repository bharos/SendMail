// File Name SendFileEmail.java

package id;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendFileEmail {
	
	 private String from;
     private String username;
     private String password;
     private String sendername;
     private String resume;
     
     public SendFileEmail() throws IOException 
     	{
     		GetPropertyValues properties = new GetPropertyValues();
     		
     		from = properties.getPropValues("from").trim();
    		if( from == null)throw new IOException("Invalid property in config.properties");
     	    
    		username = properties.getPropValues("username").trim();
     		if( username == null)throw new IOException("Invalid property in config.properties");
     	     
     		password = properties.getPropValues("password");
     		if( password == null)throw new IOException("Invalid property in config.properties");
     		
     		sendername = properties.getPropValues("sendername").trim();
     		if(sendername == null)throw new IOException("Invalid property in config.properties");
     		
     		resume = properties.getPropValues("resume").trim();
     		if(resume == null)throw new IOException("Invalid property in config.properties");
     	}
		int sendMail(String id, String firstName, String company,int empOrHR,String jobId)
		{
			 // Recipient's email ID needs to be mentioned.
		      String to = id;

		      // Sender's email ID needs to be mentioned
		     

		      // Assuming you are sending email through gmail smtp
		      String host = "smtp.gmail.com";

		      Properties props = new Properties();
		      props.put("mail.smtp.auth", "true");
		      props.put("mail.smtp.starttls.enable", "true");
		      props.put("mail.smtp.host", host);
		      props.put("mail.smtp.port", "587");

		      // Get the Session object.
		      Session session = Session.getInstance(props,
		      new javax.mail.Authenticator() {
		         protected PasswordAuthentication getPasswordAuthentication() {
		            return new PasswordAuthentication(username, password);
		         }
		      });

		      try {
		         // Create a default MimeMessage object.
		         Message message = new MimeMessage(session);

		         // Set From: header field of the header.
		         message.setFrom(new InternetAddress(from));

		         // Set To: header field of the header.
		         message.setRecipients(Message.RecipientType.TO,
		         InternetAddress.parse(to));

		         // Set Subject: header field
		         message.setSubject("Software Development Engineer | Former R&D Engineer at HPE");

		         
		         BodyPart messageBodyPart = new MimeBodyPart();

		         Multipart multipart = new MimeMultipart();

		         firstName = Character.toUpperCase(firstName.charAt(0))+firstName.substring(1,firstName.length());
		         company = Character.toUpperCase(company.charAt(0))+company.substring(1,company.length());
		         
		         
		         String greet = "\nHi "+firstName+",\n\n";
		         
		         
		         String hrBody =
"I am a Computer Science Graduate student from Stony Brook University NY, looking for full time roles as a Software Developer.\n"+
"My expected graduation date is December 2017, and I currently have a CGPA of 3.92/4.0. \n"+
"It would be a great opportunity if you can refer me to open requisitions, and I am excited to get a chance to be part of a reputed company as "+company+".\n\n"+
"Please find my resume attached with this mail, and kindly consider me for the respective positions.\n\n"+
"Thanks a lot,\n"+
sendername;
		         
		         String empBody = 
"I am a Computer Science Graduate student from Stony Brook University NY, looking for Summer'17 Internship as a Software Developer.\n"+
"I came across your profile on LinkedIn and am actually sending this mail asking for a help. It would be a great opportunity for me if you can kindly refer me to"+
" the open positions in "+company+".\n"+
"I am excited to get a chance to be part of a reputed company as "+company+".\n"+
"This is the job ID for Internship opening which I got from the "+company+" careers page :\n"+ 
jobId+"\n\n"+
"Thanks in advance !\n\n"+ 
"Have a great day.\n\n"+
"Regards,\n"+
sendername;
		         
		         // Now set the actual message
		         if(jobId != null && !jobId.isEmpty() && empOrHR == 2)
		         {
		        	 messageBodyPart.setText(greet+empBody);
			         
		         }
		         else 
		         {
		        	 messageBodyPart.setText(greet+hrBody);
		         }
		         
		         File here = new File(".");
		         System.out.println(here.getAbsolutePath());
		         
		         // Set text message part
		         multipart.addBodyPart(messageBodyPart);

		         // Part two is attachment
		         messageBodyPart = new MimeBodyPart();
		         DataSource source = new FileDataSource(resume);
		         messageBodyPart.setDataHandler(new DataHandler(source));
		         messageBodyPart.setFileName(resume);
		         multipart.addBodyPart(messageBodyPart);

		         // Send the complete message parts
		         message.setContent(multipart);
		         // Send message
		         Transport.send(message);

		         System.out.println("Sent message successfully....");
		         return 1;
		         
		      } catch (MessagingException e) {
		            throw new RuntimeException(e);
		            
		      }
		}
	  public static void main(String[] args) {
	     
	   }
	}