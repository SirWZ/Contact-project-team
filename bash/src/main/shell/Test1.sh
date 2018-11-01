#!/usr/bin/env bash
echo "Hello Word"

strs=("d" "b" "a")
for i in ${strs[*]} ;do
echo "I Am $i"
done

echo $1
shift
echo $1

echo $*

echo $@

echo $$

echo `date`

if [ $(ps -ef | grep -c "ssh") -lt 1 ]; then echo "true"; fi


a=10
b=11

if [ $a == $b ]
then
    echo "equels"
   else
    echo "no" ;
fi

