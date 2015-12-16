#!/bin/sh
#nohup java -classpath tcp_server-0.0.1.jar:lib/crypto-0.0.1.jar:lib/json-20140107.jar:lib/disruptor-3.3.2.jar:lib/log4j-1.2.17.jar com.creative.server.TCPServer 10001 1000 512 /tmp > ~/tcp_server.log 2> ~/tcp_server.log
SERVICE_NAME=TCP-server
PATH_TO_JAR=./target/
COMMNAD='nohup java -classpath tcp_server-0.0.1.jar:lib/crypto-0.0.1.jar:lib/json-20140107.jar:lib/disruptor-3.3.2.jar:lib/log4j-1.2.17.jar com.creative.server.TCPServer 10001 1000 512 > ~/tcp_server.log 2> ~/tcp_server.log &'
PID_PATH_NAME=/tmp/TCP-server-pid
case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            cd $PATH_TO_JAR && nohup java -classpath tcp_server-0.0.1.jar:lib/crypto-0.0.1.jar:lib/json-20140107.jar:lib/disruptor-3.3.2.jar:lib/log4j-1.2.17.jar com.creative.server.TCPServer 10001 1000 512 > ~/tcp_server.log 2> ~/tcp_server.log &
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
            kill $PID;
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
            kill $PID;
            echo "$SERVICE_NAME stopped ...";
            rm $PID_PATH_NAME
            echo "$SERVICE_NAME starting ..."
            cd $PATH_TO_JAR && nohup java -classpath tcp_server-0.0.1.jar:lib/crypto-0.0.1.jar:lib/json-20140107.jar:lib/disruptor-3.3.2.jar:lib/log4j-1.2.17.jar com.creative.server.TCPServer 10001 1000 512 > ~/tcp_server.log 2> ~/tcp_server.log &
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac 
