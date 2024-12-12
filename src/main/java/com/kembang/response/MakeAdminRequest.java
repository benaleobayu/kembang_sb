package com.kembang.response;


import lombok.Data;
import java.util.List;

@Data
public class MakeAdminRequest {

    private String fromUserSecureId; // ID of the user requesting to make participants admins
    private List<String> adminParticipantSecureIds; // List of participant IDs to be given admin status
}