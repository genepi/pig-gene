REGISTER pigGene.jar;

//asdfasdf
REGISTER asdf;
REGISTER asdge;

//123
R1 = LOAD '$input.txt' USING PigStorage('\t');

//345
R2 = FILTER R1 BY chrom == 12;
R3 = LOAD '$reference.vcf' USING PigStorage('\t');

//abc
R4 = JOIN R2 BY (chrom,pos), R3 BY (chrom,pos);
REGISTER asdf;

//it
STORE R4 INTO '$output';
REGISTER asdf;
REGISTER asdf;
asdf = LOAD '$asdf' USING PigStorage('\t');
REGISTER asdf;
REGISTER asdfas;
REGISTER qwer;
REGISTER erz;
REGISTER er;

//blub
REGISTER werz;
REGISTER qerqerz;

//jzdt
REGISTER wwetwe;
REGISTER werwert;
REGISTER wertwert;
REGISTER werwet;
REGISTER wertwert;
asdfasdf = FILTER wetzh BY hw;

//filter me
R25 = FILTER gwg BY qert;
STORE zuw INTO '$uwe';
