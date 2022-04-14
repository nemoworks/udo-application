# 部署MQTT
## 使用K8s部署(推荐)
``` shell
k create ns udo 
k apply -f manifests.yaml
k get svc -n udo # 查看服务
```
## 使用Docker镜像部署
### 拉取镜像

``` shell
sudo docker pull eclipse-mosquitto:2.0.11
```
### 开启端口
``` shell
czh@xeon:~$ sudo ufw allow 1883
czh@xeon:~$ sudo ufw allow 1884
czh@xeon:~$ sudo ufw allow 9001
```

### 创建配置文件夹
``` shell
mkdir -p mosquitto/config mosquitto/data mosquitto/log mosquitto/passwd
```

### 编辑配置文件
在mosquito/config路径下，创建配置文件，并写入
``` shell
czh@xeon:~/mosquitto/config$ vim mosquitto.conf
persistence true
persistence_location /mosquitto/data
log_dest file /mosquitto/mosquitto.log
listener 1883
allow_anonymous false
password_file /mosquitto/passwd/passwd
# this will expect websockets connections
listener 1884
protocol websockets
```

### 启动容器
启动容器，并按照说明，挂载配置文件夹，持久化存储文件夹，以及log文件夹，并开启相应的端口

``` shell
sudo docker run -d -p 1883:1883 -p 1884:1884 -p 9001:9001 -v /home/czh/mosquitto/config/mosquitto.conf:/mosquitto/config/mosquitto.conf -v /home/czh/mosquitto/data:/mosquitto/data -v /home/czh/mosquitto/log:/mosquitto/log -v /home/czh/mosquitto/passwd:/mosquitto/passwd --name udo-mqtt --rm eclipse-mosquitto:2.0.11
```

### 进入容器
进入容器，创建用户，设置密码
``` shell
czh@xeon:~$ sudo docker exec -it udo-mqtt /bin/sh
/ # mosquitto_passwd -c mosquitto/passwd/passwd udo-user
Password: 
Reenter password: 
```

### 退出容器
进行测试
