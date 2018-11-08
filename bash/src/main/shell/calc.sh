#!/bin/bash

read -p "请输入第一个数字" x
read -p "请输入第二个数字" y
read -p "请输入运算符号" f

#最外层判断，判断输入的是否为空
# -n 判断 是否为非空
# -a 多重条件判断 与
if [ -n "$x" -a -n "$y" -a -n "$f" ]
        then
# 下面这个表达式用于判断输入的两个字符是否完全为数字，反引号 和 $()为同一个作用，将系统命令的值赋给变量，原理为，输出变量x的值，并利用管道符，将该值进行字符串替换， sed "s/旧字符串/新字符串/g"，末尾g表示将指定范围内的所有旧字符串都替换，所以虽然[0-9]表示匹配一个字符，加了g以后，会替换所有字符。
                test1=`echo "$x" | sed "s/[0-9]//g"`
                test2=$(echo "$y" | sed "s/[0-9]//g")
#中间的判断，判断输入的两个值是否为数字
            if [ -z "$test1" -a -z "$test2" ]
                    then
# 最内层判断，判断是什么运算符号
                            if [ "$f" == "+" ]
                                    then
                                    echo "${x}和${y}的和是"$[$x+$y]
                            elif [ "$f" == "-" ]
                                    then
                                    echo "${x}和${y}的差是"$[$x-$y]
                            elif [ "$f" == "*" ]
                                    then
                                    echo "${x}和${y}的积是"$[$x*$y]
                            elif [ "$f" == "/" ]
                                    then
                                        echo "${x}和${y}的商是"$[$x/$y]
                            fi
            else
                    echo "您输入的不是数字，重新执行脚本"
                    bash jisuanqi.sh
                    exit 2
            fi
    else
            echo "您没有输入数字，重新执行脚本"
            exit 1
fi