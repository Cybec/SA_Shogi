#
# Scala and sbt Dockerfile
#
# https://github.com/hseeberger/scala-sbt
#

# Pull base image
FROM debian:stretch-slim
FROM openjdk:8u171

ENV DEBIAN_FRONTEND noninteractive

# Env variables
ENV SCALA_VERSION 2.11.8
ENV SBT_VERSION 0.13.11

# Scala expects this file
RUN ls -l /usr/lib/jvm/
RUN touch /usr/lib/jvm/java-8-openjdk-amd64/release
RUN apt-get update -y
RUN apt-get install curl -y
RUN apt-get upgrade -y


# Install Scala
# Piping curl directly in tar
RUN \
  curl -fsL https://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /root/ && \
  echo >> /root/.bashrc && \
  echo "export PATH=~/scala-$SCALA_VERSION/bin:$PATH" >> /root/.bashrc



# Install sbt
RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update -y && \
  apt-get install -y sbt && \
  sbt sbtVersion

# Define working directory
WORKDIR /root