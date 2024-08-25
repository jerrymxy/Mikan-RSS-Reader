# Mikan-RSS-Reader
根据蜜柑计划Mikan Project的RSS获取磁力地址

## 环境要求
目标JDK版本为17，但是应该JDK11、1.8也能用？
jar包是使用JDK21编译的

## 使用方法
使用Java打开jar包，输入蜜柑计划的RSS地址，按照提示操作。

1 - 获取磁力链接

2 - 下载种子文件

文件会下载到jar包所在的目录下。

2024.8更新：支持过滤关键字

或者也可以直接用命令行

```shell
java -jar Mikan_RSS_Reader.jar [url] [option] [pattern]
```

不知道什么是RSS？看图：

![Screenshot 2024-03-31 123135.png](assets/Screenshot%202024-03-31%20123135.png)
