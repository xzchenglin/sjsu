## Overview

- Utilized **Spring Boot** to construct a plain wallet and transfer application..
 - Certain operations strictly adhere to **Strong Consistency** principles and apply transactions.
 - Given the necessity for historical data, adopted the **Event Sourcing** concept to playback from snapshots.
 - Use **H2** for POC purpose.
 - The following represents the state machine for Account:
![Account State](https://github.com/xzchenglin/sjsu/assets/7553030/5270d9eb-84a0-4ad2-bb2a-3e8615fb1cd4)

- The following represents the state machine for Movement:
![MovementState](https://github.com/xzchenglin/sjsu/assets/7553030/21d4bcff-15f9-40df-9830-7e1ff14de250)

 - For some operations(make transfers and get lastest wallet state), used **CQRS** concept, the sequence diagram is illustrated below:
![Seq](https://github.com/xzchenglin/sjsu/assets/7553030/fdea9b6a-2f92-4c3f-b9a7-f79791f29f74)



## Setup and Run

It's fairly straight forward:

 - Pre-requirments: Maven, JDK 21
 - Build and run Integration tests: *mvn clean install* OR Open from InteljiJ IDEA, build and run *LedgerApplicationTests*
 - If want to start as service: *mvn spring-boot:run (There aren't many REST APIs exposed, the primary functionalities are orchestrated through the tests mentioned above.)



## TODO

Owing to various constraints, some elements are lacking in this version:

 - **MQ** part is simply simulated and not actaully introduced, performance can be boosted with the real asynchronous approach especailly in the CQRS and Event Sourcing part.
 - **Database** can be setup separatly for read/write seperation.
 - **UTs** are not thoroughly developed (strictly speaking only integration tests are implemented for demo purpose).
 - **Authentication/Logs/JavaDoc** are absent.
 
