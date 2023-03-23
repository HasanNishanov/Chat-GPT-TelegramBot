package com.company.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private Integer id;
    private String question;
    private String answer;
    private String tg_id;

}
