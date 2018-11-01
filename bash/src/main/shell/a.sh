#!/usr/bin/env bash
echo "asd"
cygwin=false
case "`uname`" in
*) cygwin=true
esac
echo $cygwin

THIS="$0"
while [ -h "$THIS" ]; do
  ls=`ls -ld "$THIS"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '.*/.*' > /dev/null; then
    THIS="$link"
  else
    THIS=`dirname "$THIS"`/"$link"
  fi
done

echo $link
echo $THIS
a="asd"
a="$a:zxczxc"

echo $a
a=false

