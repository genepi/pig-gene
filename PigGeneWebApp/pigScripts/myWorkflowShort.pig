//this is a dummy workflow that is used for testing purpose while developing the application1
REGISTER pigGene.jar;
R5 = FILTER R4 BY x==2;
STORE R5 INTO '$output.txt';
