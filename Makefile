CLASSES = $(shell find . -type f -name '*.java')
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class


run: $(CLASSES)
	$(JC) $(JFLAGS) $(CLASSES)
	java -classpath src cs349.a3.Cs349A3
