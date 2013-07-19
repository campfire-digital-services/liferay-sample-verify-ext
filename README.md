# Liferay Sample Verify EXT

*liferay-sample-verify-ext*

## Overview

Liferay Portal 6.1 provides a [data verification process](http://www.liferay.com/documentation/liferay-portal/6.1/user-guide/-/ai/verify) which can be run on startup to fix data integrity problems.

It also provides functions to [export/import data](http://www.liferay.com/documentation/liferay-portal/6.1/user-guide/-/ai/managing-pages-in-liferay-portal) to/from a Liferay ARchive (LAR) file.

This project provides a sample solution to the following LPS tickets :

* [LPS-35280 - Upgrade Process 6.0 EE SP2 (6.0.12) to 6.1 EE GA2 (6.1.20) fails during Verify Document Library Process](https://issues.liferay.com/browse/LPS-35280 "LPS-35280 - Upgrade Process 6.0 EE SP2 (6.0.12) to 6.1 EE GA2 (6.1.20) fails during Verify Document Library Process").
* [LPS-37869 - Filename Exception when Importing LAR file with puncutation characters in document file name](https://issues.liferay.com/browse/LPS-37869 "LPS-37869 - Filename Exception when Importing LAR file with puncutation characters in document file name").


## Supported Products

* Liferay Portal 6.1 EE GA2 (6.1.20+)


## Usage

### Configure Portal Properties

Add/update the following properties in file "LIFERAY_HOME/portal-ext.properties" :

| *Property*        | *Description*  | *Usage* | *Default Value* |
| :-------------  |:-------------| :----- | :----- |
| verify.frequency | Data verification frequency. | Verify Suite | 1 (run once) |
| verify.dl.file.name.normalization.enabled                | Enable DL file name normalization during Verify Document Library process. | Verify Document Library | true |
| verify.dl.file.name.normalization.max.list.page.size     | DL file items to normalize in logical "page" of files. | Verify Document Library | 50 |
| layout.import.dl.file.name.normalization.enabled | Enable DL file name normalization during LAR import for Document Library files. | Layout Import Document Library | true |


### Sample Portal Properties

```
##
## Verify
##

    #
    # Input a list of comma delimited class name that implement
    # com.liferay.portal.integrity.VerifyProcess. These classes will run on
    # startup to verify and fix any integrity problems found in the database.
    #
    #verify.processes=com.liferay.portal.verify.VerifyProcessSuite
    # see LPS-35280 / LPS-37869
    verify.processes=au.com.permeance.liferay.portal.verify.CustomVerifyProcessSuite

    #
    # Specify the frequency for verifying the integrity of the database.
    #
    # Constants in VerifyProcess:
    #     public static final int ALWAYS = -1;
    #     public static final int NEVER = 0;
    #     public static final int ONCE = 1;
    #
    #verify.frequency=1
    verify.frequency=-1
    #verify.frequency=0


    #
    # LPS-37869
    # https://issues.liferay.com/browse/LPS-37869
    #

    # LPS-37869 - Verify Document Library
    #verify.dl.file.name.normalization.enabled=false
    #verify.dl.file.name.normalization.max.list.page.size=50
   
    # LPS-37869 - Layout Import Document Library   
    #layout.import.dl.file.name.normalization.enabled=false
```

### Run Data Verify Process Suite

The data Verify Process Suite will run when Liferay Portal starts according the value of property "verify.frequency". 

* To always run data verification, set property "verify.frequency=1"
* To never run data verification, set property "verify.frequency=0"
* To run data verification once, set property "verify.frequency=-1"

See portal property configturation above.


## Building

Step 1. Checkout source from GitHub project to a Liferay Portal Plugins SDK

    % cd LIFERAY_PORTAL_PLUGINS_SDK/ext
    % git clone https://github.com/permeance/liferay-sample-verify-ext

Step 2. Build and package

    % ant clean war

This will build "liferay-sample-verify-ext-A.B.C.D.war" in the Liferay Portal plugins SDK dist folder.

NOTE: You will require JDK 1.6+ and Liferay Portal Plugins SDK.


## Deployment

### Deploy to Liferay Portal + Apache Tomcat Bundle

* Deploy "liferay-sample-verify-ext-A.B.C.D.war" to "LIFERAY_HOME/deploy" folder.
* Start Liferay Portal to install EXT plugin.
* Stop Liferay Portal.
* Start Liferay Portal again to use EXT plugin.


## Project Team

* Tim Telcik - tim.telcik@permeance.com.au


## References

### Data Verification

* [Liferay Portal 6.1 - User Guide - Verify](http://www.liferay.com/documentation/liferay-portal/6.1/user-guide/-/ai/verify "Liferay Portal 6.1 - User Guide - Verify")
* [LPS-35280 - Upgrade Process 6.0 EE SP2 (6.0.12) to 6.1 EE GA2 (6.1.20) fails during Verify Document Library Process](https://issues.liferay.com/browse/LPS-35280 "LPS-35280 - Upgrade Process 6.0 EE SP2 (6.0.12) to 6.1 EE GA2 (6.1.20) fails during Verify Document Library Process").


### Layout Export/Import

* [Liferay Portal 6.1 - User Guide - Creating sites and managing pages](http://www.liferay.com/documentation/liferay-portal/6.1/user-guide/-/ai/managing-pages-in-liferay-portal "Liferay Portal 6.1 - User Guide - Creating sites and managing pages")
* [LPS-37869 - Filename Exception when Importing LAR file with puncutation characters in document file name](https://issues.liferay.com/browse/LPS-37869 "LPS-37869 - Filename Exception when Importing LAR file with puncutation characters in document file name").
