Pentru a rula aplicatia, trebuie creat un proiect Maven cu setup-ul din pom.xml.

Codul se afla in src/main/java/api1. Clasa main este MainApplicationClass.java.
Proiectul a fost creat ca un Maven Project in Eclipse.

Pentru a crea api-ul, am folosit Spring boot si o baza de date SQLite.

Controller-ul pentru api se afla in clasa Controller. Acolo am si explicat 
posibelele HTTP requests. 
Metodele prin care se updateaza baza de date sunt in DBConnection.java.

In src/main/java/api1/tests/ControllerTests.java se afla si unit test-uri pentru 
logica aplicatie.

Structura bazei de date este urmatoarea: 

CREATE TABLE Buyer (
	id INTEGER PRIMARY KEY,
	corporate BOOLEAN NOT NULL,
	buyerName STRING NOT NULL UNIQUE,
	value REAL,
	persID STRING NOT NULL,
	dateRegistered DATE,
	address STRING);

CREATE TABLE Transaction1 (
	id INTEGER PRIMARY KEY,
	transactionNumber INT NOT NULL UNIQUE, 
	value REAL NOT NULL,
	desc STRING,
	custID INTEGER NOT NULL,
 	FOREIGN KEY(custID) REFERENCES Buyer(id));


