package com.example.bassam.sporstincmanger.Interfaces;

/**
 * Created by Bassam on 1/8/2018.
 */

public interface Constants {

    String local_server = "http://192.168.1.13:8080/sport_inc/api/";

    String server = "http://173.212.198.28:8010/sport_inc/api/";
    String upload_host = "http://173.212.198.28:8010/sport_inc/assets/uploads/";
    String UPLOAD_URL = "http://173.212.198.28:8010/sport_inc/academy/upload_Image";

    String selectData = server + "selectdata";
    String selectMangerData = server + "selectMangerRequests";
    String insertData = server + "insertdata ";
    String classesData = server + "class_Details";
    String attendanceReport = server + "courses_Details";
    String attendanceReportDetails = local_server + "Groups_details";
    String paidReport = server + "payment_details";
    String updateData = server + "updatedata";
    String deleteData = server + "deletedata";
    String ClassesTrainee = server + "trainee_attendance";
    String join = server + "joindata";
    String sendSMS = server + "sendSMS";
    String notification = server + "selectNotification";
    String profile = "profile_img";
    String profile_host = upload_host + profile +"/";
    String certification = "certifications";
    String certification_host = upload_host + certification +"/";
    String others = "other_img";
    String others_host = upload_host + others +"/";
}
