vaadin-app
==============

Template for a simple Kotlin-DSL application that only requires a Servlet 3.0 container to run.


Workflow
========

To compile the entire project, run `./gradlew`.

To run the application, run `./gradlew appRun` and open http://localhost:8080/karibu-helloworld-application .

To produce a deployable production mode WAR:
- change productionMode to true in the servlet class configuration (located in the [MyUI.kt](src/main/kotlin/org/test/MyUI.kt) file)
- run `./gradlew`
- You will find the WAR file in `build/libs/karibu-helloworld-application.war`

Client-Side compilation
-------------------------

The generated maven project is using an automatically generated widgetset by default. 
When you add a dependency that needs client-side compilation, the maven plugin will 
automatically generate it for you. Your own client-side customisations can be added into
package "client".

Debugging client side code  @todo revisit with Gradle
  - run "mvn vaadin:run-codeserver" on a separate console while the application is running
  - activate Super Dev Mode in the debug window of the application

Developing a theme using the runtime compiler
-------------------------

When developing the theme, Vaadin can be configured to compile the SASS based
theme at runtime in the server. This way you can just modify the scss files in
your IDE and reload the browser to see changes.

To use the runtime compilation, run `./gradlew clean appRun`. Gretty will automatically
pick up changes in theme files and Vaadin will automatically compile the theme on
browser refresh. You will just have to give Gretty some time (one second) to register
the change.

When using the runtime compiler, running the application in the "run" mode 
(rather than in "debug" mode) can speed up consecutive theme compilations
significantly.

It is highly recommended to disable runtime compilation for production WAR files.
