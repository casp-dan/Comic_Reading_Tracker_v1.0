CREATE TABLE CollectedIssues (
    IssueName varchar(75) NOT NULL,
    SeriesName varchar(75) NOT NULL,
    XmenAdj tinyint(1) DEFAULT NULL,
    PRIMARY KEY (IssueName,SeriesName)
)

CREATE TABLE Issue (
    IssueName varchar(75) NOT NULL,
    SeriesName varchar(100) NOT NULL,
    DateString datetime NOT NULL,
    XmenAdj tinyint(1) DEFAULT NULL,
    PRIMARY KEY (IssueName,SeriesName,DateString)
)

CREATE TABLE Series (
    SeriesName varchar(100) NOT NULL,
    Publisher varchar(75) DEFAULT NULL,
    Xmen tinyint(1) DEFAULT NULL,
    PRIMARY KEY (SeriesName)
)

CREATE TABLE publisher (
    publisher varchar(100) NOT NULL,
    list_order int DEFAULT NULL,
    PRIMARY KEY (publisher)
)
