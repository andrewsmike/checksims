all: jar

jar:
	mvn compile package

test:
	mvn compile test
clean:
	mvn clean

