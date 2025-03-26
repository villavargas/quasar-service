#!/bin/bash
sudo apt update && sudo apt install -y docker.io
git clone https://github.com/TU_REPOSITORIO/quasar-service.git
cd quasar-service
mvn clean package -DskipTests
docker build -t quasar-service .
docker run -d -p 8080:8080 --name quasar-container quasar-service
echo "🚀 Aplicación desplegada en AWS EC2 en el puerto 8080"
