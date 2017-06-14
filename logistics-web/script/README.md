# 加入启动项

http://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html

将 logistics-web-0.0.1-package.conf 和 logistics-web-0.0.1-package.jar 放在同一目录

## Unix/Linux services
Spring Boot application can be easily started as Unix/Linux services using either init.d or systemd.

The script supports the following features:

- Starts the services as the user that owns the jar file
- Tracks application’s PID using /var/run/<appname>/<appname>.pid
- Writes console logs to /var/log/<appname>.log

Assuming that you have a Spring Boot application installed in /var/myapp, to install a Spring Boot application as an init.d service simply create a symlink:

```$ sudo ln -s /opt/kemis_service/logistics-web-0.0.1-package.jar /etc/init.d/logistics-web-0.0.1-package```
Once installed, you can start and stop the service in the usual way. For example, on a Debian based system:

```$ service logistics-web-0.0.1-package start```

遇到```env: /etc/init.d/logistics-web-0.0.1-package: 权限不够```
尝试修改 jar 的执行权限和
```chmod 400 logistics-web-0.0.1-package.conf```

*If your application fails to start, check the log file written to /var/log/<appname>.log for errors.*
You can also flag the application to start automatically using your standard operating system tools. For example, on Debian:

```$ update-rc.d myapp defaults <priority>```
