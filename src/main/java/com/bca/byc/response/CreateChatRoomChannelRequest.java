package com.bca.byc.response;
import lombok.Data;
import java.util.List;

@Data
public class CreateChatRoomChannelRequest {
    
    private String channelName;
    private List<String> participantSecureIds;
    private String fromUserSecureId;
}
