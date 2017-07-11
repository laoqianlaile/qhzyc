@echo off
chcp 936

iconv -c -f utf-8 -t gb2312 1_table_create.sql > 1_table_create_ansi.sql
iconv -c -f utf-8 -t gb2312 2_table_coflow.sql > 2_table_coflow_ansi.sql
iconv -c -f utf-8 -t gb2312 3_init_function.sql > 3_init_function_ansi.sql
iconv -c -f utf-8 -t gb2312 4_init_procedure.sql > 4_init_procedure_ansi.sql
iconv -c -f utf-8 -t gb2312 5_table_index.sql > 5_table_index_ansi.sql
iconv -c -f utf-8 -t gb2312 6_component_table.sql > 6_component_table_ansi.sql
iconv -c -f utf-8 -t gb2312 7_selfdefine_table.sql > 7_selfdefine_table_ansi.sql
iconv -c -f utf-8 -t gb2312 8_init_data.sql > 8_init_data_ansi.sql
iconv -c -f utf-8 -t gb2312 auth/1_auth35.sql > auth/1_auth35_ansi.sql
iconv -c -f utf-8 -t gb2312 auth/2_auth_init_data.sql > auth/2_auth_init_data_ansi.sql
pause

set/p dbname=请输入数据库实例名并按回车: 
set/p username=请输入DBA用户名称并按回车：
set/p password=请输入DBA用户密码并按回车：
set/p newusername=请输入要创建的系统配置平台用户名称并按回车：
set/p newpassword=请输入要创建的系统配置平台用户密码并按回车：
pause
sqlplus %username%/%password%@%dbname% @db_patch.sql %username% %password% %dbname% %newusername% %newpassword% 
pause

set/p newauthusername=请输入要创建的系统管理平台用户名称并按回车：
set/p newauthpassword=请输入要创建的系统管理平台用户密码并按回车：
pause
sqlplus %username%/%password%@%dbname% @auth/auth_db_patch.sql %username% %password% %dbname% %newauthusername% %newauthpassword% 
pause

del 1_table_create_ansi.sql
del 2_table_coflow_ansi.sql
del 3_init_function_ansi.sql
del 4_init_procedure_ansi.sql
del 5_table_index_ansi.sql
del 6_component_table_ansi.sql
del 7_selfdefine_table_ansi.sql
del 8_init_data_ansi.sql
cd auth
del 1_auth35_ansi.sql
del 2_auth_init_data_ansi.sql

pause