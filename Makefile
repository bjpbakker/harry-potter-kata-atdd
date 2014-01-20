# Common tasks:
# make		     -- full build
# make clean   -- clean up built files
# make again   -- clean and rebuild

SHELL := /bin/bash

LIBRARY=potter-kata
CLASSPATH=nop
TEST_CLASSPATH=$(shell echo $(wildcard lib/test/*.jar) | sed -e 's/ /:/g')
FEATURES_CLASSPATH=$(shell echo $(wildcard lib/features/*.jar) | sed -e 's/ /:/g')

JAVA_HOME=$(shell /usr/libexec/java_home -v 1.8)

all: jar tests features
jar: dist/$(LIBRARY).jar
tests: build/.tests-pass
features: build/.features-pass
.PHONY: all jar features

# function to find files in directory with extension. $(call find,dir,ext)
find = $(shell find $(1) -name '*.$(2)')
# function to extract tests from a jar. $(call extracttests,foo.jar)
extracttests = $(shell jar tf $(1) | grep 'Test.class$$' | sed -e 's|/|.|g;s|.class$$||')

dist/$(LIBRARY).jar: $(call find,src/main,java)
	@mkdir -p build/main/classes
	@mkdir -p dist
	javac -encoding UTF-8 -g -cp $(CLASSPATH) -d build/main/classes $(call find,src/main,java)
	jar cf $@ -C build/main/classes .

build/$(LIBRARY)-test.jar: dist/$(LIBRARY).jar $(call find,src/test,java)
	@mkdir -p build/test/classes
	javac -encoding UTF-8 -g -cp $(TEST_CLASSPATH):$(CLASSPATH):dist/$(LIBRARY).jar -d build/test/classes $(call find,src/test,java)
	jar cf $@ -C build/test/classes .

build/.tests-pass: dist/$(LIBRARY).jar build/$(LIBRARY)-test.jar
	@rm -f $@
	java -cp $(TEST_CLASSPATH):$(CLASSPATH):build/$(LIBRARY)-test.jar:dist/$(LIBRARY).jar org.junit.runner.JUnitCore $(call extracttests,build/$(LIBRARY)-test.jar)
	@touch $@

build/$(LIBRARY)-glue.jar: dist/$(LIBRARY).jar $(call find,src/glue,java)
	@mkdir -p build/glue/classes
	javac -encoding UTF-8 -g -cp $(FEATURES_CLASSPATH):$(CLASSPATH):dist/$(LIBRARY).jar -d build/glue/classes $(call find,src/glue,java)
	jar cf $@ -C build/glue/classes .

build/.features-pass: dist/$(LIBRARY).jar build/$(LIBRARY)-glue.jar $(call find,features,feature)
	@rm -f $@
	java -cp $(FEATURES_CLASSPATH):$(CLASSPATH):dist/$(LIBRARY).jar:build/$(LIBRARY)-glue.jar cucumber.api.cli.Main --format pretty --glue com.github.bjpbakker.potter.cucumber $(call find,features,feature)
	@touch $@

clean:
	rm -rf build dist
.PHONY: clean

again: clean all
.PHONY: again

