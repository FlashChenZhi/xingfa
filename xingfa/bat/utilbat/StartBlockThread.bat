@echo off
set JAVAHOME=%JAVAHOME%
set IDE=%IDE%
set XINGFAPATH=%XINGFAPATH%
set CLASSPATH=com.thread.StartBlockThread
set JARPATH="%JAVAHOME%\bin\java" -classpath "%JAVAHOME%\jre\lib\charsets.jar;%JAVAHOME%\jre\lib\deploy.jar;%JAVAHOME%\jre\lib\ext\access-bridge-64.jar;%JAVAHOME%\jre\lib\ext\dnsns.jar;%JAVAHOME%\jre\lib\ext\jaccess.jar;%JAVAHOME%\jre\lib\ext\localedata.jar;%JAVAHOME%\jre\lib\ext\sunec.jar;%JAVAHOME%\jre\lib\ext\sunjce_provider.jar;%JAVAHOME%\jre\lib\ext\sunmscapi.jar;%JAVAHOME%\jre\lib\ext\zipfs.jar;%JAVAHOME%\jre\lib\javaws.jar;%JAVAHOME%\jre\lib\jce.jar;%JAVAHOME%\jre\lib\jfr.jar;%JAVAHOME%\jre\lib\jfxrt.jar;%JAVAHOME%\jre\lib\jsse.jar;%JAVAHOME%\jre\lib\management-agent.jar;%JAVAHOME%\jre\lib\plugin.jar;%JAVAHOME%\jre\lib\resources.jar;%JAVAHOME%\jre\lib\rt.jar;%XINGFAPATH%\out\production\wcs;%XINGFAPATH%\xingfa\wcs\lib\jxl.jar;%XINGFAPATH%\xingfa\wcs\lib\ojdbc14.jar;%XINGFAPATH%\xingfa\wcs\lib\jdom-1.1.jar;%XINGFAPATH%\xingfa\wcs\lib\junit-4.11.jar;%XINGFAPATH%\xingfa\wcs\lib\antlr-2.7.7.jar;%XINGFAPATH%\xingfa\wcs\lib\dom4j-1.6.1.jar;%XINGFAPATH%\xingfa\wcs\lib\jedis-2.5.2.jar;%XINGFAPATH%\xingfa\wcs\lib\c3p0-0.9.2.1.jar;%XINGFAPATH%\xingfa\wcs\lib\log4j-1.2.15.jar;%XINGFAPATH%\xingfa\wcs\lib\ezmorph-1.0.6.jar;%XINGFAPATH%\xingfa\wcs\lib\joda-time-1.6.jar;%XINGFAPATH%\xingfa\wcs\lib\jpinyin-1.1.6.jar;%XINGFAPATH%\xingfa\wcs\lib\xstream-1.4.4.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-io-2.5.jar;%XINGFAPATH%\xingfa\wcs\lib\httpcore-4.4.1.jar;%XINGFAPATH%\xingfa\wcs\lib\stax-api-1.0.1.jar;%XINGFAPATH%\xingfa\wcs\lib\xmlbeans-2.3.0.jar;%XINGFAPATH%\xingfa\wcs\lib\aopalliance-1.0.jar;%XINGFAPATH%\xingfa\wcs\lib\servlet-api-2.5.jar;%XINGFAPATH%\xingfa\wcs\lib\slf4j-api-1.6.1.jar;%XINGFAPATH%\xingfa\wcs\lib\xml-apis-1.0.b2.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-dbcp-1.1.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-digester.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-lang-2.5.jar;%XINGFAPATH%\xingfa\wcs\lib\httpclient-4.4.1.jar;%XINGFAPATH%\xingfa\wcs\lib\StartBlockThread.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-lang3-3.4.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-pool2-2.3.jar;%XINGFAPATH%\xingfa\wcs\lib\hamcrest-core-1.3.jar;%XINGFAPATH%\xingfa\wcs\lib\poi-3.14-20160307.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-codec-1.10.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-dbcp-1.2.2.jar;%XINGFAPATH%\xingfa\wcs\lib\jackson-core-2.4.2.jar;%XINGFAPATH%\xingfa\wcs\lib\jandex-1.1.0.Final.jar;%XINGFAPATH%\xingfa\wcs\lib\json-lib-2.4-jdk15.jar;%XINGFAPATH%\xingfa\wcs\lib\aspectjweaver-1.8.2.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-collections.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-dbutils-1.1.jar;%XINGFAPATH%\xingfa\wcs\lib\javassist-3.18.1-GA.jar;%XINGFAPATH%\xingfa\wcs\lib\slf4j-log4j12-1.6.1.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-logging-1.1.1.jar;%XINGFAPATH%\xingfa\wcs\lib\joda-time-1.6-javadoc.jar;%XINGFAPATH%\xingfa\wcs\lib\jackson-databind-2.4.2.jar;%XINGFAPATH%\xingfa\wcs\lib\jboss-logging-3.1.3.GA.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-beanutils-1.9.2.jar;%XINGFAPATH%\xingfa\wcs\lib\javax.servlet-api-3.0.1.jar;%XINGFAPATH%\xingfa\wcs\lib\poi-ooxml-3.14-20160307.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-tx-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-fileupload-1.3.2.jar;%XINGFAPATH%\xingfa\wcs\lib\mongo-java-driver-2.14.3.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-aop-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-orm-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-web-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\commons-collections-3.2.1.jar;%XINGFAPATH%\xingfa\wcs\lib\jackson-annotations-2.4.2.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-core-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-jdbc-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-test-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\hibernate-c3p0-4.3.6.Final.jar;%XINGFAPATH%\xingfa\wcs\lib\hibernate-core-4.3.6.Final.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-beans-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-webmvc-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\mchange-commons-java-0.2.3.4.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-aspects-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-context-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\mysql-connector-java-5.1.40-bin.jar;%XINGFAPATH%\xingfa\wcs\lib\poi-ooxml-schemas-3.14-20160307.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-expression-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\hibernate-jpa-2.1-api-1.0.0.Final.jar;%XINGFAPATH%\xingfa\wcs\lib\spring-context-support-4.1.0.RELEASE.jar;%XINGFAPATH%\xingfa\wcs\lib\jboss-logging-annotations-1.2.0.Beta1.jar;%XINGFAPATH%\xingfa\wcs\lib\hibernate-commons-annotations-4.0.5.Final.jar;%XINGFAPATH%\xingfa\wcs\lib\jboss-transaction-api_1.2_spec-1.0.0.Final.jar"

java -classpath %JARPATH% %CLASSPATH%