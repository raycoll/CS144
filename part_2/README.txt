Steven Collison
Synthia Ling

1.
Users(User_ID, 
      Sell_Rating,
      Buy_Rating,
      Location,
      Country,
      PRIMARY KEY(User_ID)
     )
Bids(User_ID, 
     Item_ID,
     Time,
     Amount,
     PRIMARY KEY(User_ID, Item_ID, Time)
    )
Items(Item_ID,
      Name,
      Buy_Price,
      Start_Price,
      Number_of_Bids,
      Long,
      Lat,
      Location,
      Country,
      Started,
      Ends,
      User_ID,
      Description
      PRIMARY KEY(Item_ID)
    )
Categories(Item_ID,
           Category,
           PRIMARY KEY(Item_ID, Category)
          )

2. User_ID -> Sell_Rating, Buy_Rating, Location, Country
User_ID, Item_ID, Time -> Amount
Item_ID -> Name, Buy_Price, Start_Price, Number_of_Bids, Long, Lat, Location, Country, Started, Ends, User_ID, Description

3. Yes our relations are in BCNF

4. Yes our relations are in 4NF
