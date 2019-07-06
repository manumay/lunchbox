# Lunchbox 360

Jetty web application for ordering lunch from a deliverer. Supports the following features:
* listing of available meals on a daily basis
* import of meals from the web (PDF-file)
* users can select their desired meal or change/remove their previous selection
* at a specific point in time orders are locked and sent to the deliverer via e-mail
* monthly billing, which cann automatically be sent to the administration office
* several other statistics and reports
* administration user interface

Compile the application using Maven:
```
mvn clean install
```
  
Run the web application locally using Maven:
```
cd lunchbox-webapp/
mvn jetty:start
```
