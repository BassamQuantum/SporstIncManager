package com.example.bassam.sporstincmanger.Interfaces;

/**
 * Created by Bassam on 1/8/2018.
 */

public interface Constants {

    String server = "http://173.212.198.28:8010/sports_inc/api/";
    String upload_host = "http://173.212.198.28:8010/sports_inc/assets/uploads/";
    String upload_files_host = "http://173.212.198.28:8010/sports_inc/assets/uploads/files/";
    String UPLOAD_URL = "http://173.212.198.28:8010/sports_inc/academy/upload_Image";

    String selectData = server + "selectdata";
    String selectMangerData = server + "selectMangerRequests";
    String insertData = server + "insertdata ";
    String insertNotify = server + "insertNotification";
    String sendReminder = server + "sendRemind";
    String classesData = server + "class_Details";
    String attendanceReport = server + "courses_Details";
    String attendanceReportDetails = server + "Groups_details";
    String paidReport = server + "payment_details";
    String updateData = server + "updatedata";
    String deleteData = server + "deletedata";
    String ClassesTrainee = server + "trainee_attendance";
    String TraineePayment = server + "trainee_payment";
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
