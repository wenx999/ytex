@rem set tomcat environment variables
@rem this assumes that YTEX_HOME\setenv.bat has been called

@rem jdbc driver should be loaded in system classpath
@rem the only thing we need from %YTEX_HOME%\config\desc is the ytex.properties file
@set CLASSPATH=%SQLJDBC_HOME%\enu\sqljdbc4.jar;%YTEX_HOME%\config\desc
@set CATALINA_OPTS=-Dorg.apache.el.parser.COERCE_TO_ZERO=false
