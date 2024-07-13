CREATE TABLE Series (
    SeriesID INTEGER not null auto_increment primary key,
    SeriesTitle VARCHAR(45),
    issueID INTEGER,
    Publisher VARCHAR(45),
    xmen boolean
);

CREATE TABLE Comic (
    issueID INTEGER not null auto_increment primary key,
    SeriesID INTEGER,
    issueName VARCHAR(45),
    dateString VARCHAR(45),
    month INTEGER,
    day INTEGER,
    year INTEGER
);



getting xmen query:
    Select SeriesID from Series where xmen=1;
    SELECT COUNT(*) FROM Comic WHERE month=2 and year=22 AND SeriesID=SeriesID;