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
    date INTEGER
);


CREATE TABLE Date (
    dateID INTEGER not null auto_increment primary key,
    month INTEGER,
    day INTEGER,
    year INTEGER

);

ALTER TABLE Series
ADD FOREIGN KEY (issueID) REFERENCES Comic(issueID);

ALTER TABLE Comic
ADD FOREIGN KEY (date) REFERENCES Date(dateID);


INSERT INTO Series (SeriesTitle, issueID, Publisher, xmen)
VALUES ('Uncanny X-Men', 0, 'Marvel', 1);

SELECT SeriesID FROM Series
WHERE SeriesTitle='Uncanny X-Men';

INSERT INTO Comic(SeriesID, issueName, date)
VALUES (1, "345", 1);


UPDATE Series SET issueID=issueID+1 WHERE SeriesID=1;