package graduation.middleware.model;


import com.google.gson.annotations.SerializedName;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Oletus
 */
public class StudentInfo {

    @SerializedName("graduationPhaseECs")
    private Integer ecs;
    
    @SerializedName("mentor")
    private String mentor;

    public Integer getEcs() {
        return ecs;
    }

    public void setEcs(Integer ecs) {
        this.ecs = ecs;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }
    
    public StudentInfo(){

    }    
    
    public StudentInfo(Integer graduationPhaseECs, String mentor){
        this.ecs = graduationPhaseECs;
        this.mentor = mentor;
    }
    
}
