/* Creates tables for the auction site 
 *
 */

CREATE TABLE Item (
    itemid INT UNSIGNED NOT NULL PRIMARY KEY,
    name VARCHAR(100),
    buyprice DECIMAL(8,2),
    start_price DECIMAL(8,2),
    num_bids INT UNSIGNED NOT NULL,
    longitude DECIMAL(9,6),
    latitude DECIMAL(9,6),
    location VARCHAR(100),
    country VARCHAR(100),
    started TIMESTAMP,
    ends TIMESTAMP,
    seller_id INT UNSIGNED NOT NULL,
    description VARCHAR(4000)
) ;

CREATE TABLE Bid (
    user_id INT UNSIGNED NOT NULL,
    item_id INT UNSIGNED NOT NULL,
    time TIMESTAMP,
    amount DECIMAL(8,2),
    PRIMARY KEY(user_id, item_id, time)
) ;

CREATE TABLE AuctionUser (
    user_id INT UNSIGNED NOT NULL PRIMARY KEY,
    sell_rating INT ,
    buy_rating INT ,
    location VARCHAR(100),
    country VARCHAR(100)
) ;

CREATE TABLE ItemCategory (
    item_id INT UNSIGNED NOT NULL,
    category VARCHAR(100),
    PRIMARY KEY(item_id, category)
) ;

