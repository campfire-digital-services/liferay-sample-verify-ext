# Liferay Sample Verify EXT

*liferay-sample-verify-ext*

This project provides a sample solution for the following LPS tickets :

* [LPS-35280 - Upgrade Process 6.0 EE SP2 (6.0.12) to 6.1 EE GA2 (6.1.20) fails during Verify Document Library Process](https://issues.liferay.com/browse/LPS-35280 "LPS-35280 - Upgrade Process 6.0 EE SP2 (6.0.12) to 6.1 EE GA2 (6.1.20) fails during Verify Document Library Process").
* [LPS-37869 - Filename Exception when Importing LAR file with puncutation characters in document file name](https://issues.liferay.com/browse/LPS-37869 "LPS-37869 - Filename Exception when Importing LAR file with puncutation characters in document file name").


## Supported Products

* Liferay Portal 6.1 EE GA2 (6.1.20+)


## Building

Step 1. Checkout source from GitHub project to a Liferay Portal Plugins SDK

    % cd LIFERAY_PORTAL_PLUGINS_SDK/ext
    % git clone https://github.com/permeance/liferay-sample-verify-ext

Step 2. Build and package

    % ant clean war

This will build "liferay-sample-verify-ext-A.B.C.D.war" in the Liferay Portal plugins SDK dist folder.

NOTE: You will require JDK 1.6+ and Liferay Portal Plugins SDK.


## Installation

### Liferay Portal + Apache Tomcat Bundle

* Deploy "liferay-sample-verify-ext-A.B.C.D.war" to "LIFERAY_HOME/deploy" folder.
* Start Liferay Portal to install EXT plugin.
* Stop Liferay Portal.
* Start Liferay Portal again to use EXT plugin.


## Project Team

* Tim Telcik - tim.telcik@permeance.com.au
