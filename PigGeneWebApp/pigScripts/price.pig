--preisauflistung
REGISTER pigGene.jar;
R1 = LOAD '$autos' USING PigStorage('\t');
R2 = LOAD '$preise' USING PigStorage('\t');
R3 = FILTER R2 BY $1 > 20000;

--alle groesser 20000
STORE R3 INTO '$prices';
R4 = JOIN R1 BY ($0), R2 BY ($0);

--gesamtausgabe
STORE R4 INTO '$total';
