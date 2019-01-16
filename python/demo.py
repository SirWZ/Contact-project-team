import requests
import pymssql
from urlparse import *


def getHost():
    response = requests.get(
        'http://spider02:8983/solr/sustainableDevelpoment/select?facet.field=host&facet=on&q=*:*').json()
    hosts = response['facet_counts']['facet_fields']['host']
    return hosts


def getCompanyId(l):
    list = []
    conn = pymssql.connect('10.101.7.128', 'sa', 'Password01!', 'ZhongYan_SustainableDevelpoment')
    cursor = conn.cursor()
    cursor.execute('SELECT COMPANY_ID,URL FROM [dbo].[View_COMPANY_SOURCE];')
    row = cursor.fetchone()
    while row:
        print row[1]
        hostByCompany = urlparse(row[1]).netloc
        id = row[0]
        if str(hostByCompany) in l:
            list.append(id)
        row = cursor.fetchone()
    return list


if __name__ == '__main__':
    host = getHost()
    l = []
    for i in range(0, len(host), 2):
        l.append(host[i])
    company_id = getCompanyId(l)

    for id in company_id:
        print id
