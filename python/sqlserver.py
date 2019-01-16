# -*- coding: utf-8 -*-
import pymssql
import sys
import os
from urlparse import *

reload(sys)
sys.setdefaultencoding('utf8')
os.environ['NLS_LANG'] = 'SIMPLIFIED CHINESE_CHINA.UTF8'

conn = pymssql.connect('10.101.7.128', 'sa', 'Password01!', 'ZhongYan_SustainableDevelpoment', charset='utf8')
cursor = conn.cursor()
# sql = "exec PROC_INSERT_PARSE_RESULT @parse_id='%s',@index_id='%s',@index_base_id='%s',@data=N'%s',@source_id='%s',@relation=%s" % ('9FFE6E7D56A543ABA612B63203AEB9F1', 'FD306F8D6C04409F8EA9D780C30FD5B8', '00A14D4BAE92CA443FAAC16A8B171BC', '阿斯顿自行车阿斯顿现在','02647911-2DCF-4086-94AB-658934B84663', '2.13221')
#
# # sql = "insert into TB_CRAWL_RESULT(ID,PARSE_HISTORY_ID,SADP_INDEX_ID,SADP_INDEX_BASE_DATA_ID,DATA) values ('%s','%s','%s','%s',N'%s')" % (str(uuid.uuid4()),str(uuid.uuid4()),str(uuid.uuid4()),str(uuid.uuid4()),'啊实打实大苏打')
# cursor.execute(sql)
#
# conn.commit()


cursor.execute("SELECT URL,ID FROM [dbo].[View_COMPANY_SOURCE]")
row = cursor.fetchone()
while row:
    ym = urlparse(row[0]).netloc
    print ym
    row = cursor.fetchone()
