# Neu Hochfahren Container
docker-compose -f ShogiDB.yml up -d

# Herunterfahren und zurücksetzen
docker-compose -f ShogiDB.yml down

# Start
docker-compose -f ShogiDB.yml start

# Stop
docker-compose -f ShogiDB.yml stop


# Docker Console
docker exec -it “container-id” bash
# Docker-Image mit Dockerfile bauen
docker build -t shogi:latest .

# docker images
docker images

# remove image force
docker rmi "IMAGE ID" -f

# RIP all none images
docker image prune -f