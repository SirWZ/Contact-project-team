# -*- coding: utf-8 -*-
from pyhanlp import *
from snownlp import SnowNLP

segment = HanLP.segment('今天开心了吗？')
print segment

def parse(content, keywords):
    wordlist = keywords.split(u',')  # python 2.7形式

    conlist = content.replace('\n', '').replace(' ', '').split('\xe3\x80\x82')

    allwords = []
    sentences = []
    for fword in conlist:
        if len(fword) > 0:
            swords = HanLP.segment(conlist)  # 以词组进行拆分
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


if __name__ == '__main__':
    l = parse(
        "25.审议并批准本公司董事及监事的薪酬，即：全体执行董事2005年度薪酬总额标准为人民币2,271,501元，包括基本薪酬、退休计划供款、绩效薪酬及各种社会保险；全体独立非执行董事2005年度薪酬总额标准为人民币1,200,000元人民币，全体监事2005年度薪酬总额标准为人民币1,397,307元，包括基本薪酬、退休计划供款、绩效薪酬及各种社会保险,并授权董事会决定每位董事及监事2005年的薪酬",
        "人民币")
    print l
