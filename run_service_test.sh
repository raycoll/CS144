#!/bin/bash

# zip submissions
./create_submission.sh

# Load mysql
cd xml_parsing
./runLoad.sh project2.zip
cd ..

# Create index
cd indexer
./index_test project3-indexer.zip
cd ..

# Run service test 
cd searcher
./service_test project3-searcher.zip
cd ..
