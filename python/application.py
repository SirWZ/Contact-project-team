
import requests

import pymssql
from urlparse import *

from flask import Flask, request

from snownlp import SnowNLP

conn = pymssql.connect('10.101.7.128', 'sa', 'Password01!', 'ZhongYan_SustainableDevelpoment')
cursor = conn.cursor()

from pyhanlp import *

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


def parse(content, keywords):
    startJVM(getDefaultJVMPath(), "-Djava.class.path=/home/gpu3/SDFlask/hanlp/hanlp-1.6.8.jar:/home/gpu3/SDFlask/hanlp",
             "-Xms512m", "-Xmx1g")
    wordlist = keywords.decode('gbk').split(u',')  # python 2.7形式
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
