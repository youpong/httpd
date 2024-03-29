[![Test](https://github.com/youpong/httpd/actions/workflows/test.yml/badge.svg)](https://github.com/youpong/httpd/actions/workflows/test.yml)

Web Server
==========

A multi-threaded HTTP 1.1 Server implemented in Java.

This software is released under MIT license.


RUNNING
=======

The Web Server runs on any OS with Java (JRE) 1.8+ installed.

To start the server, run the following command:
(The parameter service is optional, default port is 80)

```bash
$ java -jar httpd-0.1.jar [service]
```

To stop the server, just press Ctrl+C on the command line.


BUILD
=====

Define The JAVA_HOME environment variable.

Run the following command:

```bash
$ mvn package
```

This will create a "target" folder containing the application jar file:
httpd-<version>.jar
