#!/bin/sh
#nohup java -classpath tcp_server-0.0.1.jar:lib/crypto-0.0.1.jar:lib/json-20140107.jar:lib/disruptor-3.3.2.jar:lib/log4j-1.2.17.jar com.creative.server.TCPServer 10001 1000 512 /tmp > ~/tcp_server.log 2> ~/tcp_server.log
SERVICE_NAME=DNS-server
PATH_TO_JAR=./target/
COMMNAD='nohup java -classpath dns_server-0.0.1.jar:libs/tcp_server-0.0.1.jar:libs/crypto-0.0.1.jar:libs/json-20140107.jar:libs/disruptor-3.3.2.jar:libs/log4j-1.2.17.jar:libs/mysql-connector-java-5.1.38.jar com.creative.dns.DomainServer > /dev/null 2> /dev/null &'
PID_PATH_NAME=/tmp/DNS-server-pid
case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            cd $PATH_TO_JAR && nohup java -classpath dns_server-0.0.1.jar:libs/tcp_server-0.0.1.jar:libs/crypto-0.0.1.jar:libs/json-20140107.jar:libs/disruptor-3.3.2.jar:libs/log4j-1.2.17.jar:libs/mysql-connector-java-5.1.38.jar com.creative.dns.DomainServer > /dev/null 2> /dev/null &
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running ..."
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stoping ..."
            kill -9 $PID;
            echo "$SERVICE_NAME stopped ..."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    restart)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stopping ...";
            kill -9 $PID;
            echo "$SERVICE_NAME stopped ...";
            rm $PID_PATH_NAME
            echo "$SERVICE_NAME starting ..."
            cd $PATH_TO_JAR && nohup java -classpath dns_server-0.0.1.jar:libs/tcp_server-0.0.1.jar:libs/crypto-0.0.1.jar:libs/json-20140107.jar:libs/disruptor-3.3.2.jar:libs/log4j-1.2.17.jar:libs/mysql-connector-java-5.1.38.jar com.creative.dns.DomainServer > /dev/null 2> /dev/null &
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac 
