package ramsey;
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
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
		String subject = arguments[0];
		String body = arguments[1];
		final Config config = new Config();
		
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(config.EMAIL_USER_ID,config.EMAIL_PASSWORD); 
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(config.EMAIL_ADDRESS));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(config.EMAIL_ADDRESS));
			message.setSubject(subject);
			message.setText(body);
 
			Transport.send(message);
  
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}