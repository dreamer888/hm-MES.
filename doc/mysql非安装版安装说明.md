
1�Լ����� my.ini    

ϵͳ����path   

2   mysqld   --initialize   --console

  
 root  �û�������ʱ���룬�ȼ������� ��Ҫ�����¼��ȥ�޸�����
  Z+sjGdaX<0s#

3 ��װmyslq����  mysqld --install   mysql8

 ж�ط���Ļ�ִ��   mysqld  remove 


4   net start mysql8
# ���������޸�Ϊmysql8,����Ϊnet start mysql8



4   ����ʱ�����¼��ȥ �޸�Ϊ��ʽ���룬

��bin Ŀ¼���� ִ��   mysql  -P3306 -uroot  -p 
Ȼ��������ʱ���� ��¼��ȥ

ִ������������޸�����

  update user set authentication_string=PASSWORD("123456") where user="root";  ���������Ѿ��������ˣ�
 Ҫ��������������Ч
 alter user 'root'@'localhost' identified by '123456';

5���������� ��� cache ���⣬ ����navicat ��¼���˵����� 

 ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';

6 ж��mysql ���� 
 .\mysql �Curoot �Cp
 mysqld �Cremove
 net stop mysql
  

