/* Script to create the location table with a spatial index */



CREATE TABLE Location(
item_id INT NOT NULL,
g GEOMETRY NOT NULL)   
ENGINE=MyISAM;


/* load data into location table */
INSERT INTO Location(item_id, g)
VALUES (
    SELECT item_id, GeomFromText('POINT(lat,long)')
    FROM Item
);


/* Create the spacial index */
CREATE SPATIAL INDEX sp_index ON Location(g);
