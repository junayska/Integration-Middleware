package graduation.middleware;


import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import graduation.middleware.model.GraduationApprovalRequest;
import graduation.middleware.model.GraduationClientRequest;
import graduation.middleware.model.StudentInfo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Oletus
 */
public class Serializer {

public String getGraduationApprovalRequestAsString(StudentInfo si, GraduationClientRequest gcr){
    GraduationApprovalRequest gar = new GraduationApprovalRequest(
                                            gcr.getStudentNumber(),
                                            gcr.getCompany(),
                                            gcr.getProjectTitle(),
                                            si.getEcs(),
                                            si.getMentor());
    Gson gson = new Gson();
    return gson.toJson(gar);
}

public GraduationClientRequest serializeMessageToGraduationClientRequest(Message msg){
    TextMessage txtMsg = (TextMessage) msg;
    Gson gson = new Gson();
    try {
        return gson.fromJson(txtMsg.getText(), GraduationClientRequest.class);
    } catch (JMSException ex) {
        Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}
    
}
