REM –Dspring.config.location=file:/opt/kemis/application.yml 必须放在 jar 文件之后
java -jar -Xms4096m -Xmx8196m -Dspring.profiles.active=production logistics-web-0.0.1-package.jar
