Installation of the Evaluation Tool
===================================

1 Installing IBM WebSphere Liberty Profile
------------------------------------------
The application has been deployed on a WebSphere Liberty Profile Server. Nevertheless, the implementation is compatible to
the JavaEE 6.0 standard and should be runnable on all major application servers.

The following steps briefly describe how to setup the server with the evaluation application.

1.1 Download WebSphere Liberty Profile
The WebSphere Liberty Profile Server can be downloaded from this website:
https://developer.ibm.com/wasdev/downloads/

The version used for deploying the application is 8.5.5.8.

1.2 Creating a new server
The server is delivered with configuration tools. They can be used to create a new server instance. The configuration files
are provided in the directory wlp-conf. The server.xml file contains some placeholder values for secret information that has
to be replaced to be able to start the server.

Probably, some server modules that are required by the application have to be downloaded with the feature manager.

1.3 Install MySQL driver
A MySQL JDBC driver has to be downloaded from the MySQL homepage:
https://dev.mysql.com/downloads/connector/

2 Setting up the database
-------------------------
The directory database/setup contains the scripts needed to setup the database for the evaluation process.
  tables.sql    contains all tables that store the actual evaluation data
  eval-viws.sql contains the views that calculate the results of the user study

The directory database/dump contains the state of our database after finishing the user study.

3 Run the server
----------------
After going through these steps, the application nlp-evaluation-ear.ear can be deployed on the server and should be able to run.