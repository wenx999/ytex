@setlocal
@rem wrapper to run build-setup.xml build script

@call setenv.cmd
%ANT_HOME%\bin\ant -Dytex.home=%YTEX_HOME% -buildfile build-setup.xml %*
@endlocal