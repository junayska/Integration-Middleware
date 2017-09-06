package graduation.middleware;




import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
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
abstract class GatewayReceiver {
    
        abstract void newMessage(Message msg);
        
        Connection connection; // to connect to the ActiveMQ
        Session session; // session for creating messages, producers and    
        Destination receiveDestination; // reference to a queue/topic destination
        MessageConsumer consumer; // for receiving messages
    
    private String queue;

    public GatewayReceiver(String q){
        this.queue = q;
        receiveQueue();
    }
    
    public String getQueue() {return queue;}
    public void setQueue(String queue) {this.queue = queue;}
 
    public void receiveQueue(){     
            try {
                Properties props = new Properties();
                props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                        "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
                props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
         
                // connect to the Destination called “myFirstChannel”
                // queue or topic: “queue.myFirstDestination” or 
                //                 “topic.myFirstDestination”
                props.put(("queue."+queue), queue);
        
                Context jndiContext = new InitialContext(props);
                ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
                                        .lookup("ConnectionFactory");
                connection = connectionFactory.createConnection();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
                // connect to the receiver destination
                receiveDestination = (Destination) jndiContext.lookup(queue);
                consumer = session.createConsumer(receiveDestination);
        
            
                MessageListener listener = new MiddlewareMessageListener();
                consumer.setMessageListener(listener);
                connection.start(); // this is needed to start receiving messages
                
                } catch (NamingException | JMSException e) {
                    e.printStackTrace();
                }
        }
    
    public class MiddlewareMessageListener implements MessageListener{
        @Override
        public void onMessage(Message msg) {   
            newMessage(msg);
        }
    }
    
}
