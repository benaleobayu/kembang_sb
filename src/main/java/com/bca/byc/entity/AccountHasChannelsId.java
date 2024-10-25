package com.bca.byc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountHasChannelsId implements Serializable {
    private Long accountId;
    private Long channelId;
}
