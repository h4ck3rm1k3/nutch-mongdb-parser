NUTCH-MONGODB-INDEXER README

Compatible With:
   Nutch 1.3

For the latest information about Nutch, please visit the website at:

   http://nutch.apache.org

our the wiki, at:

   http://wiki.apache.org/nutch/

To get started using Nutch read Tutorial:

   http://wiki.apache.org/nutch/NutchTutorial

For the latest information about Mongodb, please visit the website at:

   http://www.mongodb.org/

our the wiki, at:

   http://www.mongodb.org/display/DOCS/Home

To get started using Mongodb and Java read these Tutorials

   http://www.mongodb.org/display/DOCS/Quickstart
   http://www.mongodb.org/display/DOCS/Java+Tutorial

Important Stuff

This patch was created to allow for the easy seeding of urls from Mongodb.  This is similar in nature to that of the DmozParser that comes with Nutch.  This provides a way to bootstrap and seed Nutch with data coming directly from Mongodb.  The injector add urls from a specified mongodb to the crawldb of your choice.  

This is just the code necessary to create the solution.  You must start by having the Nutch codebase and have it setup in your development environment (Eclipse) see http://wiki.apache.org/nutch/RunNutchInEclipse for how do this.  Once you are set up and is working well.  You are ready to get started.  The following files below are necessary to integrate into the notch base and then re-build notch

Folder Structure
----> java/org/apache/nutch/tools/MongodbParser.java
----> ivy/ivy.xml

Step 1.  Add the the MongodbParser.java to the following package java.org.apache.nutch.tools

Step 2. Open the ivy.xml and add the mongodb java driver dependency to the existing ivy/ivy.xml file.

Step 3. Rebuild and you should be ready to test

Step 4. To test you can run the following commands from terminal

----> make sure that you have created a "mongodb" directory on the root.  This will be where the urls will be stored

----> bin/nutch org.apache.nutch.tools.MongodbParser localhost -database urls -collection content -field url -limit 100 -location mongodb/urls
To see the available parameters

localhost - is the location of your mongodb database (used to connect)
-database - is the name of the database you want to connect to
-collection - is the name of the collection you want to connect to
-field - is the name of the "url" field you are pulling from
-limit - is if you want to limit your query
-location - is the location where you want to store the seed list file

----> bin/nutch org.apache.nutch.tools.MongodbParser

To seed the crawled run the following

----> bin/nutch org.apache.nutch.crawl.Crawl inject crawldb mongodb 
