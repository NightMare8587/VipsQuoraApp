package com.example.vipsquoraapp.HomeScreen.Fragments;

public class ReportThredClass {
    public String createdBy,threadName,threadID,reportedBy,reason,creatorID,emailOfReporter;

    public ReportThredClass(String createdBy, String threadName, String threadID, String reportedBy, String reason,String creatorID,String emailOfReporter) {
        this.createdBy = createdBy;
        this.emailOfReporter = emailOfReporter;
        this.threadName = threadName;
        this.creatorID = creatorID;
        this.threadID = threadID;
        this.reportedBy = reportedBy;
        this.reason = reason;
    }
}
