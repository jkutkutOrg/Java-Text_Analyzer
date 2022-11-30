# Text Analyzer:

Program to analyze different properties of a text file.

## Java version:
Amazon Corretto 18.0.2

## Jar generation:
### analyzer.jar:
- Just make the jar file with the following file: ``Analyzer.java``
### text_analyzer.jar:
- Make the jar file with the following files:
  - ``Analyzer.java``
  - ``Main.java``
  - ``SuperScanner.java``

## How to run:
### analyzer.jar:
- Run the jar file with the following command:

```
java -cp jars/analyzer.jar com.jkutkutorg.textAnalyzer.Analyzer <ARGUMENTS>
```

### text_analyzer.jar:
- Run the jar file with the following command:

```
java -cp jars/text_analyzer.jar com.jkutkutorg.textAnalyzer.Main <ARGUMENT>
```

## Bonuses:
- Multiple modes:
  - Vowel count.
  - Consonant count.
  - Letter count.
  - Number count.
- General child logic.
  - All child processes are created from the same JAR file.
  - Each one can be run and configured independently.
- Use of Lambda expressions to generalize the logic of the analyzer.
- Analyzer able to work both as a child process and as a standalone script.
- Analyzer can send result to stdout or to a file.
- Data validation of all inputs given by the user.

TODO

- Docker environment to run the program.