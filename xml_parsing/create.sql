/* Creates tables for the auction site 
 *
 */

CREATE TABLE Item (
    item_id INT UNSIGNED NOT NULL PRIMARY KEY,
    name VARCHAR(100),
    buyprice DECIMAL(8,2),
    start_price DECIMAL(8,2),
    num_bids INT UNSIGNED NOT NULL,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    location VARCHAR(100),
    country VARCHAR(100),
    started TIMESTAMP,
    ends TIMESTAMP,
    seller_id INT UNSIGNED NOT NULL,
    description VARCHAR(4000)
) COLLATE utf8_bin;

CREATE TABLE Bid (
    user_id VARCHAR(100) NOT NULL,
    item_id INT UNSIGNED NOT NULL,
    time TIMESTAMP NOT NULL,
    amount DECIMAL(8,2),
    PRIMARY KEY(user_id, time)
) COLLATE utf8_bin;

CREATE TABLE AuctionUser (
    user_id VARCHAR(100) NOT NULL PRIMARY KEY,
    sell_rating INT ,
    buy_rating INT ,
    location VARCHAR(100),
    country VARCHAR(100)
) COLLATE utf8_bin;

CREATE TABLE ItemCategory (
    item_id INT UNSIGNED NOT NULL,
    category VARCHAR(100),
    PRIMARY KEY(item_id, category)
) COLLATE utf8_bin;

