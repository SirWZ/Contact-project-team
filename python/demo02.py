# -*- coding: utf-8 -*-
import requests
import pymssql
from urlparse import *
import uuid

conn = pymssql.connect('10.101.7.128', 'sa', 'Password01!', 'ZhongYan_SustainableDevelpoment')
cursor = conn.cursor()

from flask import Flask, request, render_template, redirect, url_for
import glob
import os
from snownlp import SnowNLP
from openpyxl import Workbook, load_workbook
from gevent import monkey

monkey.patch_all()
from gevent import pywsgi
import sys

reload(sys)
sys.setdefaultencoding('utf-8')
from jpype import *

startJVM(getDefaultJVMPath(), "-Djava.class.path=/home/gpu3/SDFlask/hanlp/hanlp-1.6.8.jar:/home/gpu3/SDFlask/hanlp",
         "-Xms4g", "-Xmx4g")


def parse(content, keywords):
    wordlist = keywords.split(u',')  # python 2.7形式
    # wordlist=words.split(u',')    #python 3.5形式
    conlist = content.replace('\n', '').replace(' ', '').split('\xe3\x80\x82')
    HanLP = JClass('com.hankcs.hanlp.HanLP')
    allwords = []
    sentences = []
    for fword in conlist:
        if len(fword) > 0:
            swords = HanLP.extractPhrase(HanLP.convertToSimplifiedChinese(fword), 10000)  # 以词组进行拆分
            allwords.append(swords)  # 保留原话内容
            sentences.append(fword)  # 保留拆分后的内容  这里形成的两个数列具有一一对应关系
    wordall = []
    for line in allwords:
        words = []
        for word in line:
            word = "".join(word)
            words.append(word)
        wordall.append(words)
    if len(wordall) == 0:
        return 'None'
    ss = SnowNLP(wordall)  ##ss.sim 进行全匹配   传入参数conlist：有空格无法匹配
    simlist = ss.sim(wordlist)  # 根据指标对应的关键词词组进行权重计算 结果是一个数列，每个元素与上边生成的两个数列都是一一对应的关系
    conlist = []
    if max(simlist) > 0:
        for ind in range(len(simlist)):
            if simlist[ind] > 0:
                shan = SnowNLP(sentences[ind].decode('utf8'))
                sshan = shan.han  # 繁体转简体
                conlist.append((sshan, simlist[ind]))  # 提取权重大于0的语句
        sortconlist = sorted(conlist, key=lambda x: x[1], reverse=True)  # 根据权重排序，只取权重高的前三句话
        return sortconlist[:3]
    else:
        return 'None'


def solrGet(year, hosts):
    startrow = 0
    rows = 100
    result = []
    filter = "&fq=host:"
    filter = filter + " OR host:".join(hosts)
    parameter = "http://10.101.240.84:8983/solr/sustainableDevelpoment/select?sort=boost ASC&q=*:*&start=%d&rows=%d" + filter
    url = parameter % (startrow, rows)
    solr = requests.get(url)
    json = solr.json()
    result.append(json)
    length = json['response']['numFound']
    for startrow in range(rows, length, rows):
        url = parameter % (startrow, rows)
        json = requests.get(url).json()
        result.append(json)
    return result


def getHost(company):
    dict = {}
    company = "".join(company)
    cursor.execute("SELECT [URL],[SOURCE_ID] FROM [dbo].[View_COMPANY_SOURCE] where [COMPANY_ID] ='" + company + "'")
    row = cursor.fetchone()
    # print('cursor.........')
    # print(len(row))
    while row:
        ym = urlparse(row[0]).netloc
        dict[ym] = row[1]
        row = cursor.fetchone()
    return dict


def getALLCompany():
    cursor.execute("SELECT COMPANY_ID FROM [dbo].[View_COMPANY_SOURCE]")
    data = cursor.fetchall()
    ids = []
    for item in data:
        item = "".join(item)
        ids.append(item)
    return ids


