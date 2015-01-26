#!/bin/bash
# Parses the ebay xml data and loads it into the CS144 mysql database.  


# Build and run parser
ant run

# Create tables
mysql CS144 < create.sql

# Load data into tables
mysql CS144 < load.sql

# Done, delete tmp files
rm #tmpfilespathhere
