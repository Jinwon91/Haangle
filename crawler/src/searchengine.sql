DROP TABLE content_table CASCADE CONSTRAINTS ;
 
DROP TABLE word_table CASCADE CONSTRAINTS ;
 
CREATE TABLE content_table
  (
    content_idx INTEGER NOT NULL ,
    url varchar2(1000) ,
    content clob,
    title varchar2(255),
    regdate DATE ,
    hit    INTEGER,
    pagerank INTEGER
  ) ;
ALTER TABLE content_table ADD CONSTRAINT content_table_PK PRIMARY KEY ( content_idx ) ;
 
 
CREATE TABLE word_table
  (
    word_idx    INTEGER NOT NULL ,
    word        VARCHAR2 (100) NOT NULL ,
    content_idx INTEGER ,
    hit         INTEGER,
    position    integer,
    word_type   varchar(3)
  ) ;
CREATE INDEX word_table__IDX ON word_table
  ( 
    word
  ) ;
ALTER TABLE word_table ADD CONSTRAINT word_table_PK PRIMARY KEY ( word_idx ) ;
 
drop sequence content_seq;
create sequence content_seq;
 
drop sequence word_seq;
create sequence word_seq;
commit;
