@setlocal
@rem wrapper to run build-setup.xml build script

@call setenv.cmd
@set CLASSPATH=%ANT_CONTRIB_HOME%\ant-contrib-1.0b3.jar
%ANT_HOME%\bin\ant -Dmaveric.home=%MAVERIC_HOME% -Dytex.home=%YTEX_HOME% -buildfile build-setup.xml %*
@endlocal