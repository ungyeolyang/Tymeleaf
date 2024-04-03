package com.kh.project.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardVO {
    private String commentNo;
    private String nutrientsName;
    private String userId;
    private String content;
    private String userNick;
}
