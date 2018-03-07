@echo off
color A
echo 前台页面开始启动
D:
cd D:\apache-tomcat-7.0.56\webapps\wcsweb
npm start

echo 启动完成 & pause >nul