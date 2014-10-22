RJMBYOB
=======

Simple ephimeral database 

The MIT License (MIT)

Copyright (c) 2014

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.


=== WHAT IS THIS? ===
A simple Ephimeral "Database" built using Spring3+Jersey. It allows REST calls to be made to get the data inside services.
Really isn't much. It's a lazyman take on : https://github.com/RJMetrics/byodb

=== Software Used ==
Spring 3
Jersey 2
Google gson
Maven



== HOW TO BUILD ==
If you are lazy...drop the .war file into a tomcat instance and navigate to {tomcat localhost}:{port}/RjMetricsDB/api/database/{API CALLS}

If you want to dig in... 

run mvn install (this is a maven project after all...)

if you are really a glutton for punishment, grab Eclipse EE and import the project, and run > mvn install.



 == API CALLS ==
 Using any language you want, build a simple database that exposes the following HTTP REST interface:
 - `GET    /tables` - List tables
 - `GET    /tables/:table` - Retrieve the entire contents of `:table`
 - `GET    /tables/:table/:key` - Retrieve the contents of `:key` in `:table`
 - `POST   /tables/:table` - Expects a JSON map in the request body. Merges the contents of that map into `:table`, overwriting any existing keys.
 - `PUT    /tables/:table/:key` - Expects a JSON document in the request body. Sets the value of `:key` in `:table` to that document, overwriting any existing value.
 - `DELETE /tables/:table/:key` - Disassociates any value with `:key` in `:table`
 - `DELETE /tables/:table` - Empties all keys and values from `:table`
 - `DELETE /tables` - Empties all tables
 - `GET    /search?q=:query` - Returns all keys that match the regular expression `:query`
