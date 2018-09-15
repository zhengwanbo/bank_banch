#!/bin/sh

INSTALL_DIR=$(cd "$(dirname "$0")"; pwd)
export TOOL_HOME=$INSTALL_DIR/..
TOOL_CLASSPATH=`echo $TOOL_HOME/lib/*.jar|tr ' ' ':'`
export CLASSPATH=.:$TOOL_CLASSPATH
echo "TOOL_HOME="$TOOL_HOME

USE_CHARSET=UTF-8

TEST_COMMAND=`echo "java -Dfile.encoding=${USE_CHARSET} -Xms1000m -Xmx1000m com.bank.Tester" | sed 's/\s\+/ /g'`
echo "TEST_COMMAND="$TEST_COMMAND
$TEST_COMMAND

exit 0
