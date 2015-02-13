#!/bin/bash

# zip project 2 stuff
cd xml_parsing
zip project2.zip build.xml create.sql drop.sql load.sql queries.sql runLoad.sh README.txt src/ lib/ 
cd ..

# zip searcher 
cd searcher
zip project3-searcher.zip build.xml README.txt src/ lib/ WebContents/META-INF/services.xml 
cd ..

# zip indexer 
cd indexer
zip project3-indexer.zip buildSQLIndex.sql dropSQLIndex.sql build.xml README.txt src/ lib/
cd .. 
