@setlocal
@call setenv.cmd
javaw -classpath %TOOLS_CP% %TOOLS_OPTIONS% ytex.tools.DBAnnotationViewerMain
@endlocal
