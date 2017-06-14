#!/usr/bin/env bash
ps -ef | grep logistics-web | awk '{print}'
echo `date` "Stopping logistics web..."
kill `ps -ef | grep logistics-web | awk 'NR==1{print $2}'`
echo `date` "Stopped"