import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class SendEmail {
	public SendEmail(String[] arguments) {
		// TODO Auto-generated constructor stub
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
		String numOfElements = arguments[0];
		String cliqueSize = arguments[1];
		String table = arguments[2];
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("ben.ferenchak","qazxsw1200");
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ben.ferenchak@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("ben.ferenchak@gmail.com"));
			message.setSubject("Ramsey Solution Found");
			message.setText("Number of Elements: " + numOfElements + "\n"
					       + "Clique Size: " + cliqueSize + "\n"
					       + "Caylee Table:\n" + table );
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}