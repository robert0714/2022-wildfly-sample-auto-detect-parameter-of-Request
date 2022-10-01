# Auto-detecting the Parameter's Encoding of the HttpRequest in Wildfly
Using org.apache.tika to deciding to when to encoding from Http Request, we can get the correct data .
## Standalone.bat
```bat

set "JAVA_OPTS=%JAVA_OPTS% -Dserver.port=8080  " 
set "JAVA_OPTS=%JAVA_OPTS% -Djboss.bind.address=0.0.0.0 " 
set "JAVA_OPTS=%JAVA_OPTS% -javaagent:D:\DATA\JAVA_program\program\server\wildfly\elastic-apm-agent-1.33.0.jar"
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.service_name=robertTest"
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.server_urls=http://192.168.50.92:8200"
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.enabled=true"
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.application_packages=com.cht.* "
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.trace_methods=com.cht.* "
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.stack_trace_limit=180 "
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.trace_methods_duration_threshold=100ms "
REM set "JAVA_OPTS=%JAVA_OPTS% -Dfile.encoding=UTF8 "
REM set "JAVA_OPTS=%JAVA_OPTS% -Dorg.apache.catalina.connector.URI_ENCODING=UTF-8"
REM set "JAVA_OPTS=%JAVA_OPTS% -Dsun.jnu.encoding=UTF-8"


set "JAVA_OPTS=%JAVA_OPTS% -Dfile.encoding=MS950 "
set "JAVA_OPTS=%JAVA_OPTS% -Dorg.apache.catalina.connector.URI_ENCODING=MS950"
set "JAVA_OPTS=%JAVA_OPTS% -Dsun.jnu.encoding=MS950"
set "JAVA_OPTS=%JAVA_OPTS% -Dorg.apache.catalina.connector.USE_BODY_ENCODING_FOR_QUERY_STRING=true"

rem By default debug mode is disable.
```
