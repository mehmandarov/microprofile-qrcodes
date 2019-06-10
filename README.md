# QR codes on-demand with MicroProfile

## Introduction

This project will generate a QR code as an image and a PDF on demand.

### What it does?

The project will generate a QR code based on the string passed in the URL (in this case _"MyString"_):

    http://localhost:8181/qrcreator/qr/MyString/
    
Or a PDF file with the provided string in clear text and the QR code:
    
    http://localhost:8181/qrcreator/qr/MyString/pdf

The QR code will contain a JSON object with the following structure:

    { 
        "Name", "MyString",
        "Key", "EncodedStringWithSalt"
    }
    
Where, 

* _Name_ is the string provided as the URL
* _Key_ is the hashed _Name_ with salt.

The contents of the QR code can be modified in `QRCodeSupplier` class, while the hashing algorithm is specified in `SecretKeyProvider` class.
    

### Running the project

The generation of the executable jar file can be performed by issuing the following command

    mvn clean package

This will create an executable jar file `qrcreator.jar` within the _target_ maven folder. This can be started by executing the following command

    java -jar target/qrcreator.jar

To launch the test page, open your browser at the following URL

    http://localhost:8181/qrcreator/index.html


## MicroProfile Specification Examples

By default, there is always the creation of a JAX-RS application class to define the path on which the JAX-RS endpoints are available.

The application's endpoint is defined in class `QRController`.

More information on MicroProfile can be found [here](https://microprofile.io/).


### Config

Configuration of your application parameters ([specification][2]).

The example class `SecretKeySupplier` shows you how to configure and define default values for variables.


### Health

The health status can be used to determine if the 'computing node' needs to be discarded/restarted or not ([specification][3]).

The class `ServiceHealthCheck` contains an example of a custom check which can be integrated to health status checks of the instance.  The index page contains a link to the status data.


### Metrics

The Metrics exports _Telemetric_ data in a uniform way of system and custom resources ([specification][4]).

The class `RandomStringsController` contains an example how you can measure the execution time of a request. The index page also contains a link to the metric page (with all metric info).


### Open API

Exposes the information about your endpoints in the format of the OpenAPI v3 specification ([docs][5]).

The index page contains a link to the OpenAPI information of the available endpoints for this project.



## Author
[Rustam Mehmandarov][6]



[1]: https://microprofile.io/
[2]: https://microprofile.io/project/eclipse/microprofile-config
[3]: https://microprofile.io/project/eclipse/microprofile-health
[4]: https://microprofile.io/project/eclipse/microprofile-metrics
[5]: https://microprofile.io/project/eclipse/microprofile-open-api
[6]: https://github.com/mehmandarov
