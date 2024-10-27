#!/bin/bash

# 定义颜色输出
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 定义项目路径
BANK_SERVICE_PATH="./bank-service"
MAIL_SERVICE_PATH="./mail-service"
DELIVERY_SERVICE_PATH="./delivery-service"
STORE_PATH="./store"

# 给所有 mvnw 文件添加执行权限
echo -e "${YELLOW}正在添加执行权限到 mvnw 文件...${NC}"
chmod +x $BANK_SERVICE_PATH/mvnw
chmod +x $MAIL_SERVICE_PATH/mvnw
chmod +x $DELIVERY_SERVICE_PATH/mvnw
chmod +x $STORE_PATH/mvnw

# 检查服务是否成功启动的函数
check_service() {
    local service_name=$1
    local port=$2
    local max_attempts=30
    local attempt=1
    
    echo -e "${YELLOW}正在检查 $service_name 是否启动...${NC}"
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s http://localhost:$port/actuator/health > /dev/null 2>&1; then
            echo -e "${GREEN}$service_name 已成功启动${NC}"
            return 0
        fi
        echo "等待 $service_name 启动... ($attempt/$max_attempts)"
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo -e "${RED}$service_name 启动超时${NC}"
    return 1
}

# 启动 bank-service (端口 8081)
echo -e "${YELLOW}正在启动 bank-service...${NC}"
cd $BANK_SERVICE_PATH
./mvnw spring-boot:run &
BANK_PID=$!
cd ..
check_service "bank-service" 8081 || exit 1

# 启动 mail-service (端口 8082)
echo -e "${YELLOW}正在启动 mail-service...${NC}"
cd $MAIL_SERVICE_PATH
./mvnw spring-boot:run &
MAIL_PID=$!
cd ..
check_service "mail-service" 8082 || exit 1

# 启动 delivery-service (端口 8083)
echo -e "${YELLOW}正在启动 delivery-service...${NC}"
cd $DELIVERY_SERVICE_PATH
./mvnw spring-boot:run &
DELIVERY_PID=$!
cd ..
check_service "delivery-service" 8083 || exit 1

# 当所有依赖服务都启动后，启动 store (端口 8080)
echo -e "${YELLOW}所有依赖服务已启动，现在启动 store...${NC}"
cd $STORE_PATH
./mvnw spring-boot:run &
STORE_PID=$!
cd ..
check_service "store" 8080 || exit 1

echo -e "${GREEN}所有服务已成功启动！${NC}"

# 保存所有进程 ID 到文件中，方便之后关闭
echo "$BANK_PID $MAIL_PID $DELIVERY_PID $STORE_PID" > running_services.pid

# 等待用户输入以关闭所有服务
echo "按 CTRL+C 关闭所有服务"
wait