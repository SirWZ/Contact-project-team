# -*- coding: utf-8 -*-
"""
Created on Tue Jan 15 10:42:17 2019

@author: swy
"""


from flask import Flask,request,render_template,redirect,url_for
import glob
import os
from snownlp import SnowNLP
from openpyxl import Workbook,load_workbook
from gevent import monkey
monkey.patch_all()
from gevent import pywsgi
import sys
# reload(sys)
# sys.setdefaultencoding('utf-8')
from jpype import *

startJVM(getDefaultJVMPath(), "-Djava.class.path=/home/gpu3/SDFlask/hanlp/hanlp-1.6.8.jar:/home/gpu3/SDFlask/hanlp","-Xms4g","-Xmx4g")



def parse(content,keywords):
    wordlist=keywords.decode('gbk').split(u',')   #python 2.7形式
    # wordlist=words.split(u',')    #python 3.5形式
    conlist=content.replace('\n','').replace(' ','').split('\xe3\x80\x82')
    HanLP = JClass('com.hankcs.hanlp.HanLP')
    allwords=[]
    sentences=[]
    for fword in conlist:
        if len(fword)>0:
            swords = HanLP.extractPhrase(HanLP.convertToSimplifiedChinese(fword),10000) #以词组进行拆分
            allwords.append(swords) #保留原话内容
            sentences.append(fword) #保留拆分后的内容  这里形成的两个数列具有一一对应关系
    wordall=[]
    for line in allwords:
        words=[]
        for word in line:
            word="".join(word)
            words.append(word)
        wordall.append(words) 
    ss = SnowNLP(wordall)       ##ss.sim 进行全匹配   传入参数conlist：有空格无法匹配
    simlist=ss.sim(wordlist) #根据指标对应的关键词词组进行权重计算 结果是一个数列，每个元素与上边生成的两个数列都是一一对应的关系
    conlist=[]
    if max(simlist)>0:
        for ind in range(len(simlist)):
            if simlist[ind]>0:
                shan=SnowNLP(sentences[ind].decode('utf8'))
                sshan=shan.han #繁体转简体
                conlist.append((sshan,simlist[ind])) #提取权重大于0的语句
        sortconlist=sorted(conlist,key=lambda x:x[1],reverse=True) #根据权重排序，只取权重高的前三句话
        return sortconlist[:3]
    else:
        return 'None'