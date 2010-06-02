@rem -------------------------------------------
@rem customize these variables to match your environment
@rem -------------------------------------------
@rem where java is installed
@set JAVA_HOME=C:\java\jdk-6u17-windows-x32

@rem where ant is installed
@rem download from http://ant.apache.org/bindownload.cgi
@set ANT_HOME=C:\java\apache-ant-1.8.0

@rem where ant-contrib is installed
@rem download from http://sourceforge.net/projects/ant-contrib/files/
@set ANT_CONTRIB_HOME=C:\java\ant-contrib

@rem where maveric arc is installed
@set MAVERIC_HOME=C:\java\MavericArcApp-1.0.0

@rem where mssql server jdbc drivers were unpacked 
@rem download from http://www.microsoft.com/downloads/details.aspx?displaylang=en&FamilyID=a737000d-68d0-4531-b65d-da0f2a735707
@set SQLJDBC_HOME=C:\java\sqljdbc_2.0

@rem where ytex is intalled 
@set YTEX_HOME=E:\projects\VA\dist\ytex

@rem where ms sql server tools are installed
@rem should contain bcp.exe and sqlcmd.exe 
@set MSSQL_TOOLS=C:\Program Files\Microsoft SQL Server\100\Tools\Binn

@rem tomcat installation directory
@set CATALINA_HOME=C:\Users\vhacongarlav\java\apache-tomcat-6.0.24

@rem -------------------------------------------
@rem end customizations.  The following is environment-independent
@rem -------------------------------------------


@set PATH=%JAVA_HOME%\bin;%MSSQL_TOOLS%;%SystemRoot%;%SystemRoot%\System32;%SystemRoot%\System32\wbem;%ANT_HOME%\apache-ant\bin

@rem if you are using a 64-bit jdk, adjust accordingly
@set PATH=%PATH%;%SQLJDBC_HOME%\enu\auth\x86

@rem add ytex dependencies
@set YTEX_CP=%YTEX_HOME%\lib\com.springsource.antlr-2.7.7.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\com.springsource.org.aopalliance-1.0.0.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\com.springsource.org.apache.commons.codec-1.3.0.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\com.springsource.org.apache.commons.collections-3.2.1.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\com.springsource.org.apache.commons.dbcp-1.2.2.osgi.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\com.springsource.org.apache.commons.fileupload-1.2.0.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\com.springsource.org.apache.commons.httpclient-3.1.0.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\com.springsource.org.apache.commons.io-1.4.0.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\com.springsource.org.apache.commons.lang-2.1.0.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\com.springsource.org.apache.commons.logging-1.1.1.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\com.springsource.org.apache.commons.pool-1.5.3.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\com.springsource.org.aspectj.weaver-1.6.8.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\dom4j-1.6.1.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\ehcache-1.2.3.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\GeneralNegEx.Java.v.1.2.05092009.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\hibernate3.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\javassist-3.9.0.GA.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\jta-1.1.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.aop-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.asm-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.aspects.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.beans-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.context-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.context.support-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.core-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.expression-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.jdbc-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.orm-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.transaction-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.web-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\org.springframework.web.servlet-3.0.2.RELEASE.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\slf4j-api-1.5.8.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\slf4j-jcl-1.5.8.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\weka.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\ytex.model.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\ytex.negex.jar
@set YTEX_CP=%YTEX_CP%;%YTEX_HOME%\lib\ytex.uima.jar

@rem add JDBC dependencies
@set SYSTEM_CP=%SQLJDBC_HOME%\enu\sqljdbc4.jar
@set SYSTEM_CP=%SYSTEM_CP%;%YTEX_HOME%\config\desc
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\dest
@set SYSTEM_CP=%SYSTEM_CP%;%YTEX_HOME%\maveric\resources

@rem add maveric and other dependencies
@rem add patches before the rest of the maveric/ctakes libraries
@set SYSTEM_CP=%SYSTEM_CP%;%YTEX_HOME%\lib\ctakes-patches.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\log4j-1.2.14.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\lvg2008dist.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\opennlp-tools-1.4.0.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\ext\mavericPipeline-1.0.0.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\jdom.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\maxent-2.5.0.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\OpenAI_FSM.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\trove.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\lucene-1.4-final.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\uima-adapter-soap.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\uima-adapter-vinci.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\uima-core.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\uima-cpe.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\uima-document-annotation.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\uima-tools.jar
@set SYSTEM_CP=%SYSTEM_CP%;%MAVERIC_HOME%\lib\uima-cpe.jar

@rem TOOLS_CP is for running uima tools
@set TOOLS_CP=%YTEX_CP%;%SYSTEM_CP%

@set TOOLS_OPTIONS=-Xmx1g -Dlog4j.configuration=file:///%YTEX_HOME%/config/desc/log4j.properties -Djava.util.logging.config.file=file:///%YTEX_HOME%/config/desc/logger.properties -DVNS_HOST=localhost

@rem we have a tomcat configuration in this directory
@set CATALINA_BASE=%YTEX_HOME%\web\catalina