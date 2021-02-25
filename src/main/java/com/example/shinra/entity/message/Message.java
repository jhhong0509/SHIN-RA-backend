package com.example.shinra.entity.message;

import com.example.shinra.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String room;

    private String sender;

    private String message;

    private boolean isDeleted;

    private boolean isShow;

    public String getMessage() {
        if(isDeleted == true) {
            return "";
        }
        return message;
    }


}
