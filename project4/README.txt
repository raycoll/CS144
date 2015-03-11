Team: Stevia
Steven Collison
Synthia Ling


Q1. We used HTTPS for the submission on the credit card entry page. 
    We also used HTTPS for transmitting the confirmation page back to the user 
    after the server receives submission.
    In the diagram, we encrypt (4)->(5) and (5)->(6).
    

Q2. We ensure the item is purchased at exactly its Buy_Price by using HttpSession. 
    We never allow the user to enter the Buy_Price as a parameter at any point.
    The Item_Id, Buy_Price, and other item data is set for the session every time a user
    visits an item information page. 
    When we retrieve data to generate the credit card entry page and confirmation page, 
    we use the session data that was set when visiting the item page. 
    Thus, a user can't modify an item's Buy_Price because the session manages all the data and 
    that data is only set on a vistit to an item's page.  
