JAVA ?= java
JAVAC ?= javac
JAVA_RELEASE := 17
BUILD_DIR := build/classes
MAIN_CLASS := ru.university.paymentgateway.Main
SOURCES := $(shell find src/main/java -type f -name '*.java')

.PHONY: compile run

compile:
	mkdir -p $(BUILD_DIR)
	$(JAVAC) --release $(JAVA_RELEASE) -encoding UTF-8 -Xlint:all -d $(BUILD_DIR) $(SOURCES)

run: compile
	$(JAVA) -cp $(BUILD_DIR) $(MAIN_CLASS)