def getIndexData():
    source = []
    sql = """
    SELECT
	    INDEX_ID,
	    BASE_DATA_ID,
	    KEYWORDS
    FROM
	    [dbo].[View_INDEX]
    WHERE
	    KEYWORDS IS NOT NULL
    """
    cursor.execute(sql)
    row = cursor.fetchone()
    while row:
        keyword = (row[0], row[1], row[2])
        source.append(keyword)
        row = cursor.fetchone()
    return source


def insertParseHistory(parseHistoryId, year, companyId):
    cursor.execute(
        "insert into  [dbo].[TB_CRAWL_PARSE_HISTORY](ID,YEAR,COMPANY_ID,STATUS,SUBMIT_USER_ID)VALUES('%s','%s','%s','%s','%s')" %
        (parseHistoryId, year, companyId, '2', '1')
    )
    conn.commit()
    print '插入解析历史表'


def insertParseResultBySQLServer(parseHistoryId, indexId, indexBaseId, data, sourceId, relation):
    cursor.execute(
        "exec PROC_INSERT_PARSE_RESULT @parse_id='%s',@index_id='%s',@index_base_id='%s',@data=N'%s',@source_id='%s',@relation=%s" %
        (parseHistoryId, indexId, indexBaseId, data, sourceId, relation)
    )
    conn.commit()
    print '调用过程，插入解析结果表'


ids = ['2E2FF329EE2A4A618BE3944BD912E413', 'F28926057AE14B2BB1FE0BD6A689CB01',
       '5C4438F99BA8476D8715E1A134CFB783''07808D29B5A6475996EF1E89CC7AB9D3', 'FC5552CC0FFF47C6A665B12925D19DCB',
       'D4D3FE9FBCA442B493D4D07A43503075', '445AEA7953D54BEA9AC196916FC92895', 'DE7FD7CE1AB04F60BD640AD97D155A97',
       '6E6B01C2E3004620B5945FCBF915E8B3', 'F6B87C361EE84B1BAF83E096E547A31F', '0C64B9933B6149DA829C603E3CB72EAC',
       '8D45C2250C874EACB7C603E80ACC51DC', 'F117C49456954E97A2B3873EE0775D3F', 'D1C3C4D0931740F298B3FDD94B5A74B4',
       '9ADBDAC0FD924E06881F813CF1A4A189', '160A966E56FF4CCE89297C9391EFE688', '8C0D93210BE24C37B9D898EFF31883FF',
       'F4106B2097384FB686E2159846648965', '0D8A951AAE2D4BC3AA473EBBF5C0DA36', 'E8561BB02F374DF28A549C503AE31124',
       '86CBF07DCA8642C094AF97EED1FC1B92', 'D6CD20D86A10495487B402CC0C9B3E31', 'DC2F8D5ACFB54BEF9F6DA5E4C20E0D1B',
       '215EA1EE963645AC81152190D82CDC29']


def func():
    print(len(ids))
    index_data = getIndexData()
    for companyid in ids:

        uid = str(uuid.uuid4())

        insertParseHistory(uid, '2018', companyid)

        hostdict = getHost(companyid)

        hosts = list(hostdict.keys())
        # 获取solr 数据
        datas = solrGet('', hosts)
        for data in datas:
            for document in data['response']['docs']:
                # 待解析数据
                content = document['content']
                source_id = document['host'][0]
                if source_id in hostdict:
                    source_id = hostdict[source_id]

                for index in index_data:
                    (indexId, indexBaseId, keywords) = index
                    content = "".join(content)
                    #  keywords = "".join(keywords)
                    parseResult = parse(content, keywords)
                    if len(parseResult) != 4:
                        for result in parseResult:
                            resultdata = result[0]
                            relationdata = result[1]
                            print(result[0])
                            print(result[1])


                            insertParseResultBySQLServer(uid, indexId, indexBaseId, resultdata, source_id,relationdata)

                            print('插入》》》》》》》》》》》》》》》')


if __name__ == '__main__':
    func()
