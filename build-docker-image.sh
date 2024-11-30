#!/bin/bash

# 각 모듈별 Docker 이미지 빌드
docker build -t survey-gateway:latest -f survey-gateway/Dockerfile .
docker build -t survey-response-gateway:latest -f survey-response-gateway/Dockerfile .
docker build -t survey-discovery:latest -f survey-discovery/Dockerfile .

docker build -t survey-config-server:latest -f survey-config-server/Dockerfile .

docker build -t survey-service:latest -f survey-service/Dockerfile .
docker build -t survey-admin-service:latest -f survey-admin-service/Dockerfile .
docker build -t survey-respondent-service:latest -f survey-respondent-service/Dockerfile .
docker build -t survey-response-service:latest -f survey-response-service/Dockerfile .

