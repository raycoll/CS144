Steven Collison
Synthia Ling

1.
AuctionUser(User_ID, 
      Sell_Rating,
      Buy_Rating,
      Location,
      Country,
      PRIMARY KEY(User_ID)
     )
Bid(User_ID, 
     Item_ID,
     Time,
     Amount,
     PRIMARY KEY(User_ID, Item_ID, Time)
    )
Item(Item_ID,
      Name,
      Buy_Price,
      Start_Price,
      Number_of_Bids,
      Lat,
      Long,
      Location,
      Country,
      Started,
      Ends,
      User_ID,
      Description
      PRIMARY KEY(Item_ID)
    )
ItemCategory(Item_ID,
           Category,
           PRIMARY KEY(Item_ID, Category)
          )

2. User_ID -> Sell_Rating, Buy_Rating, Location, Country
User_ID, Item_ID, Time -> Amount
Item_ID -> Name, Buy_Price, Start_Price, Number_of_Bids, Lat, Long, Location, Country, Started, Ends, User_ID, Description

3. Yes our relations are in BCNF

4. Yes our relations are in 4NF
