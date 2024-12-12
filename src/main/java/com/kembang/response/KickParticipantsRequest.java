package com.kembang.response;

import lombok.Data;
import java.util.List;

@Data
public class KickParticipantsRequest {

    private String fromUserSecureId; // ID of the user requesting to kick participants
    private List<String> kickParticipantSecureIds; // List of participant IDs to be removed from the chat room
}
