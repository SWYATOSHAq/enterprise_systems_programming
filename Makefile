JAVA ?= java
JAVAC ?= javac
JAVA_RELEASE := 17
JDBC_VERSION := 42.7.13
BUILD_DIR := build/classes
LIB_DIR := build/lib
JDBC_JAR := $(LIB_DIR)/postgresql-$(JDBC_VERSION).jar
CLASSPATH := $(BUILD_DIR):$(LIB_DIR)/*
MAIN_CLASS := ru.university.paymentgateway.Main
SOURCES := $(shell find src/main/java -type f -name '*.java')
DB_HOST ?= localhost
DB_PORT ?= 5432
DB_NAME ?= payment_gateway
DB_USER ?= postgres

.PHONY: deps compile setup-db run check-db-password

deps: $(JDBC_JAR)

$(JDBC_JAR):
	mkdir -p $(LIB_DIR)
	curl -L --fail --silent --show-error -o $@ https://repo.maven.apache.org/maven2/org/postgresql/postgresql/$(JDBC_VERSION)/postgresql-$(JDBC_VERSION).jar

compile: deps
	mkdir -p $(BUILD_DIR)
	$(JAVAC) --release $(JAVA_RELEASE) -encoding UTF-8 -Xlint:all -cp '$(LIB_DIR)/*' -d $(BUILD_DIR) $(SOURCES)

check-db-password:
	@test -n "$(DB_PASSWORD)" || (echo "Задайте пароль: DB_PASSWORD=... make run"; exit 1)

setup-db: check-db-password
	@PGPASSWORD='$(DB_PASSWORD)' psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME) -f database/schema.sql

run: compile check-db-password
	$(JAVA) -cp '$(CLASSPATH)' $(MAIN_CLASS)
