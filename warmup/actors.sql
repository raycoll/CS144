/* Create Actors table */
CREATE TABLE Actors(Name VARCHAR(40), Movie VARCHAR(80), Year INTEGER, Role VARCHAR(40));

/* Load the Actor data file */
LOAD DATA LOCAL INFILE "~/data/actors.csv" INTO TABLE Actors FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';

/* Get all actors in "Die Another Day"  */
SELECT Name FROM Actors WHERE Movie="Die Another DAY";

/* Drop the experimental Actors table */
DROP TABLE Actors;


