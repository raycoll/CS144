#!/bin/bash
# Parses the ebay xml data and loads it into the CS144 mysql database.  
# Drop old tables
mysql CS144 < drop.sql

# Build and run parser
ant clean
ant run-all

# Create tables
mysql CS144 < create.sql

# Load data into tables
mysql CS144 < load.sql

# Done, delete tmp files
rm *.dat
