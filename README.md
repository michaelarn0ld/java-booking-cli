# Don't Wreck My House!
Don't wreck my house is a TUI application that serves as a travel reservation
system. The app is designed to be used by an administrator to create, view, 
modify, and delete for guest/host pairs. The file uses file I/O as a data store;
the core purpose of this project was to implement a simplified layered
application complemented with the MVC design pattern.

Generics were also explored in this project, specifically in the implementation
of Host and Guest models (and their subsequent repositories and services). This
exercise in abstraction was possible due to the crossover in state and
functionality between Host and Guest, or more generically, Client types.

## Technologies
- Java
- Maven
- Spring
- JUnit 5

## Project Structure
Caveats
* ```/data/reservations/*``` contains a collection of files such that each file
catalogs all reservations associated with a specific host; file names are
unique an of the format {host_id}.csv. While this is an inconvenient way to 
store data, it was a firm constraint of the project specification.
```
.
├── data
│   ├── guests.csv
│   ├── hosts.csv
│   ├── reservations
│   │   └── ...
│   └── ...
│
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── michaelarn0ld
    │   │       └── mastery
    │   │           ├── App.java
    │   │           ├── data
    │   │           │   ├── abstractions
    │   │           │   │   ├── ClientFileRepository.java
    │   │           │   │   └── FileRepository.java
    │   │           │   ├── contracts
    │   │           │   │   ├── ClientRepository.java
    │   │           │   │   └── ReservationRepository.java
    │   │           │   ├── exceptions
    │   │           │   │   └── DataException.java
    │   │           │   ├── GuestFileRepository.java
    │   │           │   ├── HostFileRepository.java
    │   │           │   └── ReservationFileRepository.java
    │   │           ├── domain
    │   │           │   ├── ClientService.java
    │   │           │   ├── ReservationService.java
    │   │           │   └── Result.java
    │   │           ├── models
    │   │           │   ├── Client.java
    │   │           │   ├── Guest.java
    │   │           │   ├── Host.java
    │   │           │   ├── Reservation.java
    │   │           │   └── State.java
    │   │           └── ui
    │   │               ├── ConsoleIO.java
    │   │               ├── Controller.java
    │   │               └── View.java
    │   └── resources
    │       └── app-config.xml
    └── test
        └── java
            └── michaelarn0ld
                └── mastery
                    ├── data
                    │   ├── GuestFileRepositoryTest.java
                    │   ├── GuestRepositoryDouble.java
                    │   ├── HostFileRepositoryTest.java
                    │   ├── HostRepositoryDouble.java
                    │   ├── ReservationFileRepositoryTest.java
                    │   └── ReservationRepositoryDouble.java
                    ├── domain
                    │   ├── ClientServiceTest.java
                    │   └── ReservationServiceTest.java
                    └── models
                        └── ReservationTest.java
```

## Contributors
@author: Michael Arnold \
@contact: me@michaelarnold.io

## License
Copyright © 2021 Michael Arnold

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
