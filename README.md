# seckill
使用说明：
1.首先从github上把项目传到本地，可以直接下载项目的压缩包，点击Clone or download,然后Download Zip。也可以通过git，使用git clone  https://github.com/YuanTianxin/seckill.git 命令，把项目克隆到本地

2.然后修改数据库连接信息，在resources目录下jdbc.properties配置文件中修改

3.在resources目录下的databaseinfo中有schema.sql文件，里面写的是建表的语句，在mysql中把表给创建好，并且初始化数据，如果mysql是5.5版本及以下的，在创建表的时候会出错，不允许多列出现timestamp类型字段，网上有创建触发器来解决的办法，但是还是建议下一个高版本的mysql，避免麻烦。但是原来的mysql一定要卸载干净，可以参考网上的方法，同时不必担心原来数据库中的表会丢失，你只需要把原来存数据的data文件夹替换新的即可，建议到官网下一个免安装版的，安装版的很容易出现各种解决不了的bug，当然只是建议，有时候还是看人品的。同时还有一个seckill.sql文件，写的是实现秒杀逻辑的mysql的存储过程，用存储过程来进行事务处理。根据语句顺序创建好存储过程。

4.下一个redis，输入redis-server,打开服务，怎么用网上有很多教程。当然不使用redis也可以，毕竟redis只是来优化缓存，并不是必须的，只是在执行过程会报错，但是不影响项目的整体运行。在resources目录下还有一个optimization.txt，写的是优化的方法。

5.导入eclipse，通过tomcat或者其他服务器部署应用

6.在浏览器中输入http://localhost:8080/seckill/meseckill/list 即可访问。
