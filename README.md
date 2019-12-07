# AMoeBa-Tools

#### 一、项目结构

- AMoeBa_Server (后端应用代码)

- AMoeBa_UI (前端应用代码)

#### 二、项目主要技术

> ##### 前端主要技术

- Angular

- Angular Material

> ##### 后端主要技术

- Spring Boot

- SQLite

#### 三、项目概述

> ##### 目的

- 本项目开发的目的是为了解决大部分sinosoft同事，日常工作繁忙，经常忘记填写AMoeBa日志，每到月底补日志十分痛苦的惨况

- 系统提供批量删除以及批量填写日志的功能，批量填写时若系统数据库已维护当年的法定节假日，则会自动跳过

> ##### 说明

- 本人已经离职，项目问题可能无法及时处理。如有同事维护修改bug，可以拉取分支后创建合并请求，我会合并到master

- 此项目已经部署在阿里云服务器上，地址 [http://www.ioally.com/amoeba/](http://www.ioally.com/amoeba/) 云服务器配置比较低，希望大家使用的同时尽量的低调，切勿奔走相告，当云服务版本30天之内有超过两次回话池使用率达到80%将自动开启秘钥授权机制，不允许无授权用户使用。

- 秘钥功能以及bug修复将于近期合并至master（2019-12）

> ##### 前端

- 前端采用Google Angular Material 设计方案实现简约的页面风格

- 前端页面所有图标均来自  [Google Icons](https://material.io/tools/icons/)  开源图标库，不涉及任何版权风险

- 前端使用Nginx部署服务

> ##### 后端

- 主要使用Spring Boot实现后端服务

- 数据存储选择了较为轻量级的SQLite方案

#### 四、项目主要配置描述

> ##### 前端

- 前端主要是nginx反向代理的配置，参考以下内容

  ```
  
  #user  nobody;
  worker_processes  1;
  
  events {
      worker_connections  1024;
  }
  
  http {
      include       mime.types;
      default_type  application/octet-stream;
      sendfile        on;
      keepalive_timeout  65;
  
      server {
          listen       8888;
          server_name  localhost;
  
          location /amoeba {
              alias  ~/AMoeBa/AMoeBa_UI;
              index  index.html index.htm;
          }
  
          location /amoebaApi{
              proxy_pass http://amoebaApiUrl/amoeba;
              access_log "logs/amoebaApi.log";
          }
  
          error_page   500 502 503 504  /50x.html;
          location = /50x.html {
              root   html;
          }
  
      }
  
      upstream amoebaApiUrl {
              server localhost:9908;
      }
  
      include servers/*;
  }
  ```

> ##### 后端

```yml
# 日志相关配置
logging.path # log日志存储路径

# AMoeBa相关基础配置
amoeba.url # 登录AMoeBa的地址
amoeba.sessionSize # 可以提供服务的session数量
amoeba.sessionTimeout # session无操作的超时时间（单位：分钟）
amoeba.isVerifyKey # 登录时是否需要验证密钥信息
amoeba.multiThread # 是否开启多线程提交任务, 批量操作时会新创建线程执行任务

# 安全相关配置
amoeba.security.aesKey # 生成密钥时，加密算法的私钥串
amoeba.security.validTime # 生成的密钥有效时间（单位：天）
amoeba.security.dateStrategy # 密钥生成的策略，即密钥串中包含的时间戳(cutoff-截止日期，begin-开始日期)
amoeba.security.switchFlag # 密钥生成的开关控制
amoeba.security.sendEmail # 生成密钥时，是否直接发送邮件给用户

# sqlite数据库相关配置
sqliteDataBsae.driver-class-name # 数据库驱动名，默认就好
sqliteDataBsae.dataFilePath # 数据库文件路径，建议填写绝对路径，避免载入出错
```

#### 五、数据结构

> ##### 用户信息表（UserInfo）

| 字段名       | 字段类型    | 是否可以为空 | 是否主键 | 默认值 | 字段注释             |
| --------- | ------- | ------ | ---- | --- | ---------------- |
| id        | VARCHAR | N      | Y    | 无   | 用户工号             |
| userName  | VARCHAR | N      | N    | 无   | 用户姓名             |
| passWord  | VARCHAR | N      | N    | 无   | 用户的AMoeBa系统唯一id，不是密码  |
| vaildFlag | VARCHAR | N      | N    | 1   | 是否有效标志。1-有效；0-无效 |
| role      | VARCHAR | N      | N    | 9   | 用户角色代码           |
| login     | INTEGER | N      | N    | 1   | 用户的登录次数         |
| lastLogin | VARCHAR | Y      | N    | 无  | 用户的最后一次登录时间         |

> ##### 角色信息表(RoleInfo)

| 字段名       | 字段类型    | 是否可以为空 | 是否主键 | 默认值 | 字段注释             |
| --------- | ------- | ------ | ---- | --- | ---------------- |
| roleCode  | VARCHAR | N      | Y    | 无   | 角色代码             |
| roleName  | VARCHAR | N      | N    | 无   | 角色名称             |
| vaildFlag | VARCHAR | N      | N    | 1   | 是否有效标志。1-有效；0-无效 |

> ##### 系统菜单信息(SysMenu)

| 字段名       | 字段类型    | 是否可以为空 | 是否主键 | 默认值 | 字段注释                   |
| --------- | ------- | ------ | ---- | --- | ---------------------- |
| role      | VARCHAR | N      | Y    | 无   | 角色代码                   |
| menuId    | INTEGER | N      | Y    | 无   | 菜单id                   |
| menuName  | VARCHAR | N      | N    | 无   | 菜单名称                   |
| targetUrl | VARCHAR | N      | N    | 无   | 菜单指向的目标地址              |
| menuIcon  | VARCHAR | N      | N    | ''  | 菜单对应的图标，参见Google Icons |
| vaildFlag | VARCHAR | N      | N    | 1   | 是否有效标志。1-有效；0-无效       |

> ##### 节假日主表(HolidayMain)

| 字段名             | 字段类型    | 是否可以为空 | 是否主键 | 默认值 | 字段注释             |
| --------------- | ------- | ------ | ---- | --- | ---------------- |
| holidayId       | INTEGER | N      | Y    | 无   | 假期id（年份+月份+2位序号） |
| holidayYear     | INTEGER | N      | Y    | 无   | 假期所在年度           |
| holidayName     | VARCHAR | N      | N    | 无   | 假期名称             |
| holidayStrategy | VARCHAR | N      | N    | 无   | 假期调休策略           |
| holidayDays     | INTEGER | N      | N    | 无   | 放假天数             |

> ##### 节假日安排表(HolidayPlan)

| 字段名         | 字段类型    | 是否可以为空 | 是否主键 | 默认值 | 字段注释             |
| ----------- | ------- | ------ | ---- | --- | ---------------- |
| holidayId   | INTEGER | N      | Y    | 无   | 假期id（年份+月份+2位序号） |
| holidayDate | DATE    | N      | Y    | 无   | 假期休息日期           |

> ##### 反馈信息表(FeedBack)

| 字段名        | 字段类型    | 是否可以为空 | 是否主键 | 默认值 | 字段注释               |
| ---------- | ------- | ------ | ---- | --- | ------------------ |
| id         | VARCHAR | N      | Y    | 无   | 反馈信息的id（32位的uuid）  |
| content    | VARCHAR | N      | N    | 无   | 反馈信息的内容            |
| createBy   | VARCHAR | N      | N    | 无   | 反馈人名称              |
| createTime | VARCHAR | N      | N    | 无   | 反馈时间               |
| email      | VARCHAR | N      | N    | 无   | 反馈人邮箱              |
| isSendFlag | VARCHAR | N      | N    | 0   | 是否已经发送邮件。1-已发；0-未发 |

> ##### 密钥信息表(KeyInfo)

| 字段名        | 字段类型    | 是否可以为空 | 是否主键 | 默认值 | 字段注释               |
| ---------- | ------- | ------ | ---- | --- | ------------------ |
| userId     | VARCHAR | N      | Y    | 无   | 用户等登录id  |
| key        | VARCHAR | N      | N    | ''   | 用户的对应密钥            |
| privateKey | VARCHAR | N      | N    | 无   | 用来给密钥加密的私钥       |
