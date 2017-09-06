package graduation.middleware;




import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Oletus
 */
public class GatewaySender {
        
    Connection connection; // to connect to the ActiveMQ
    Session session; // session for creating messages, producers and    
    Destination sendDestination; // reference to a queue/topic destination
    MessageProducer producer; // for sending messages
    
    private String queue;
    private String message;
    private String correlationId;
    
    // CONSTRUCTOR, GETTERS AND SETTERS
    public GatewaySender(String queue, String message, String correlationId) {
        this.queue = queue;
        this.message = message;
        this.correlationId = correlationId;
        try {
            sendMessage();
        } catch (JMSException ex) {
            Logger.getLogger(GatewaySender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(GatewaySender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getQueue() {return queue;}
    public void setQueue(String queue) {this.queue = queue;}
    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
    public String getCorrelationId() {return correlationId;}
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;}
    
    // SENDING METHOD
    private void sendMessage() throws JMSException, NamingException{
                    Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            props.put(("queue." + queue + "Queue"), queue + "Queue");       
            Context jndiContext = new InitialContext(props);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                .lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            sendDestination = (Destination) jndiContext.lookup(queue + "Queue");
            producer = session.createProducer(sendDestination);
            Message msgToSend = session.createTextMessage(message);
            msgToSend.setJMSCorrelationID(correlationId);
            producer.send(msgToSend);
            System.out.println("(GatewaySender.java) Sent message to " + queue + "Queue : " + msgToSend);
    }
}
