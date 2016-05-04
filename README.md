# Liferay Sample Verify EXT

*liferay-sample-verify-ext*

## Overview

Liferay Portal 6.2 provides a [data verification process](https://github.com/liferay/liferay-portal/blob/6.2.x/portal-impl/src/com/liferay/portal/verify/VerifyProcessSuite.java) which can be run on startup to fix data integrity problems.

This project provides a sample solution to the following LPS tickets :

* [LPS-35280 - Upgrade Process 6.0 EE SP2 (6.0.12) to 6.1 EE GA2 (6.1.20) fails during Verify Document Library Process](https://issues.liferay.com/browse/LPS-35280 "LPS-35280 - Upgrade Process 6.0 EE SP2 (6.0.12) to 6.1 EE GA2 (6.1.20) fails during Verify Document Library Process").


## Supported Products

* Liferay Portal 6.2 CE GA6 (6.2.0+)


## Usage

### Configure Portal Properties

Add/update the following properties in file "LIFERAY_HOME/portal-ext.properties" :

| *Property*        | *Description*  | *Usage* | *Default Value* |
| :-------------  |:-------------| :----- | :----- |
| verify.frequency | Data verification frequency. | Verify Suite | 1 (run once) |
| verify.dl.file.name.normalization.enabled                | Enable DL file name normalization during Verify Document Library process. | Verify Document Library | true |
| verify.dl.file.name.normalization.max.list.page.size     | DL file items to normalize in logical "page" of files. | Verify Document Library | 50 |


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
    # verify.processes=com.liferay.portal.verify.VerifyProcessSuite
    # see LPS-35280
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


##
## Patches
##

    # LPS-35280 - Verify Document Library
    # https://issues.liferay.com/browse/LPS-35280
    #verify.dl.file.name.normalization.enabled=false
    #verify.dl.file.name.normalization.max.list.page.size=50
```

### Run Data Verify Process Suite

The data Verify Process Suite will run when Liferay Portal starts according the value of property "verify.frequency". 

| *Property*        | *Value*  | *Description* |
| :-------------  |:-------------| :----- |
| verify.frequency | 1 | always run data verification |
| verify.frequency | 0 | never run data verification |
| verify.frequency | 1 | only run data verification once |

See portal property configturation above.


## Building

Step 1. Checkout source from GitHub project to a Liferay Portal Plugins SDK

    % mkdir work
    % cd work
    % git clone https://github.com/permeance/liferay-sample-verify-ext
    $ cd liferay-sample-verify-ext
    % git checkout 6.2.0.x
    % git branch
    6.2.0.x

Step 2. Build and package

    % mvn -U clean package

This will build "liferay-sample-verify-ext-A.B.C.D.war" in the project target folder.

NOTE: You will require JDK 1.8+, Apache Maven 3.2.x and Liferay Maven SDK 6.2 CE GA6.


## Deployment

### Deploy to Liferay Portal + Apache Tomcat Bundle

* Deploy "liferay-sample-verify-ext-A.B.C.D.war" to "LIFERAY_HOME/deploy" folder.
* Start Liferay Portal to install EXT plugin.
* Stop Liferay Portal.
* Redeploy all portal plugins (including liferay-sample-verify-ext)
* Start Liferay Portal again to use EXT plugin.


## Project Team

* Tim Telcik - tim.telcik@permeance.com.au


## References

### Data Verification

* Liferay Portal 6.2 - User Guide - Verify (TBC)
* [GitHub Liferay Portal 6.2 - VerifyProcessSuite](https://github.com/liferay/liferay-portal/blob/6.2.x/portal-impl/src/com/liferay/portal/verify/VerifyProcessSuite.java)
* [LPS-35280 - Upgrade Process 6.0 EE SP2 (6.0.12) to 6.1 EE GA2 (6.1.20) fails during Verify Document Library Process](https://issues.liferay.com/browse/LPS-35280 "LPS-35280 - Upgrade Process 6.0 EE SP2 (6.0.12) to 6.1 EE GA2 (6.1.20) fails during Verify Document Library Process").
