#!/usr/bin/env bash
docker-compose build --build-arg aws
docker-compose up -d

#crontab -e
#@reboot  $HOME/aws-run.sh