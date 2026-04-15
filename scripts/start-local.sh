#!/bin/zsh
set -e
cd "$(dirname "$0")/.."
docker compose up -d --build
echo "供应商管理系统已启动:"
echo "前端: http://127.0.0.1:3180"
echo "后端: http://127.0.0.1:9180/api"

