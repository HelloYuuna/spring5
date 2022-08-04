package net.softsociety.spring5.domain;

import lombok.Data;

@Data
public class Reply {
    private int replynum;
    private int boardnum;
    private String memberid;
    private String textarea;
    private String inputdate;
}
