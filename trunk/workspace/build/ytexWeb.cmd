@setlocal
@rem to start tomcat
@call setenv.cmd
%CATALINA_HOME%\bin\catalina.bat run
@endlocal
