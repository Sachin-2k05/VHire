package com.example.vHire.Config;

public class EmailTemplates {

    public static String bookingRequest(
            String companyName,
            String date,
            String time
    ) {
        return """
            <h2>New Booking Request</h2>
            <p><b>%s</b> has sent you a booking request.</p>
            <p>Date: <b>%s</b></p>
            <p>Time: <b>%s</b></p>
            <hr>
            <p style="color:gray">vHire Team</p>
        """.formatted(companyName, date, time);
    }

    public static String bookingAccepted(
            String workerName,
            String date,
            String time
    ) {
        return """
            <h2>Booking Accepted</h2>
            <p>Your booking with <b>%s</b> has been accepted.</p>
            <p>Date: <b>%s</b></p>
            <p>Time: <b>%s</b></p>
            <hr>
            <p style="color:gray">vHire Team</p>
        """.formatted(workerName, date, time);
    }

    public static String bookingRejected(
            String workerName
    ) {
        return """
            <h2>Booking Rejected</h2>
            <p>Your booking with <b>%s</b> was rejected.</p>
            <hr>
            <p style="color:gray">vHire Team</p>
        """.formatted(workerName);
    }
}