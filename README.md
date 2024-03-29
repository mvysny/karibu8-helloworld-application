[![Powered By Vaadin on Kotlin](http://vaadinonkotlin.eu/iconography/vok_badge.svg)](http://vaadinonkotlin.eu)
[![Heroku](https://heroku-badge.herokuapp.com/?app=karibu-helloworld-app&style=flat&svg=1)](https://karibu-helloworld-app.herokuapp.com/)

# Karibu-DSL Example App / Archetype

Template for a simple Kotlin-DSL application that only requires a Servlet 3.0 container to run.
Just clone this repo and start experimenting!

Uses [Karibu-DSL](https://github.com/mvysny/karibu-dsl) Kotlin bindings for
the [Vaadin](https://vaadin.com) framework. For more information on Vaadin please
see the [Vaadin 8 Documentation](https://vaadin.com/docs/v8/framework/tutorial.html).
Also uses the [Vaadin Gradle plugin](https://github.com/johndevs/gradle-vaadin-plugin/wiki).

# Deprecated

Vaadin 8 is no longer maintained and has [reached the end-of-life](https://vaadin.com/vaadin-8). This example will
not be maintained anymore. Please visit [karibu-helloworld-application](https://github.com/mvysny/karibu-helloworld-application)
for the up-to-date example.

# Getting Started

To quickly start the app, make sure that you have Java 8 (or higher) JDK installed. Then, just type this into your terminal:

```bash
git clone https://github.com/mvysny/karibu-helloworld-application
cd karibu-helloworld-application
./gradlew build appRun
```

The app will be running on [http://localhost:8080/](http://localhost:8080/)

Since the build system is a Gradle file written in Kotlin, we suggest you use [Intellij IDEA](https://www.jetbrains.com/idea/download)
to edit the project files. The Community edition is enough to run the server
via Gretty's `./gradlew appRun`. The Ultimate edition will allow you to run the project in Tomcat - this is the recommended
option for a real development.

# Workflow

To compile the entire project, run `./gradlew`.

To run the application, run `./gradlew appRun` and open [http://localhost:8080/](http://localhost:8080/) .

To produce a deployable production mode WAR:
- change `productionMode` to `true` in the servlet class configuration (located in the [MyUI.kt](src/main/kotlin/org/test/MyUI.kt) file)
- run `./gradlew`
- You will find the WAR file in `build/libs/karibu-helloworld-application.war`

This will allow you to quickly start the example app and allow you to do some basic modifications.

## Client-Side compilation

The project is using a pre-compiled widgetset by default, present in the `vaadin-client-compiled.jar` file.

When you add a dependency that needs client-side compilation, the [Vaadin Gradle plugin](https://github.com/johndevs/gradle-vaadin-plugin/wiki) will 
automatically generate it for you (the `AppWidgetset.gwt.xml` file; Vaadin UI
will automatically use the `@Widgetset("AppWidgetset")` if such file exists).
Your own client-side customisations can be added into
the folder `src/main/java/client`.

### Enabling Widgetset compilation

In order to trigger the client-side compilation, do this:

1. Create an empty folder `src/main/java/client`.
2. Remove the `compile("com.vaadin:vaadin-client-compiled:${vaadin.version}")` dependency from `build.gradle.kts`
3. Run `./gradlew`. Vaadin Gradle plugin will now detect that there is the `client` folder and will
  reconfigure the project to use vaadin-client-compiler to GWT-compile the widgetset. It will also
  generate `src/main/resources/AppWidgetset.gwt.xml` which is a configuration file for the GWT compiler.
4. Running `./gradlew` again will now compile the widgetset to `src/main/webapp/VAADIN/widgetsets/AppWidgetset` and will package
  it into the WAR archive. The `AppWidgetset` is somewhat special: if `src/main/resources/AppWidgetset.gwt.xml` is present,
  Vaadin will auto-activate this widgetset as if the UI was annotated by `@Widgetset("AppWidgetset")`.
5. You can now place Java-based client-code to `src/main/java/client` and  shared code to `src/main/java/shared`. Remember -
   you have to use Java since GWT can't compile Kotlin.

### Debugging client-side

Debugging client side code with [Vaadin Gradle Plugin's superdevmode](https://github.com/johndevs/gradle-vaadin-plugin/wiki/Tasks-and-configuration-DSL#vaadinsuperdevmode):
  - run "./gradlew vaadinSuperDevMode" on a separate console while the application is running
  - browse to [http://localhost:8080/?superdevmode](http://localhost:8080/?superdevmode) to activate the superdevmode
  - Read more on the superdevmode here: [Debugging Your Widgetset Components With SuperDevMode For Dummies](https://mvysny.github.io/Debugging-your-widgetset-components-with-superdevmode-for-dummies/)

## Developing a theme using the runtime compiler

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

## Development with Intellij IDEA Ultimate

The easiest way (and the recommended way) to develop Karibu-DSL-based web applications is to use Intellij IDEA Ultimate.
It includes support for launching your project in any servlet container (Tomcat is recommended)
and allows you to debug the code, modify the code and hot-redeploy the code into the running Tomcat
instance, without having to restart Tomcat.

1. First, download Tomcat and register it into your Intellij IDEA properly: https://www.jetbrains.com/help/idea/2017.1/defining-application-servers-in-intellij-idea.html
2. Then just open this project in Intellij, simply by selecting `File / Open...` and click on the
   `build.gradle` file. When asked, select "Open as Project".
2. You can then create a launch configuration which will launch this example app in Tomcat with Intellij: just
   scroll to the end of this tutorial: https://kotlinlang.org/docs/tutorials/httpservlets.html
3. Start your newly created launch configuration in Debug mode. This way, you can modify the code
   and press `Ctrl+F9` to hot-redeploy the code. This only redeploys java code though, to
   redeploy resources just press `Ctrl+F10` and select "Update classes and resources"
   
Or watch the [Debugging Vaadin Apps With Intellij video](https://www.youtube.com/watch?v=M0Q7D03bYXc).

## Dissection of project files

Let's look at all files that this project is composed of, and what are the points where you'll add functionality:

| Files | Meaning
| ----- | -------
| [build.gradle.kts](build.gradle.kts) | [Gradle](https://gradle.org/) build tool configuration files. Gradle is used to compile your app, download all dependency jars and build a war file
| [gradlew](gradlew), [gradlew.bat](gradlew.bat), [gradle/](gradle) | Gradle runtime files, so that you can build your app from command-line simply by running `./gradlew`, without having to download and install Gradle distribution yourself.
| [.github](.github) | Configuration file for the [Github Actions](https://docs.github.com/en/free-pro-team@latest/actions/learn-github-actions). Github will watch any changes to this repo and will automatically build your app and runs all the tests after every commit.
| [.gitignore](.gitignore) | Tells [Git](https://git-scm.com/) to ignore files that can be produced from your app's sources - be it files produced by Gradle, Intellij project files etc.
| [src/main/resources/](src/main/resources) | A bunch of static files not compiled by Kotlin in any way; see below for explanation.
| [simplelogger.properties](src/main/resources/simplelogger.properties) | We're using [Slf4j](https://www.slf4j.org/) for logging and this is the configuration file for [Slf4j Simple Logger](https://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html).
| [webapp/](src/main/webapp) | static files provided as-is to the browser. See below for explanation
| [mytheme/](src/main/webapp/VAADIN/themes/mytheme) | Vaadin Theme which is generally a bunch of SCSS files compiled to one large CSS. Read more at [Creating and Using Themes](https://vaadin.com/docs/v8/framework/themes/themes-creating.html)
| [src/main/kotlin/](src/main/kotlin) | The main Kotlin sources of your web app. You'll be mostly editing files located in this folder.
| [MyUI.kt](src/main/kotlin/org/test/MyUI.kt) | When Servlet Container (such as [Tomcat](http://tomcat.apache.org/)) starts your app, it will show the components attached to the main `UI` class, or in this case, the `MyUI` class. The `MyUIServlet` defines which UI to use and where to map the application to.
| [MyUITest.kt](src/test/kotlin/org/test/MyUITest.kt) | Automatically run by Gradle to test your UI; see [Karibu Testing](https://github.com/mvysny/karibu-testing) for more information.

# More Resources

* The DSL technique is used to allow you to nest your components in a structured code. This is provided by the
  Karibu-DSL library; please visit the [Karibu-DSL home page](https://github.com/mvysny/karibu-dsl) for more information.
* The browserless testing is demonstrated in the [MyUITest.kt](src/test/kotlin/org/test/MyUITest.kt) file.
  Please read [Browserless Web Testing](https://github.com/mvysny/karibu-testing) for more information.
* For more complex example which includes multiple pages, please see the [Karibu-DSL example-v8 app](https://github.com/mvysny/karibu-dsl#quickstart).
* For information on how to connect the UI to the database backend please visit [Vaadin-on-Kotlin](http://www.vaadinonkotlin.eu/)
  You can find a complete CRUD example at [Vaadin-on-Kotlin vok-example-crud-sql2o](https://github.com/mvysny/vaadin-on-kotlin#example-project).
