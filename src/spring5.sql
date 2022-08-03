-- spring5 테이블 구조

-- 테이블 생성 user info
DROP TABLE spring5_member;
CREATE TABLE spring5_member (
    memberid     varchar2(20)    constraint userid_PK primary key                                  -- 사용자 식별 ID, pk
    ,memberpw    varchar2(100)   constraint usrpw_NN not null                                      -- 사용자 비밀번호, nn (암호화하여 저장)
    ,membername  varchar2(20)    constraint usrname_NN not null                                    -- 사용자 이름
    ,email       varchar2(50)                                                                      -- 이메일
    ,phone       varchar2(30)                                                                      -- 전화번호
    ,address     varchar2(30)                                                                      -- 주소
    ,enabled     number(1)
                                    default 1
                                    constraint account_status_CK check ( enabled in ( 1, 0 ) )                        -- 계정 상태 CK
    ,rolename    varchar2(20)
                                    default 'ROLE_USER'
                                    constraint role_CK check ( rolename in ( 'ROLE_USER', 'ROLE_ADMIN' ) )            -- 역할 CK, default ROLE_USR
);

-- 데이터 삽입 user info
insert into spring5_member
values ('aaa', '111', '차슈', 'aaa@aaa.com', '010-1234-5678', '서울시 녀러분', 1, 'ROLE_USER');

insert into spring5_member (memberid, memberpw, membername)
values ('bbb', '222', '콩이');


-- board
CREATE TABLE spring5_board (
                               boardnum            number               constraint spring5_boardNum_pk primary key  ,          -- 글번호
                               memberid            varchar2(20)         references SPRING5_MEMBER(memberid) ,                  -- 작성자
                               title               varchar2(300)        not null                            ,                  -- 제목
                               textarea            varchar2(4000)       not null                            ,                  -- 내용
                               inputdate           date                 default SYSDATE                     ,                  -- 작성시간
                               hits                number               default 0                           ,                  -- 조회수
                               originalfile        varchar2(300)                                            ,                  -- 첨부파일 원래 이름
                               savedfile           varchar2(100)                                                               -- 서버에 저장된 이름
);


-- board sequence
CREATE SEQUENCE spring5_board_seq;


-- insert board
INSERT INTO spring5_board (boardnum, memberid, title, textarea)
VALUES (spring5_board_seq.nextval, 'cha1', '복붙중', '복붙중') ;

-- reply
CREATE TABLE spring5_reply (
                               replynum            number          primary key ,                                           -- 댓글 일련번호
                               boardnum            number          references spring5_board(boardnum) on delete cascade ,  -- 게시물 일련번호
                               memberid            varchar2(20)    references spring5_member(memberid) ,                   -- 작성자 아이디
                               textarea            varchar2(200)   NOT NULL ,                                              -- 댓글 내용
                               inputdate           date            DEFAULT SYSDATE                                         -- 작성 시간
);


-- reply sequence
CREATE SEQUENCE spring5_replay_seq;


-- insert reply
INSERT INTO spring5_reply (replynum, boardnum, memberid, textarea)
VALUES (spring5_replay_seq.nextval, 1, 'cha', '사료삽니다') ;


select * from spring5_board;

select * from spring5_reply;