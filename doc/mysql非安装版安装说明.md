
1自己创建 my.ini    

系统环境path   

2   mysqld   --initialize   --console

  
 root  用户生成临时密码，先记下来， 需要后面登录进去修改密码
  Z+sjGdaX<0s#

3 安装myslq服务  mysqld --install   mysql8

 卸载服务的话执行   mysqld  remove 


4   net start mysql8
# 若将名字修改为mysql8,命令为net start mysql8



4   用临时密码登录进去 修改为正式密码，

在bin 目录下面 执行   mysql  -P3306 -uroot  -p 
然后输入临时密码 登录进去

执行下面的命令修改密码

  update user set authentication_string=PASSWORD("123456") where user="root";  这条命令已经不能用了，
 要用下面的命令才有效
 alter user 'root'@'localhost' identified by '123456';

5下面这上是 解决 cache 问题， 就是navicat 登录不了的问题 

 ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';

6 卸载mysql 服务 
 .\mysql –uroot –p
 mysqld –remove
 net stop mysql
  

