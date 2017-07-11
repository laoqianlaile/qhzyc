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

set/p dbname=���������ݿ�ʵ���������س�: 
set/p username=������DBA�û����Ʋ����س���
set/p password=������DBA�û����벢���س���
set/p newusername=������Ҫ������ϵͳ����ƽ̨�û����Ʋ����س���
set/p newpassword=������Ҫ������ϵͳ����ƽ̨�û����벢���س���
pause
sqlplus %username%/%password%@%dbname% @db_patch.sql %username% %password% %dbname% %newusername% %newpassword% 
pause

set/p newauthusername=������Ҫ������ϵͳ����ƽ̨�û����Ʋ����س���
set/p newauthpassword=������Ҫ������ϵͳ����ƽ̨�û����벢���س���
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