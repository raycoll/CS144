/*SET profiling = 1;*/
/* A collection of queries to test the parsed xml data */

/*
Find the number of users in the database.
*/
SELECT COUNT(*)
FROM AuctionUser;

/*
 * Find the number of items in "New York", (i.e., items whose location is exactly the string "New York"). 
 * Pay special attention to case sensitivity. You should match the items in "New York" but not in "new york".
*/
SELECT COUNT(*)
FROM Item
WHERE location='New York';

/*
 * Find the number of auctions belonging to exactly four categories.
 */

SELECT COUNT(*)
FROM(
SELECT DISTINCT item_id
FROM ItemCategory
GROUP BY item_id
HAVING COUNT(*) = 4
) as dumb_table;

/*
 * Find the ID(s) of current (unsold) auction(s) with the highest bid. 
 * Remember that the data was captured at the point in time December 20th, 2001, one second after midnight, 
 * so you can use this time point to decide which auction(s) are current. 
 * Pay special attention to the current auctions without any bid.
*/
/*
SELECT b.item_id
FROM (SELECT item_id
FROM Item
WHERE Ends > '2001-12-20 00:00:01') as current_items
INNER JOIN Bid b
ON (current_items.item_id=b.item_id)
ORDER BY b.amount DESC
LIMIT 1;
*/
SELECT item_id
FROM (SELECT b.item_id, MAX(amount)                                                                                                           
	  FROM (SELECT item_id                                                                                                                        
			FROM Item                                                                                                                                   
			WHERE Ends > '2001-12-20 00:00:01') as current_items                                                                                        
			INNER JOIN Bid b                                                                                                                            
			ON (current_items.item_id=b.item_id)) as newtable;

/*
 * Find the number of sellers whose rating is higher than 1000.
 */
SELECT COUNT(*)
FROM AuctionUser
WHERE sell_rating > 1000;


/*
 * Find the number of users who are both sellers and bidders.
 */
SELECT COUNT(*)
FROM AuctionUser
WHERE sell_rating IS NOT NULL AND buy_rating IS NOT NULL;

/*
 * Find the number of categories that include at least one item with a bid of more than $100.
 */

SELECT COUNT(DISTINCT(ic.category))
FROM (SELECT item_id FROM Bid WHERE amount > 100) as bi,  ItemCategory ic
WHERE ic.item_id=bi.item_id;

/*SHOW PROFILES;*/
