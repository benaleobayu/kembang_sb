package com.kembang.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ChatMessage {

    private String id;
    private String nickname;
    private String content;
    private Date timestamp;

}
