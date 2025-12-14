# Spatially Balanced Latin Square (SBLS)

Project for the "Introduction to Constraint Programming" course at Université Côte d'Azur.

Information about the project can be found in `report/Rapport.pdf`.

## Author

Daniel Carriba Nosrati

## Requirements

- Java is required <br>
  Java version 21 is highly recommended, as the project was build and tested with Java 21

- Gradle Wrapper is included, no separate Gradle installation is required.

## Clone the repository

```bash
git clone https://github.com/dcarriba/CARRIBA_NOSRATI_SBLS.git
```

## Build and Run Instructions

Navigate into the project directory:

```bash
cd CARRIBA_NOSRATI_SBLS
```

### Important notice

Make sure the `gradlew` file has the rights to be executed on your machine.

If you are on Windows using CMD or PowerShell, you may use `gradlew.bat` instead of `./gradlew` for all following commands.

### Build the project

To build the project, use the following command:

```bash
./gradlew build
``` 

### Run the project

To run the project, i.e. to solve the SBLS problem for n, use the following command:

```bash
./gradlew run --args='-n <N> -printSolution <BOOLEAN>'
```

with `<N>` the wanted value for n and `<BOOLEAN>` the boolean value whether the solution should be printed or not.

Example with n equals 6 and where the solution should be printed:

```bash
./gradlew run --args='-n 6 -printSolution true'
```

### Run unit tests

```bash
./gradlew test
```
