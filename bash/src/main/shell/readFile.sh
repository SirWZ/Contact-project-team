#!/usr/bin/env bash
filePath='/home/sun/block.txt'
h=""
echo "reading $filePath"
while read line
    do
        echo $line
        h=$h"$line"

done < /etc/hosts

echo ${#h}
echo ${h:1:4}

a=(1 2 3 4 5)
echo ${#a[*]}

echo $?

a=20
b=20
if [ $a == $b ]
then
    echo "eq"
else
    echo "No eq"
fi

if [ $a -gt $b ]
then
echo "-eq"
else
echo "-ne"
fi

if [ -f $filePath ]
    then
    echo "asd"
fi

i=1
while(( $i <= 10 ))
do
    echo $i
    let "i++"
done

#while read FILM
#do
#    echo "yes ${read} is verygood"
#done
read FILM
case $FILM in
1)
    echo "1"
    ;;
2)
    echo "2"
    ;;
esac
