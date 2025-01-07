# docker
docker pull mysql:8.0.38
docker run --name boki-board-mysql -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql:8.0.38