# Makefile for Maven Project

# Variables
SRC_DIR := src/main/java
TARGET_DIR := target
MAIN_JAR_NAME := jlox.jar
TOOL_JAR_NAME := generate-ast.jar

# Default target
all: package run

package:
	@mvn package
clean:
	@mvn clean

$(TARGET_DIR)/$(MAIN_JAR_NAME):
	@mvn package

$(TARGET_DIR)/$(TOOL_JAR_NAME):
	@mvn package

# Run target
run: $(TARGET_DIR)/$(MAIN_JAR_NAME)
	@java -jar $<

generate-ast: ${TARGET_DIR}/${TOOL_JAR_NAME}
	@java -jar $< ${SRC_DIR}/com/sravan/lox

.PHONY: all clean package run
