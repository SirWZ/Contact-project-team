#!/usr/bin/env bash
sum=123000
echo `cat /etc/hosts | awk -v sum=$sum '{sum+=1}END{print sum}'`
echo `ps -aux | awk -v sum=$sum '{sum=$1}END{print sum}'`
echo sum

echo sum