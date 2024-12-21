CREATE TABLE Series(
    SeriesName VARCHAR(45) primary key,
    Publisher VARCHAR(45),
    Xmen boolean
);

CREATE TABLE Issue(
    IssueName VARCHAR(45) primary key,
    SeriesName VARCHAR(45) foreign key,
    XmenAdj boolean
);

CREATE TABLE Date(
    IssueName VARCHAR(45) primary key foreign key,
    Date VARCHAR(45) primary key,
    Month INTEGER,
    Day INTEGER,
    Year INTEGER
);














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

CREATE TABLE XmenAdjSeries (
    SeriesID INTEGER primary key,
    SeriesTitle VARCHAR(45),
    issueID INTEGER
);

CREATE TABLE XmenAdjComic (
    issueID INTEGER not null auto_increment primary key,
    SeriesID INTEGER,
    issueName VARCHAR(45),
    dateString VARCHAR(45),
    month INTEGER,
    day INTEGER,
    year INTEGER
);