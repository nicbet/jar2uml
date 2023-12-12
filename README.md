# Jar2UML
A re-documentation tool that describes the classes in a given JAR file as a UML (PlantUML) class diagram.

## Building
Uses `gradle`. For MacOSX, you can install `gradle` via homebrew: `brew install gradle`.

Building the tool is straightforward:

```
gradle distZip
```

You will find a distributable zip archive under `build/distributions`.

## Running

Once you have built the distributable zip, unpack it and run as follows:

```
bin/jar2uml <INPUT>
```

with `<INPUT>` being any jar file that you want to document. The tool will output to `stdout` the PlantUML code, so you might want to redirect `bin/jar2uml <INPUT> > mydiagram.plantuml`.

## Sample Output

The following output was generated from a run of `bin/jar2uml lib/jar2uml-0.0.1.jar`:

![sample output](doc/output.png)
