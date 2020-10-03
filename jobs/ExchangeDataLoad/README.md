# Exchange Data Load

Multiple quartz jobs which consume data from coin exchanges, transforms them to a common coin schema and saves them to MongoDB. 

## Instructions

These instructions will get the job up and running on your environment. 
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

### Running the job
1. Start a bash terminal at the root of the service

Locally
```
mvn spring-boot:run -Dspring.profiles.active=local
```

## Exchanges
Currently the majority of data is collected from CoinRanking with the following support exchanges:

* Okex
* Bitfinex
* Binance
* CoinBene

The coin data is aggregated to store a summarised view. In the future state I hope to move dependence away from CoinRanking and add more exchanges. 
