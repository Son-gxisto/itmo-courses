PACKAGE=A.zip

all: main

run: main
	java -Xms256m -Xmx512m -cp src Main

main:
	javac -cp src src/Main.java

pack:
	zip $(PACKAGE) -r Makefile src
