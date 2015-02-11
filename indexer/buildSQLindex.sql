/* Script to create the location table with a spatial index */



CREATE TABLE Location(
item_id INT NOT NULL,
g GEOMETRY NOT NULL)   
ENGINE=MyISAM;


/* load data into location table */
INSERT INTO Location(item_id, g)
SELECT item_id, Point(1,1)
FROM Item
WHERE latitude IS NOT NULL AND longitude IS NOT NULL;

UPDATE Location,Item 
SET g = PointFromText(CONCAT('POINT(',longitude,' ',latitude,')')) 
WHERE Location.item_id=Item.item_id;


/* Create the spacial index */
CREATE SPATIAL INDEX sp_index ON Location(g);
