# RBScanner
RBScanner or "Resource Bundle Scanner" is a tool used to scan an app for property files usages. 
Intended to be used mostly for resource bundle files, it also has an option of scanning resource bundle files with multiple locales. 
After scanning the user may generate a new trimmed ".properties" files based on the inputted files which excludes unused properties.


### Requires:
* #### To run the jar file:
   - Java 8 JRE
* #### To build from source:
   - Java 8 JDK
   - Maven


# Warning
As of the moment the tests haven't been fixed yet. When attempting to build it please run 
* mvn package -DskipTests
* mvn package -DskipTests=true 
or
* mvn package -Dmaven.test.skip=true 