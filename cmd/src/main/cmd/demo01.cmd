@echo off

echo wscript.echo dateadd("d",-7,now())>x.vbs
for /f %%i in ('cscript /nologo x.vbs') do set d=%%i
del x.vbs

echo %d%


set pic_dir=C:\software\test\*.*
set filePrefix=ora_smes30_vis_rpt_mescomm_hsmes_hxcs_mes_
set fileShufix=.dmp

for /f %%i in ('dir /s /b %pic_dir%') do (
	set str_path=%%~ni
	echo %str_path%

)

::if "12" lss "4" (echo 12竟然小于4哦?) else echo 12当然不会小于4的！