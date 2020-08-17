#!/usr/bin/env bash

# script to setup project directly on aws ec2 (ubuntu 16)
PROFILE=aws

# install docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# docker compose setup
sudo curl -L "https://github.com/docker/compose/releases/download/1.26.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
docker-compose --version

# app setup and build
docker-compose build --build-arg $PROFILE
sudo usermod -aG docker $USER
newgrp docker

# test installation
sudo docker run hello-world

# start the app
docker-compose up -d
