/* A collection of queries to test the parsed xml data */

/*
Find the number of users in the database.
*/
SELECT count(*)
FROM AuctionUser;

/*
 * Find the number of items in "New York", (i.e., items whose location is exactly the string "New York"). 
 * Pay special attention to case sensitivity. You should match the items in "New York" but not in "new york".
*/

/*
 * Find the number of auctions belonging to exactly four categories.
 */
SELECT 
FROM ItemCategory
GROUP BY Item_ID
HAVING COUNT(*)=4;
/*
 * Find the ID(s) of current (unsold) auction(s) with the highest bid. 
 * Remember that the data was captured at the point in time December 20th, 2001, one second after midnight, 
 * so you can use this time point to decide which auction(s) are current. 
 * Pay special attention to the current auctions without any bid.
*/
 
/*
 * Find the number of sellers whose rating is higher than 1000.
 */
SELECT count(*)
FROM AuctionUser
WHERE Sell_Rating > "1000";
/*
 * Find the number of users who are both sellers and bidders.
 */
SELECT count(*)
FROM AuctionUser
WHERE Sell_Rating != NULL AND Buy_Rating != NULL;
/*
 * Find the number of categories that include at least one item with a bid of more than $100.
 */

