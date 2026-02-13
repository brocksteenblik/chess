# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Server API Diagram Link
[link](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+iMykoKp+h-Ds0KPMB4lUEiMAIEJ4oYoJwkEkSYCkm+hi7jS+4MkyU76XOnl3kuwpihKboynKZbvEqmAqsGGo3gFDpJuUbm1D2fbbu5FnPAAkmgVAmkgHBupyUYxnGhSgclom1OmACMwwwNm-J5vM0FFiW9Q+NMl7QEgABeKC7HRTbDvyyWpWFW5uRUqUFcgWjok0RnqRiak7GsU3yK5zooBUY30oecgoM+8Tnpe14HaoFTLgGa4Bhd027clOl1A54oZKoAGYK9IHVLp+lrBRVHoJp-3wsmyCpjAuFODAjV6URBnGpeINoCNDGeN4fj+F4KDoDEcSJHjBMOb4WCiYKoH1A00gRvxEbtBG3Q9HJqgKcMwOIeg6HwpUr0vFzSGmbCGEWVZNn2OT9lCeTTlqC5mWCld3lgKdPLnfB3NoP5vKBZUy4hU+j3yLK8pCzzsXqjACV60ls27Wlvb9m5f0SXUeXzUVp1lSgsYKbz1VQ9h9UI2MOaqK1BZjB10BdT1ep9YNw2NgxV1u5ZLoPS+T2dsrt78vUHAoNwx6XrbC4CkF9TSCXTKGBblW7ZhwdgDheGp4x2MBCi67+Ng4oavxaIwAA4kqGiU9lpYNKPjMs-YSqc6j2u89pZn+o3P0b3zHaZ12MAooSCvjzmw85JrlHazteceXb9KMmrl6X2juuV-ewowEb2dXtoZso1rJCMVVTxXThNR26UXbN2SnvXK+VCocB9tGP2FVA4VBqqHNATVw4tXzO1YsccFQJ3iEnIaDZ6IV33BnJE6tc773zolekyAcinzUBiN+VDq5jygLoGAepDCLzmDAcUAAzaAxxUFgPFi6QRahz5YFdjAgWMBZGqHkdvUWENfTgxeCoieNMRiyJytINYmx4i6hQCVJUaxkigDVFYlSei5gADklQXG6GDd2LcShtxhnheGWCRhOLUAYoxJjtTmMnEjGxCA7EOORrI1xcx3EwAxl3Zi-gOAAHY3BOBQHDfwEZghwC4gANngBOQwrCbZT20e7GmrQOgLyXsQtGWZEluLXh+He9QYAAKvkhAJhilRJJQCLL8VNHYwDQCgTYrCX7azWB0uYN96F30rqrU6CykIcP1kKUU4pjY51NhFLeVtQEF0XA7TsTsMqKOnrUT2CCkHlQDlVdBrdapOAaoEnBuY8GFgIaWbqFFSEpwoVIup+96i0PkErdZXkYBHXRPM5ZXJKF7NunASp0zZljyVDyYZLjOkzW6ZohkSpjEaK-OA+p2CwnRzQu88oNU-H4VGMsBl9Q6rhGCIEVJncsYZMsCXGymxCZIASGAEVfYIDioAFIQHFPioR-hbEgDVEUVuky6XNGZDJHosjl6APQFmbAsSRVQDgBAGyUAlmUukJ4xM-MemNzNRayg1rbX2rmMY8Z5koVWQAFZKrQPMxuqkPVWptdAH1KBjGrMHFSS5o5H5bMbrspKBtP7f1hcAf+ZyQE20halSB8KHlPIWi8lBbzwZBx8V8n52CI5R3wZ1IhoKoADTIWkyFe8aEm2APC5NjDRx+EWigeZgMYDmuAJar1sbgkJoxVm-ZCpsATqamEi4wi0BiOAXFYtKafSpSJfGx1RgIBqGmRAdEMArQj2VcYctULnhXV9v7eMzKMHfLDi2gFMcgXx07d28FTZ7mVFZbDQVTEcZeDnRKqV8H5SIGDLAYA2BzWEDyAUGp2qHmNDpgzJmLNn1aTJV+Wo1KA39qzsXUuKBpAACF2HDpkCm+oIBuB4FY2A7NtQZSPwFFxtDQ02M1WgxQoAA)
