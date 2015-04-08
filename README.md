# Vertx3 chat start application
A vertx3 application used during workshops as a starting point. Contains vertx-mongodb-service, AngularJS and vertx3.

# Usage
The application needs mongodb running on the same host.
Clone the repository and use mvn install to build the application. Use java-jar target\vertx3-chat-start-0.0.1-SNAPSHOT-fat.jar to run in standalone mode 
and mvn -jar target\vertx3-chat-0.0.1-SNAPSHOT-fat.jar -cluster -cluster-host <ip-address> to run in clustered mode.
Point you browser to http://localhost:8080/ and Hello World will be presented. The next step is to start implementing the chat application.
