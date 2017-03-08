# Project Title

This is an example MicroService that will reverse geo-locate GPS coordinates to an actual address.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```
Java 1.7+
Gradle
```

### Installing

A step by step series of examples that tell you have to get a development env running

First you will need to build and run the tests..

```
gradle build
```

And if that's is successful, you can simply run by executing this command:

```
gradle bootRun
```

Now you should be able to hit the services using a browser...
There are 2 endpoints:

1) Get an address from a set of coordinates:

```
http://localhost:8080/lookup?latlong=40.714224,-73.961452
```

2) View the cached set of coordinates:

```
http://localhost:8080/cache
```

## Running the tests

For this microService I've opted for simple integration tests.
They can be run by executing:

```
gradle test
```

## Deployment

Being a SpringBoot app you can deploy this in PCF or similar VM based containers.

## Built With

* Spring Boot - The web framework used
* Gradle - Dependency Management

## Authors

* **Kamlesh Gokal** - *Initial work* - [KamleshG](https://github.com/kamleshg)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to Matt Tucker for guidance on capping a Map for caching purposes....