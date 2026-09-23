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

.PHONY: deps compile run check-db-password

deps: $(JDBC_JAR)

$(JDBC_JAR):
	mkdir -p $(LIB_DIR)
	curl -L --fail --silent --show-error -o $@ https://repo.maven.apache.org/maven2/org/postgresql/postgresql/$(JDBC_VERSION)/postgresql-$(JDBC_VERSION).jar

compile: deps
	mkdir -p $(BUILD_DIR)
	$(JAVAC) --release $(JAVA_RELEASE) -encoding UTF-8 -Xlint:all -cp '$(LIB_DIR)/*' -d $(BUILD_DIR) $(SOURCES)

check-db-password:
	@test -n "$(DB_PASSWORD)" || (echo "Задайте пароль: DB_PASSWORD=... make run"; exit 1)

run: compile check-db-password
	$(JAVA) -cp '$(CLASSPATH)' $(MAIN_CLASS)
