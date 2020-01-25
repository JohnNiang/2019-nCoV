# 数据来源

<https://3g.dxy.cn/newh5/view/pneumonia>

# 配置文件

程序可读取外部配置文件，这里默认读取 `~/.ncov/application.yml`。

```yml
spring:
  mail:
    # 邮箱发送方配置
    host: smtp.qq.com
    username: xxx@foxmail.com
    password: openyouremail
    protocol: smtp
    properties.mail.smtp.auth: true
    properties.mail.smtp.port: 465
    properties.mail.smtp.starttls.enable: true
    properties.mail.smtp.starttls.required: true
    properties.mail.smtp.ssl.enable: true
    properties.mail.display.sendname: JohnNiang
    default-encoding: utf-8
ncov:
  # 邮箱接收方地址设置
  to:
    - xxx@xxx.com # 这里填写你需要接收消息的邮箱地址
    - yyy@yyy.com # 同上
  cron: 0/30 * * * * ? # 轮询数据的计划
```

# 项目来源

<https://github.com/NGLSL/mail>
