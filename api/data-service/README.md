# Data Service

Collection of APIs to return coin information from the datasource (MongoDB atm).

## Instructions

These instructions will get the service up and running on your environment. 
Currently there is only instructions to setup on windows. 

### Windows

### Prerequisites

1. Starting up Mongo 
	1. Go to Services
	2. Right click 'MongoDB Server' and start if the status is not currently running
	3. Test to see if the service is running with the following commands:

```
"C:\Program Files\MongoDB\Server\4.0\bin\mongo.exe"

show dbs
```

2. Data Load Job needs to run otherwise no new data will be ingested
	1. Go to the project at {Project Home}/jobs/ExchangeDataLoad and follow the instructions

### Running the service
1. Start a bash terminal at the root of the service

Locally
```
mvn spring-boot:run -Dspring.profiles.active=local
```

## APIs

Get all the coin information
```
http://localhost:8070/coin/
```

Get coin information for a certain coin
```
http://localhost:8070/coin/{name}
```

Check service health
```
http://localhost:8070/health
```