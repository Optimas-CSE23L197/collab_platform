package com.example.collaboration.collab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomJoinRequestDTO {
    private String roomId;
    private String roomPassword;
}
