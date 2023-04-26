Application for Mastercard Send APIs

[![](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_send-api-demo-app&metric=alert_status)](https://sonarcloud.io/dashboard?id=Mastercard_send-api-demo-app)
[![](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_send-api-demo-app&metric=coverage)](https://sonarcloud.io/dashboard?id=Mastercard_send-api-demo-app)
[![](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_send-api-demo-app&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=Mastercard_send-api-demo-app)

========

+ [Overview](#overview)
+ [Prerequisites](#prerequisites)
+ [What You Will Run](#what-you-will-run)
+ [Setup](#setup)
+ [Use The Application](#use-the-application)
+ [To Go Further](#to-go-further)
+ [Support](#support)
+ [License](#license)


## <a name="overview"></a>Overview
This is a simple Open API and Java Spring Boot application that shows how to integrate different endpoints of the Transfer Eligibility, Payments, and Customer Parameter Management (CPM) APIs in different environments. 
It provides API Client capabilities with OAuth 1.0a and JSON Web Encryption (JWE) based encryption/decryption for accessing Mastercard Send API endpoints. You can run the application through your command prompt 
or terminal without running any Integrated Development Environment (IDE). You can use your preferred IDE to go through the source code and check the workflows of all the endpoints.

This application is suitable for the Originating Institutions and Transaction Initiators.  The CPM API endpoints are suitable for Sponsor Originating Institutions who will be sponsoring a 
partner’s (Transaction Initiator, TI) Mastercard Send funds transfer activity.

## <a name="prerequisites"></a>Prerequisites
- Java 11. Ensure that your `JAVA_HOME` environment variable is pointing to your JDK installation or have the Java executable on your `PATH` environment variable.
- <a href="https://maven.apache.org/download.cgi">Maven 3.6.0+</a>
- IDE of your choice, for example, Eclipse, IntelliJ IDEA, or VS Code.
- An account on Mastercard Developers with access to the Mastercard Send 2.0 APIs. Create a Mastercard Developers project with the Mastercard Send 2.0 API service to generate/set up the Sandbox keys that are 
required to use the Sandbox environment. For guidance on creating a project, see the 
<a href="https://developer.mastercard.com/platform/documentation/getting-started-with-mastercard-apis/quick-start-guide/">quick start guide</a>.

## <a name="what-you-will-run"></a>What You Will Run
You will run a spring boot application that enables you to:

- Check the eligibility of the sending account and receiving account for a particular funds transfer (before initiating that transfer).
- Check whether a single account is able to send and receive transfers.
- Create a Payment Transaction to send funds to a Recipient.
- Retrieve details of a Payment Transaction, including its latest status, by searching for its `paymentReference` or `paymentId`.
- Set and adjust Mastercard Send transaction processing limits for your partners (TIs) and their Transfer Acceptors (TAs).
- Configure alerts.
- Retrieve the current daily usage figures.

## <a name="setup"></a>Setup
Once you clone the project, you may open it in your IDE.  The following steps use the keys and details that are downloaded or provided when creating your Mastercard Developers project. 
Some items are available on your Developer Dashboard project page after project setup. For more information about the keys, 
see <a href="https://developer.mastercard.com/mastercard-send-2-0/documentation/api-basics/">API Basics</a>.

### Configure Signing Keys
Signing keys are used to generate Authorization headers, which Mastercard uses to authenticate your API requests.

1. Copy the downloaded .p12 file to the `./src/main/resources` path and update the file name in the `./src/main/resources/application.properties` file for the `signing.pkcs12KeyFile=` property.
2. Using the signing key details obtained from Mastercard Developers, update the `./src/main/resources/application.properties` file for the `signing.keyAlias`, `signing.keyPassword`, `signing.consumerKey` properties.

### API Specification
You can directly run the application with the API specifications (YAML file) included in this application. This application includes the API specifications that were available at 
the time of preparing this application.

If there is a newer version of an API specification, you can download the YAML files using the links below and add them to your `./src/main/resources` directory:

* <a href="https://static.developer.mastercard.com/content/mastercard-send-2-0/swagger/mastercard-send-transfer-eligibility-api-swagger.yaml">mastercard-send-transfer-eligibility-api-swagger.yaml</a>  (41KB)
* <a href="https://static.developer.mastercard.com/content/mastercard-send-2-0/swagger/mastercard-send-payments-api-swagger.yaml">mastercard-send-payments-api-swagger.yaml</a>  (109KB)
* <a href="https://static.developer.mastercard.com/content/mastercard-send-2-0/swagger/mastercard-send-customer-parameter-management-api-swagger.yaml">mastercard-send-customer-parameter-management-api-swagger.yaml</a>  (53KB)
* The latest API specifications are available in the <a href="https://developer.mastercard.com/mastercard-send-2-0/documentation/api-reference/">API Reference</a> page.

### Build And Run
The server port is configured to 8080.

Since you have installed Maven during the initial setup, you can build and run the code using the following command:

`mvn spring-boot:run`

You can access the application by visiting `localhost:8080` in your browser.

## <a name="use-the-application"></a>Use The Application

When you start the application by visiting `localhost:8080`, the work flows are shown below.  For convenience, the forms are 
pre-populated with a functioning request, but you can change the values and submit the form.  Submitting the form will 
call the respective endpoints of the API. You can also view the original request and any related responses as shown below the forms.    

&#8287;  
&#8287;  
### Send Demo App Flow  

![send_demo_app_workflow_gif](docs/send_demo.gif)  

&#8287;  
&#8287;  
### Customer Parameter Management Flow  

![cpm_workflow_gif](docs/customer_parameter_management.gif)  


## <a name="to-go-further"></a>To Go Further
### Prerequisite to MTF or Production Environment Usage
To use the APIs in the MTF and Production environments, you must <a href="https://developer.mastercard.com/mastercard-send/documentation/send-2-registration/">register</a> as a Mastercard Send Program Participant.
Your <a href="https://developer.mastercard.com/mastercard-send-2-0/documentation/support/">Mastercard representative</a> can help you with this process, during which you will be given the Partner Reference IDs
for use in your API calls to those environments.  You must also be set up for those environments, which requires you to provide your Consumer Key to your Mastercard Delivery Manager,
see <a href="https://developer.mastercard.com/mastercard-send-2-0/documentation/api-basics/#client-authentication">Client authentication</a>.  After successful MTF testing, you set up your Production keys by
requesting Production access for your project in your <a href="https://developer.mastercard.com/dashboard">Developer Dashboard</a> project page.

### Switch Between Environments
You can switch between Sandbox, MTF, and Production environments for the APIs by updating the `environment` property in the `./src/main/resources/application.properties` file.
These are the values you should use for each environment:

* Sandbox: `environment=sandbox`
* MTF: `environment=mtf`
* Production: `environment=production`

When you change environment, remember that you might need to adjust the keys and properties as per the instructions above.

### Enable Encryption
The MTF and Production environments use payload encryption. Sandbox does not.

To enable or disable encryption/decryption for your requests and responses, update the `client.encryption.enabled` property to either true or false in the `./src/main/resources/application.properties` file.

To use encryption/decryption, you must configure the encryption keys:

1. Copy these files to the `./src/main/resources` path:

* Client Encryption Key certificate .pem file
* Mastercard Encryption Key .p12 file
2. In the `./src/main/resources/application.properties` file:
* Update the Client Encryption Key certificate file name for the `client.encryption.certificate=` property.
* Update the Mastercard Encryption Key file name for the `mastercard.encryption.pkcs12KeyFile=` property.
* Update the Mastercard Encryption Key details for the `mastercard.encryption.keyAlias=` and `mastercard.encryption.keystorePassword=` properties.

Client Encryption Keys are used to encrypt the request payload sent by you. Mastercard Encryption Keys are used to decrypt the response payload returned by Mastercard. Remember that the MTF and
Production environments use different sets of keys.

### Configure Partner Details
To use the APIs in the MTF and Production environments, update the `const PARTNER_ID=` parameter in the `./src/main/resources/static/assets/js/environment.js` file to match your Partner Reference ID.

### To Change The Server Port
Changing the port isn't necessary, but if you want to change the server port, you can update the `server.port=` property in the `./src/main/resources/application.properties` file.

If you update the default port, you must update the new port in the `./src/main/resources/static/assets/js/environment.js` path for the `const SERVER_URL` property.

## <a name="support"></a>Support

If you would like further information, please send an email to `apisupport@mastercard.com`

* For information regarding licensing, refer to the [LICENSE](LICENSE.md).
* For copyright information, refer to the [COPYRIGHT](COPYRIGHT.md).
* For instructions on how to contribute to this project, refer to the [CONTRIBUTING](CONTRIBUTING.md).
* For changelog information, refer to the [CHANGELOG](CHANGELOG.md).

## <a name="license"></a>License
Copyright 2023 Mastercard

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and limitations under the License.

