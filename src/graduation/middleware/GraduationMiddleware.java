package graduation.middleware;

import graduation.middleware.model.GraduationClientRequest;
import graduation.middleware.model.StudentInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
/**
 *
 * @author Oletus
 */
public final class GraduationMiddleware {
	public static void main(String[] args) {
            new GraduationMiddleware();
	}
        
        public GraduationMiddleware(){
            new GatewayReceiver(MIDDLEWARE) {
                @Override
                void newMessage(Message msg) {
                    try {
                        handleMessage(msg);
                    } catch (JMSException ex) {
                        Logger.getLogger(GraduationMiddleware.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
        }
        
        public static final String APPROVAL = "approval";
        public static final String CLIENT = "client";
        public static final String MIDDLEWARE = "middlewareQueue";
        
        private void handleMessage(Message msg) throws JMSException {
            System.out.println("(GraduationMiddleware.java) handleMessage: " + msg);
            TextMessage txtMsg = (TextMessage) msg;
            if (txtMsg.getJMSCorrelationID() == null){
                System.out.println("(GraduationMiddleware.java) FORWARDING MESSAGE TO THE APPROVAL");
                onRequestOperation(msg);
            } else
            {
                System.out.println("(GraduationMiddleware.java) FORWARDING MESSAGE TO THE CLIENT");
                onReplyOperation(msg);
            }
        }
        
        // Message is handled in this method if there is correlationId
        private void onReplyOperation(Message msg) throws JMSException{
            TextMessage txtMsg = (TextMessage) msg;
            new GatewaySender(CLIENT,txtMsg.getText(),msg.getJMSCorrelationID());
        }
        
        // Message is handled in this method if correlationId equals null
        private void onRequestOperation(Message msg) throws JMSException{
            System.out.println("(GraduationMiddleware.java) onRequestOperation: " + msg);
            RestRequester rr = new RestRequester();
            Serializer serializer = new Serializer();
            GraduationClientRequest gcr = serializer.serializeMessageToGraduationClientRequest(msg);           
            StudentInfo si = rr.getStudentInfo(gcr.getStudentNumber());
            String msgToPost = serializer.getGraduationApprovalRequestAsString(si, gcr);
            // Next if statement check if the ECs value is between 24-29
            if (si.getEcs()>=24 && si.getEcs()<=29){
                 new GatewaySender(si.getMentor().toLowerCase(),msgToPost,msg.getJMSMessageID());    
            }
            new GatewaySender(APPROVAL,msgToPost,msg.getJMSMessageID());
        }
}