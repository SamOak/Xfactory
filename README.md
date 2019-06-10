# Xfactory 
## XML, XSL, XPATH Toolbox and batchprocessing

Xfactory is a free software which I wrote just to play around with spring-boot and ZK framework.

Xfactory feature:
 * flexible and high performat xsl batch tranformation
 * XSLT Workbench
 * XSD Valitdator
 * XPath Workbench
 * XML Beautifier

I started that project just for fun ... let's say a kind of 'finger excerice'. And it becomes more complex than planned.
I think (or hope?) someone out there find this piece of code helpful - either how to use ZK in a spring-boot project or using the application itself.

So...
* If you think Xfactory is useful for you, please feel free to use it. And if you do so - may drop
    me a mail. I am happy when it is used.
* If you think there is a function missing: let me know.
* If you think some stuff could be implemented in a better way: please let me know.

Overall: Cut me some slack :-)

I will continue to work on this project to improve it more and more. If you want to support me with a donation, I would be very pleased.
[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=TVD2RFNP5N5UL)


You'll find the current release here: 
[Xfactory-0.0.3.jar](https://github.com/SamOak/Xfactory/releases/download/0.0.3/Xfactory-0.0.3.jar)


## What is Xfactory?

Xfactory runs xlst in a **batch-mode** (filesystem) or via **REST-Service** or in **Web GUI** (Workbench).

The batchmode work with a directory structure *in/done/error*. If you drop a xml file in *in/xyz* then Xfactory try to process this file with a xls-File *"yxz.xsl"* in the conf-directory.
Xfactory pick up more the one file at once. Please check *application.property* file the adjust the queueing if necessary.

## Functions

### Batchmode

The batchmode converts xml files very fast and the configuration is flexible and pretty easy.

Inside application.properties you'll find a parameter named *"xfactory.path.basepath"*. All directories and operations are relative to this directory.

#### Directories

| Path | Description |
| -------- | -------- |
| xfactory.path.basepath/conf | here we store all xsl-Files we need for processing |
| xfactory.path.basepath/in | here you find all subdirectories |
| xfactory.path.basepath/done | files which are processwed are store here |
| xfactory.path.basepath/out | new files - after transformation |
| xfactory.path.basepath/error | if something is wrong we move the files to this directory |

(Always refer application.properties for a detailed setup.
  For example you can rename in/done/out/error if needed.)

#### Processing strategy

Xfactory look for all subdirectories under xfactory.path.basepath/in. For each subdirectory Xfactory search for files. If there is a xml-file (use parameter *xfactory.filter.process.regex* to pick up different files) it picks up this file and try to find a xls-File inside xfactory.path.basepath/conf with exactly the same name like the subdirectory name plus *.xsl* extension.

While processing the file, Xfactory put a semaphore files like *"xyz.job", "xyz.done"* and so on.

After a successful transformation Xfactory store the new xml-file to a subdiretory in *xfactory.path.basepath/out* with the same name. If necessary Xfactory creates that subdirectory. The sourcefiles are moved to a subdirectory with the same name under *xfactory.path.basepath/done*.

In case of an error Xfactory leave everything as it is - just a semaphore file with *.error* indicates a problem and prevents the file from being reprocessed.

So, for example, if you want to process your invoice-xml files just:

1. create a subdirectory in xfactory.path.basepath/in named *"invoices"*
2. store you xslt script named *"invoices.xsl"* in *xfactory.path.basepath/conf*

**That's it.**

Now drop files to *xfactory.path.basepath/in/invoices** and Xfactory will process them all and store the result files in *xfactory.path.basepath/out/invoices*.

#### Queuing

Xfactory try to run the processing as fast as possible. For that it picks up more the one file at once an run transformation in parallel.

Please check application.properties file and adjust the queuing parameters for your use.


### Rest-Service

Xfactory currently offer one Service endpoint for transforming an xml file via REST Service.

Just send a xml file via *post method* to

    http://localhost:8080/transform/{xsltfile}/{returnformat}

| Placeholder | Description |
| ----------- | ----------- |
| {xsltfile} | a xslt filename in *xfactory.path.basepath/conf* |
| {returnformat} | xml = then the result is the transformed content (probably xml)<br>json - the result is a json string which contain all available information like source, target, performance and so on.    |

### Web-GUI

#### Toolbars

Please find a short description for all toolbaricons used by Xfactory:

| Icon | Description |
| ------ | ---------- |
| ![Refresh](src/main/resources/static/img/refresh.gif?raw=true "Refresh") | Refresh the list in this context |
| ![AddNew](src/main/resources/static/img/addnew.gif?raw=true "AddNew") | Load file content (from local disk) |
| ![Beauty](src/main/resources/static/img/beautify.gif?raw=true "Beautifier") | Beautify the xml |
| ![Clear](src/main/resources/static/img/clear.gif?raw=true "Clear") | Reset the view (clear errors, clear content) |
| ![Start](src/main/resources/static/img/xsdvalidate.gif?raw=true "Start") | Start transformation or check |
| ![ExecuteXPath](src/main/resources/static/img/start16.gif?raw=true "ExecuteXPath") | Exceute XPath |



| Icon | Description |
| ------ | ---------- |
| ![Pause](src/main/resources/static/img/pause.png?raw=true "Pause") | Pause the batch processing. |
| ![Shutdown](src/main/resources/static/img/shutdown.png?raw=true "Shutdown") | Shutdown the application (force shutdown!) |

#### Library

This section helps you to organize you xslt files for the batch processing component.
Add, delete or view files ...

#### XSLT Batch Transformation

This section shows a live protocoll of each processed xml (batch processing). This protocoll includes
the file processing and the ReST processing.

Additional to that you can pause and shutdown the application via the "remote control".

#### XSLT Workbench

Load a sample xml file and start to work on your xsl-sheet. You can run your sheet and Xfactory shows the result
or a detailed error message.

#### XSD Valitdator

Load a xml file and axsd file and check if the xml follows the xsd definitions.

#### XPath Workbench

Load a xml file and type your Xpath expression and see the result online ...

#### XML Beautifier

Load a xml file and run a xml beautifier.

## Getting Started

### Run the application

To run the application just get the current version of the application jar-File. For example *"Xfactory-0.0.1.jar"*

To start:

    java -jar Xfactory-0.0.1.jar



### Application parameter

To adjust the parameters for your specific environment or for your specific needs, you have to change the *application.properties* file.

You'll find this file inside the jar file (in [...]/BOOT-INF/classes).

There are basically three ways to adjust runtime parameter:

1. Extract *application.properties* from the jar. Do your changes. Pack the file back to the jar-file.<br>
That's great, because you just have one file for your application and it is not that easy to
change something. On the other hand, it is not that flexible. I think it is a good way for a production environment.

2. Extract *application.properties* from the jar. Do your changes. Store the file in the
same directory like your jar-File.<BR>This great because you can easily change your setting or you prepare a couple of different profiles.

3. Passing a parameter when starting the application:<br>
        java -jar Xfactory-0.0.1.jar --server.port=9090<Br>
This is a good way if you have just a few parameters to change.

For additional information please have a look to this: https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html

### Docker¶

Currently there is 'just' a Dockerfile. To build a Image based on the current version run

    docker build -t "xfactory:xfactor.0.0.1" .

from the projects root directory.

Run a container with:

	docker run -p 8080:8080 xfactory:xfactor.0.0.1

## Useful stuff

### Spring boot actuator

Xfactory includes the spring-boot-actuator module. So you can access all the actuator features with

    http://localhost:8080/actuator

You'll find a detailed description here: https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready.html

## Beyond...

> Software is never really finished

Ok, so what is missing?

* Implement testcases, what is not done yet :-(
* create a docker setup to store *application.properties* and all the files (in/out/done) outside the docker-image
* create a FAQ-page for all your questions and notes

(Do you have one more point?)

**In any case ... if you have questions, ideas or bug reports feel free to contact me!**
