构建及运行顺序

1.       首先建立s_user s_role表，并向表中写入数据

2.       建立SUserService SUserRepository user  SRoleRepository SRole类

3.       运行程序，将s_user中用户的密码加密

4.       如三中所述，配置Spring Security

5.       运行，访问http://localhost:8080/hello，系统出现如下界面：



用户名输入useremail  密码输入111111，点sign in则进入hello.html



重启浏览器，再访问http://localhost:8080/hello，则无需登录，直接到达。

在hello页面点sign out后，则返回home页面，退出了登录。


添加到 Linux 服务：
```
ln -s /opt/kemis_test/logistics-test-0.0.1-package.jar /etc/init.d/logistics-test
ll /etc/init.d/
chkconfig --add logistics-test
chkconfig --list

service logistics-test start
service logistics-test stop
```
