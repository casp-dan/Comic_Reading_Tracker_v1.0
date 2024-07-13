CREATE TABLE Series (
    SeriesID INTEGER not null auto_increment primary key,
    SeriesTitle VARCHAR(45),
    issueID INTEGER,
    Publisher VARCHAR(45),
    xmen boolean
);

CREATE TABLE Comic (
    issueID INTEGER not null auto_increment primary key,
    issueName VARCHAR(45),
    date VARCHAR(45)
);


ALTER TABLE Series
ADD FOREIGN KEY (issueID) REFERENCES Comic(issueID);