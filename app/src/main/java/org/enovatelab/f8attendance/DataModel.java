package org.enovatelab.f8attendance;


public class DataModel {

    private String AttendeeCode;

    public DataModel(){}

    public DataModel(String AttendeeCode){
        this.AttendeeCode = AttendeeCode;




    }

    public String getAttendeeCode() {
        return AttendeeCode;
    }

    public void setAttendeeCode(String attendeeCode) {
        AttendeeCode = attendeeCode;
    }
}
