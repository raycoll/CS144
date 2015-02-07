/* Drops the previous created Location table and associated spatial index */

DROP TABLE IF EXISTS Location;
DROP INDEX sp_index ON Location;      
