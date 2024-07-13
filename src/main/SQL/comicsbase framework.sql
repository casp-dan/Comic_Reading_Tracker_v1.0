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
    day INTEGER,
    month INTEGER,
    year INTEGER
);