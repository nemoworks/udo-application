# UDO Application

这是UDO(Universal Digital Object)资源管理平台的后端部分实现，负责UDO模型及实例的管理，以及支撑UDO之间的协同交互过程。

web前端部分：https://github.com/nemoworks/udo-board 

IOS端：https://github.com/nemoworks/udo-iosapp

系统演示：https://www.bilibili.com/video/BV1yq4y1H7wx?share_source=copy_web

##项目结构
```
.
├── README.md
├── pom.xml
├── mosquitto-deploy              部署mqtt server
 ├── manifests.yaml
│   └── mosquitto
├── udo-api-graphql               graphql api生成udo管理接口
|
├── udo-api-rest                  restful api支持udo交互
|
├── udo-core                      udo模型定义
|
├── udo-messaging                 udo协同通讯模块
|
├── udo-poc                       udo验证实例
|
├── udo-storage-elasticsearch     udo elasticsearch存储支持
|
└── udo-storage-jpa               udo jpa存储支持

```
## 项目启动

- 本地启动数据库镜像
  ```
    docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.9.2
  ```
  
- 运行mqtt server

    ```
     参考mosquitto-deploy目录
    ```

- 启动本项目
    ```
      mvn spring-boot:run
    ```

## 演示示例

