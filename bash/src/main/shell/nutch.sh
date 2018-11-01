#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# 
# The Nutch command script
#
# Environment Variables
#
#   NUTCH_JAVA_HOME The java implementation to use.  Overrides JAVA_HOME.
#
#   NUTCH_HEAPSIZE  The maximum amount of heap to use, in MB. 
#                   Default is 1000.
#
#   NUTCH_OPTS      Extra Java runtime options.
#                   Multiple options must be separated by white space.
#
#   NUTCH_LOG_DIR   Log directory (default: $NUTCH_HOME/logs)
#
#   NUTCH_LOGFILE   Log file (default: hadoop.log)
#
#   NUTCH_CONF_DIR  Path(s) to configuration files (default: $NUTCH_HOME/conf).
#                   Multiple paths must be separated by a colon ':'.
#
cygwin=false
case "`uname`" in
CYGWIN*) cygwin=true;;
esac

# resolve links - $0 may be a softlink
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

# if no args specified, show usage
if [ $# = 0 ]; then
  echo "nutch 1.15"
  echo "Usage: nutch COMMAND"
  echo "where COMMAND is one of:"
  echo "  readdb            read / dump crawl db"
  echo "  mergedb           merge crawldb-s, with optional filtering"
  echo "  readlinkdb        read / dump link db"
  echo "  inject            inject new urls into the database"
  echo "  generate          generate new segments to fetch from crawl db"
  echo "  freegen           generate new segments to fetch from text files"
  echo "  fetch             fetch a segment's pages"
  echo "  parse             parse a segment's pages"
  echo "  readseg           read / dump segment data"
  echo "  mergesegs         merge several segments, with optional filtering and slicing"
  echo "  updatedb          update crawl db from segments after fetching"
  echo "  invertlinks       create a linkdb from parsed segments"
  echo "  mergelinkdb       merge linkdb-s, with optional filtering"
  echo "  index             run the plugin-based indexer on parsed segments and linkdb"
  echo "  dedup             deduplicate entries in the crawldb and give them a special status"
  echo "  dump              exports crawled data from segments into files"
  echo "  commoncrawldump   exports crawled data from segments into common crawl data format encoded as CBOR"
  echo "  solrindex         run the solr indexer on parsed segments and linkdb - DEPRECATED use the index command instead"
  echo "  solrdedup         remove duplicates from solr - DEPRECATED use the dedup command instead"
  echo "  solrclean         remove HTTP 301 and 404 documents from solr - DEPRECATED use the clean command instead"
  echo "  clean             remove HTTP 301 and 404 documents and duplicates from indexing backends configured via plugins"
  echo "  parsechecker      check the parser for a given url"
  echo "  indexchecker      check the indexing filters for a given url"
  echo "  filterchecker     check url filters for a given url"
  echo "  normalizerchecker check url normalizers for a given url"
  echo "  domainstats       calculate domain statistics from crawldb"
  echo "  protocolstats     calculate protocol status code stats from crawldb"
  echo "  crawlcomplete     calculate crawl completion stats from crawldb"
  echo "  webgraph          generate a web graph from existing segments"
  echo "  linkrank          run a link analysis program on the generated web graph"
  echo "  scoreupdater      updates the crawldb with linkrank scores"
  echo "  nodedumper        dumps the web graph's node scores"
  echo "  plugin            load a plugin and run one of its classes main()"
  echo "  junit             runs the given JUnit test"
  echo "  startserver       runs the Nutch Server on localhost:8081"
  echo "  startweb          runs the Nutch WebServer on localhost:8080"
  echo "  webapp            run a local Nutch Web Application on locahost:8080"
  echo "  warc              exports crawled data from segments at the WARC format"
  echo "  updatehostdb      update the host db with records from the crawl db"
  echo "  readhostdb        read / dump host db"
  echo "  sitemap           perform Sitemap processing"
  echo " or"
  echo "  CLASSNAME         run the class named CLASSNAME"
  echo "Most commands print help when invoked w/o parameters."
  exit 1
fi

# get arguments
COMMAND=$1
shift

# some directories
THIS_DIR="`dirname "$THIS"`"
NUTCH_HOME="`cd "$THIS_DIR/.." ; pwd`"

# some Java parameters
if [ "$NUTCH_JAVA_HOME" != "" ]; then
  #echo "run java in $NUTCH_JAVA_HOME"
  JAVA_HOME="$NUTCH_JAVA_HOME"
fi
  
if [ "$JAVA_HOME" = "" ]; then
  echo "Error: JAVA_HOME is not set."
  exit 1
fi

local=true

# NUTCH_JOB 
if [ -f "${NUTCH_HOME}"/*nutch*.job ]; then
  local=false
  for f in "$NUT
  CH_HOME"/*nutch*.job; do
    NUTCH_JOB="$f"
  done
  # cygwin path translation
  if $cygwin; then
	NUTCH_JOB="`cygpath -p -w "$NUTCH_JOB"`"
  fi
fi

JAVA="$JAVA_HOME/bin/java"
JAVA_HEAP_MAX=-Xmx1000m 

# check envvars which might override default args
if [ "$NUTCH_HEAPSIZE" != "" ]; then
  #echo "run with heapsize $NUTCH_HEAPSIZE"
  JAVA_HEAP_MAX="-Xmx""$NUTCH_HEAPSIZE""m"
  #echo $JAVA_HEAP_MAX
fi

# CLASSPATH initially contains $NUTCH_CONF_DIR, or defaults to $NUTCH_HOME/conf
CLASSPATH="${NUTCH_CONF_DIR:=$NUTCH_HOME/conf}"
CLASSPATH="${CLASSPATH}:$JAVA_HOME/lib/tools.jar"

# so that filenames w/ spaces are handled correctly in loops below
IFS=

# add libs to CLASSPATH
if $local; then
  for f in "$NUTCH_HOME"/lib/*.jar; do
   CLASSPATH="${CLASSPATH}:$f";
  done
  # local runtime
  # add plugins to classpath
  if [ -d "$NUTCH_HOME/plugins" ]; then
     CLASSPATH="${NUTCH_HOME}:${CLASSPATH}"
  fi
fi

# cygwin path translation
if $cygwin; then
  CLASSPATH="`cygpath -p -w "$CLASSPATH"`"
fi

# setup 'java.library.path' for native-hadoop code if necessary
# used only in local mode 
JAVA_LIBRARY_PATH=''
if [ -d "${NUTCH_HOME}/lib/native" ]; then

  JAVA_PLATFORM=`"${JAVA}" -classpath "$CLASSPATH" org.apache.hadoop.util.PlatformName | sed -e 's/ /_/g'`

  if [ -d "${NUTCH_HOME}/lib/native" ]; then
    if [ "x$JAVA_LIBRARY_PATH" != "x" ]; then
      JAVA_LIBRARY_PATH="${JAVA_LIBRARY_PATH}:${NUTCH_HOME}/lib/native/${JAVA_PLATFORM}"
    else
      JAVA_LIBRARY_PATH="${NUTCH_HOME}/lib/native/${JAVA_PLATFORM}"
    fi
  fi
fi

if [ $cygwin = true -a "X${JAVA_LIBRARY_PATH}" != "X" ]; then
  JAVA_LIBRARY_PATH="`cygpath -p -w "$JAVA_LIBRARY_PATH"`"
fi

# restore ordinary behaviour
unset IFS

# default log directory & file
if [ "$NUTCH_LOG_DIR" = "" ]; then
  NUTCH_LOG_DIR="$NUTCH_HOME/logs"
fi
if [ "$NUTCH_LOGFILE" = "" ]; then
  NUTCH_LOGFILE='hadoop.log'
fi

#Fix log path under cygwin
if $cygwin; then
  NUTCH_LOG_DIR="`cygpath -p -w "$NUTCH_LOG_DIR"`"
fi

NUTCH_OPTS=($NUTCH_OPTS -Dhadoop.log.dir="$NUTCH_LOG_DIR")
NUTCH_OPTS=("${NUTCH_OPTS[@]}" -Dhadoop.log.file="$NUTCH_LOGFILE")

if [ "x$JAVA_LIBRARY_PATH" != "x" ]; then
  NUTCH_OPTS=("${NUTCH_OPTS[@]}" -Djava.library.path="$JAVA_LIBRARY_PATH")
fi

# figure out which class to run
if [ "$COMMAND" = "crawl" ] ; then
  echo "Command $COMMAND is deprecated, please use bin/crawl instead"
  exit -1
elif [ "$COMMAND" = "inject" ] ; then
  CLASS=org.apache.nutch.crawl.Injector
elif [ "$COMMAND" = "generate" ] ; then
  CLASS=org.apache.nutch.crawl.Generator
elif [ "$COMMAND" = "freegen" ] ; then
  CLASS=org.apache.nutch.tools.FreeGenerator
elif [ "$COMMAND" = "fetch" ] ; then
  CLASS=org.apache.nutch.fetcher.Fetcher
elif [ "$COMMAND" = "parse" ] ; then
  CLASS=org.apache.nutch.parse.ParseSegment
elif [ "$COMMAND" = "readdb" ] ; then
  CLASS=org.apache.nutch.crawl.CrawlDbReader
elif [ "$COMMAND" = "mergedb" ] ; then
  CLASS=org.apache.nutch.crawl.CrawlDbMerger
elif [ "$COMMAND" = "readlinkdb" ] ; then
  CLASS=org.apache.nutch.crawl.LinkDbReader
elif [ "$COMMAND" = "readseg" ] ; then
  CLASS=org.apache.nutch.segment.SegmentReader
elif [ "$COMMAND" = "mergesegs" ] ; then
  CLASS=org.apache.nutch.segment.SegmentMerger
elif [ "$COMMAND" = "updatedb" ] ; then
  CLASS=org.apache.nutch.crawl.CrawlDb
elif [ "$COMMAND" = "invertlinks" ] ; then
  CLASS=org.apache.nutch.crawl.LinkDb
elif [ "$COMMAND" = "mergelinkdb" ] ; then
  CLASS=org.apache.nutch.crawl.LinkDbMerger
elif [ "$COMMAND" = "dump" ] ; then
  CLASS=org.apache.nutch.tools.FileDumper
elif [ "$COMMAND" = "commoncrawldump" ] ; then
  CLASS=org.apache.nutch.tools.CommonCrawlDataDumper
elif [ "$COMMAND" = "solrindex" ] ; then
  CLASS="org.apache.nutch.indexer.IndexingJob -D solr.server.url=$1"
  shift
elif [ "$COMMAND" = "index" ] ; then
  CLASS=org.apache.nutch.indexer.IndexingJob
elif [ "$COMMAND" = "solrdedup" ] ; then
  echo "Command $COMMAND is deprecated, please use dedup instead"
  exit -1
elif [ "$COMMAND" = "dedup" ] ; then
  CLASS=org.apache.nutch.crawl.DeduplicationJob
elif [ "$COMMAND" = "solrclean" ] ; then
  CLASS="org.apache.nutch.indexer.CleaningJob -D solr.server.url=$2 $1"
  shift; shift
elif [ "$COMMAND" = "clean" ] ; then
  CLASS=org.apache.nutch.indexer.CleaningJob
elif [ "$COMMAND" = "parsechecker" ] ; then
  CLASS=org.apache.nutch.parse.ParserChecker
elif [ "$COMMAND" = "indexchecker" ] ; then
  CLASS=org.apache.nutch.indexer.IndexingFiltersChecker
elif [ "$COMMAND" = "filterchecker" ] ; then
  CLASS=org.apache.nutch.net.URLFilterChecker
elif [ "$COMMAND" = "normalizerchecker" ] ; then
  CLASS=org.apache.nutch.net.URLNormalizerChecker
elif [ "$COMMAND" = "domainstats" ] ; then 
  CLASS=org.apache.nutch.util.domain.DomainStatistics
elif [ "$COMMAND" = "protocolstats" ] ; then
   CLASS=org.apache.nutch.util.ProtocolStatusStatistics
elif [ "$COMMAND" = "crawlcomplete" ] ; then
  CLASS=org.apache.nutch.util.CrawlCompletionStats
elif [ "$COMMAND" = "webgraph" ] ; then
  CLASS=org.apache.nutch.scoring.webgraph.WebGraph
elif [ "$COMMAND" = "linkrank" ] ; then
  CLASS=org.apache.nutch.scoring.webgraph.LinkRank
elif [ "$COMMAND" = "scoreupdater" ] ; then
  CLASS=org.apache.nutch.scoring.webgraph.ScoreUpdater
elif [ "$COMMAND" = "nodedumper" ] ; then
  CLASS=org.apache.nutch.scoring.webgraph.NodeDumper
elif [ "$COMMAND" = "plugin" ] ; then
  CLASS=org.apache.nutch.plugin.PluginRepository
elif [ "$COMMAND" = "junit" ] ; then
  CLASSPATH="$CLASSPATH:$NUTCH_HOME/test/classes/"
  if $local; then
    for f in "$NUTCH_HOME"/test/lib/*.jar; do
      CLASSPATH="${CLASSPATH}:$f";
    done
  fi
  CLASS=org.junit.runner.JUnitCore
elif [ "$COMMAND" = "startserver" ] ; then
  CLASS=org.apache.nutch.service.NutchServer
elif [ "$COMMAND" = "webapp" ] ; then
  CLASS=org.apache.nutch.webui.NutchUiServer
elif [ "$COMMAND" = "warc" ] ; then
  CLASS=org.apache.nutch.tools.warc.WARCExporter
elif [ "$COMMAND" = "updatehostdb" ] ; then
  CLASS=org.apache.nutch.hostdb.UpdateHostDb
elif [ "$COMMAND" = "readhostdb" ] ; then
  CLASS=org.apache.nutch.hostdb.ReadHostDb
elif [ "$COMMAND" = "sitemap" ] ; then
  CLASS=org.apache.nutch.util.SitemapProcessor
else
  CLASS=$COMMAND
fi

# distributed mode
EXEC_CALL=(hadoop jar "$NUTCH_JOB")

if $local; then
 EXEC_CALL=("$JAVA" $JAVA_HEAP_MAX "${NUTCH_OPTS[@]}" -classpath "$CLASSPATH")
else
 # check that hadoop can be found on the path
 if [ $(which hadoop | wc -l ) -eq 0 ]; then
    echo "Can't find Hadoop executable. Add HADOOP_COMMON_HOME/bin to the path or run in local mode."
    exit -1;
 fi
fi

# run it
exec "${EXEC_CALL[@]}" $CLASS "$@"

