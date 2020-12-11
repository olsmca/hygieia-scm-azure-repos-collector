---
title: Azure repos Collector
tags:
keywords:
summary:
sidebar: hygieia_sidebar
permalink: azure-repos.html
---
Configure the Azure Repos Collector to display and monitor information (related to code contribution activities) on the Hygieia Dashboard, from the Azure DevOps repository. Collect source code details from Azure DevOps repos based on the repository URL.

This project uses Spring Boot to package the collector as an executable JAR file with dependencies.

### Setup Instructions

## Fork and Clone the Collector 

Fork and clone the Artifactory Collector from the [GitHub repo](https://github.com/Hygieia/hygieia-scm-azure-repos-collector). 

To configure the Azure repos Collector, execute the following steps:

*   **Step 1: Change Directory**

Change the current working directory to the `hygieia-scm-azure-repos-collector` directory of your Hygieia source code installation.

For example, in the Windows command prompt, run the following command:

```
cd C:\Users\[username]\hygieia-scm-azure-repos-collector
```

*   **Step 2: Run Maven Build**

Run the maven build to package the collector into an executable jar file:

```
 mvn install
```

The output file `[collector name].jar` is generated in the `hygieia-scm-azure-repos-collector\target` folder.

*   **Step 3: Set Parameters in Application Properties File**

Set the configurable parameters in the `application.properties` file to connect to the Dashboard MongoDB database instance, including properties required by the Azure repos Collector.

For information about sourcing the application properties file, refer to the [Spring Boot Documentation](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-application-property-files).

To configure parameters for the Azure repos Collector, refer to the sample [application.properties](#sample-application-properties-file) file.

*   **Step 4: Deploy the Executable File**

To deploy the `[collector name].jar` file, change directory to `hygieia-scm-azure-repos-collector\target`, and then execute the following from the command prompt:

```
java -jar [collector name].jar --spring.config.name=azure-repos --spring.config.location=[path to application.properties file]
```

### Sample Application Properties File

The sample `application.properties` file lists parameter values to configure the Azure Repos Collector. Set the parameters based on your environment setup.

``` 
		# Database Name
		dbname=dashboarddb

		# Database HostName - default is localhost
		dbhost=localhost

		# Database Port - default is 27017
		dbport=27017

		# MongoDB replicaset
		dbreplicaset=[false if you are not using MongoDB replicaset]
		dbhostport=[host1:port1,host2:port2,host3:port3]

		# Database Username - default is blank
		dbusername=dashboarduser

		# Database Password - default is blank
		dbpassword=dbpassword

		# Logging File location
		logging.file=./logs/azure-repos.log

		#Collector schedule (required)
		azure-repos.cron=0 0/1 * * * *
		
		# Azure Respos API Version (optional, defaults to current version of 6.0)
		azure-repos.apiVersion=6.0

		# Azure Repos API Token (required, user token the collector will use by default, can be overridden on a per repo basis from the UI. API token provided by azure-repos)
		azure-repos.personalAccessToken=
		
		# This is the key generated using the Encryption class in core
		azure-repos.key=<your-generated-key>
		
		# Proxy Host
		azure-repos.proxyHost=
		
		# Proxy Port
		azure-repos.proxyPort=
		
		# Non Proxy xxx.xxx.xxx.xxx
		azure-repos.nonProxy=
```
**Note**: For information on generating your azure-repos key for private repos, refer to [Encryption of Private Repos](../collectors.md#encryption-for-private-repos).
