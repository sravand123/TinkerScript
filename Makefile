# Makefile for Maven Project

# Variables
SRC_DIR := src/main/java
TARGET_DIR := target
MAIN_JAR_NAME := jlox-app.jar
TOOL_JAR_NAME := generate-ast-tool.jar

# Default target
all: clean compile package run

# Clean target
clean:
	@mvn clean

# Compile target
compile:
	@mvn compile

# Package target
package:
	@mvn package

# Run target
run: $(TARGET_DIR)/$(MAIN_JAR_NAME)
	@mvn package
	@java -jar $<

generate-ast: ${TARGET_DIR}/${TOOL_JAR_NAME}
	@mvn package
	@java -jar $< ${SRC_DIR}/com/sravan/lox

# Build and run target
build-and-run: all run

.PHONY: all clean compile package run build-and-run
