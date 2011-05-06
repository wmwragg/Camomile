Camomile
========

## The idea

The idea behind Camomile came about from the frustration I have when I discover a funky new language, or get into a more established language, and discover there are no database bindings. Sure you can use databases like CouchDB, but if you want to use a database you already have, you would have to port it to the new database, with all the nightmares that data migration brings. What I wanted to be able to do was just hook the new language up to my database and code away.

Playing with CouchDB made me realise what a wonderful interface to a database it had. There were no database bindings required, just an ability to call it's RESTful web services with normal HTTP. Pretty much all languages have an HTTP layer somewhere, so it got me thinking, could I bring a RESTful interface to any database?

I soon realised that the answer was Java and JDBC. The two crown jewels of Java are it's JVM and the JDBC interface. With just a JDBC jar file and some configuration, you can just connect to a database, and virtually every database has a JDBC jar.

So the idea behind Camomile is that you just have a JDBC jar which you plop in a directory, and a simple configuration file for setting up the connection, and you can then just connect to the database using a RESTful API, and it will return results back as JSON text.

## What it will look like

The idea is to have a simple directory which you just copy to your hard drive, no installation, as long as you have Java 1.5 or later installed. The directory structure will look something like this:

	camomile/
		camomile
  	camomile.bat
		connections.json
		connectors/
		server/

You put the JDBC jars in the connectors/ directory, and add the connection configuration to the connections.json file using JSON syntax:

	{
		"<connection name>" : {
			"driver" : "<JDBC driver class>",
			"url" : "<JDBC connection URL>",
			"user" : "<Database user>",
			"password" : "<Database user's password>",
			"allow sql" : "<Allow raw SQL queries true/false>"
		}
	}

e.g. for MySQL test database

	{
		"test" : {
			"driver" : "com.mysql.jdbc.Driver",
			"url" : "jdbc:mysql://localhost:3306/test",
			"user" : "root",
			"password" : "",
			"allow sql" : "true"
		}
	}

Then just start up Camomile using the script:

	camomile 8080

This will start camomile on port 8080. You can then access the database via the RESTful camomile interface e.g. via curl bring back the top 20 rows from the info table:

    curl -X GET -H "Content-Type: application/json" http://localhost:8080/sql/test/20 -d'{"SQL":"select * from info"}'

Queries (SQL select statements) are done through GET while updates (SQL none select statements) are done through POST:

    curl -X POST -H "Content-Type: application/json" http://localhost:8080/sql/test -d"{\"SQL\":\"insert into info \(name\) values \('info text'\)\"}"

The default style for the return JSON will be compact, if you want pretty print JSON returned, use the query parameter "json=pretty" e.g.

    curl -X GET -H "Content-Type: application/json" "http://localhost:8080/sql/test/20?json=pretty" -d'{"SQL":"select * from info"}'

There will be a full noSQL RESTful Relational Mapping (RRM) API, which I have yet to finalise, but it will be something like ActiveResources API.

## What is the current state of Camomile

* Camomile is in early alpha stage. It has the RESTful SQL interface completed, but needs full testing and tweaking, especially the database types to JSON conversions.
* It comes in one directory, but this is just essentially a Jetty install, with a wrapper.
* There is no RRM yet, I still have to work out what the API for this is going to look like.
* There is no auto compile at the moment, this is all manual until I work out how to use ANT/Mavern to do it. I have compiled the code in the repository already, so you only have to download Camomile and run it, no compiling needed if you just want to use it. If you are on a Unix style system e.g. Mac OSX or Linux, then you can use the compile-cammomile script, to compile camomile.
