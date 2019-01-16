# -*- coding: utf-8 -*-
import requests

import pymssql
from urlparse import *

from flask import Flask, request

conn = pymssql.connect('10.101.7.128', 'sa', 'Password01!', 'ZhongYan_SustainableDevelpoment')
cursor = conn.cursor()

app = Flask(__name__)


def solrGet(year, hosts):
    startrow = 0
    rows = 100
    result = []

    filter = "&fq=host:"
    filter = filter + " OR host:".join(hosts)

    parameter = "http://spider01:8983/solr/sustainableDevelpoment/select?sort=boost ASC&q=*:*&start=%d&rows=%d" + filter
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


def getHost(company, sourceId):
    dict = {}
    cursor.execute("SELECT URL,ID FROM [dbo].[View_COMPANY_SOURCE] where COMPANY_ID = '" + company + "'")
    row = cursor.fetchone()
    while row:
        if sourceId != None:
            if row[1] in sourceId:
                ym = urlparse(row[0]).netloc
                dict[ym] = row[1]
        else:
            ym = urlparse(row[0]).netloc
            dict[ym] = row[1]
        row = cursor.fetchone()
    return dict


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


def insertParseResultBySQLServer(parseHistoryId, indexId, indexBaseId, data, sourceId, relation):
    cursor.execute(
        "exec PROC_INSERT_PARSE_RESULT @parse_id='%s',@index_id='%s',@index_base_id='%s',@data=N'%s',@source_id='%s',@relation=%s" %
        (parseHistoryId, indexId, indexBaseId, data, sourceId, relation)
    )
    conn.commit()


def updateParseStatus(parseId, year, statusCode):
    cursor.execute("UPDATE [dbo].[TB_CRAWL_PARSE_HISTORY] SET status = %d WHERE	ID = '%s' AND YEAR = '%s' " % (
        statusCode, parseId, year))
    conn.commit()


@app.route('/', methods=['POST'])
def hello_world():
    request_json = request.get_json()
    year = None
    company = None
    sources = None
    parseHistoryId = None
    if 'year' in request_json:
        year = request_json['year']
    else:
        return 'Missing parameter year', 500

    if 'company' in request_json:
        company = request_json['company']
    else:
        return 'Missing parameter company', 500

    if 'parseHistoryId' in request_json:
        parseHistoryId = request_json['parseHistoryId']
    else:
        return 'Missing parameter parseHistoryId', 500

    if 'sources' in request_json:
        sources = request_json['sources']

    updateParseStatus(parseHistoryId, year, 1)

    # 获取host
    hostdict = getHost(company, sources)
    hosts = list(hostdict.keys())

    # 获取solr 数据
    datas = solrGet(year, hosts)

    index_data = getIndexData()
    for data in datas:
        for document in data['response']['docs']:
            # 待解析数据
            content = document['content']
            source_id = document['host'][0]
            if source_id in hostdict:
                source_id = hostdict[source_id]

            for index in index_data:
                (indexId, indexBaseId, keywords) = index
                # parseResult = parse(content, keywords)
                print(content)

    updateParseStatus(parseHistoryId, year, 2)

    return "OK"


if __name__ == '__main__':
    app.debug = True
    app.run()
