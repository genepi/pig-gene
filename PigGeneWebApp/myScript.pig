REGISTER pigGene.jar;
R1 = FILTER rel1 BY id > 20;
R2 = FILTER R1 BY id < 50;
R3 = JOIN R1 BY (R1.id == R2.id), R2 BY (R2.id > 30);
R4 = FILTER R3 BY chrom == 12;
R5 = FILTER R4 BY pos = 14000;
