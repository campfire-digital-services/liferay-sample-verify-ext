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

### Portal Properties

* verify.dl.file.name.normalization.enabled. Default value is true.
* verify.dl.file.name.normalization.max.list.page.size. Default value is 50.
* layout.import.dl.file.name.normalization.enabled=false Default is true.


### Configure Liferay Portal + Apache Tomcat Bundle

To enable the custom verify suite, add the following properties to file "LIFERAY_HOME/portal-ext.properties" :

{code}
##
## Verify
##

    #
    # Input a list of comma delimited class name that implement
    # com.liferay.portal.integrity.VerifyProcess. These classes will run on
    # startup to verify and fix any integrity problems found in the database.
    #
    #verify.processes=com.liferay.portal.verify.VerifyProcessSuite
    # see LPS-35280 / LRSUPPORT-306
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
## LPS-37869
## https://issues.liferay.com/browse/LPS-37869
##

   # LPS-37869 - Verify Document Library
   #verify.dl.file.name.normalization.enabled=false
   #verify.dl.file.name.normalization.max.list.page.size=50
   
   # LPS-37869 - Import Document Library   
   #layout.import.dl.file.name.normalization.enabled=false
{code}


### Deploy to Liferay Portal + Apache Tomcat Bundle

* Deploy "liferay-sample-verify-ext-A.B.C.D.war" to "LIFERAY_HOME/deploy" folder.
* Start Liferay Portal to install EXT plugin.
* Stop Liferay Portal.
* Start Liferay Portal again to use EXT plugin.


## Project Team

* Tim Telcik - tim.telcik@permeance.com.au
