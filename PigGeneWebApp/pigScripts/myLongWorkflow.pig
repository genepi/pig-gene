//this is a dummy workflow that is used for testing purpose while developing the application
REGISTER pigGene.jar;
REGISTER dataFu.jar;

//dfysdfh
R1 = LOAD '$input.vcf' USING pigGene.PigGeneStorage();
R3 = LOAD '$input2.txt' USING PigStorage(' ');

//filter
R2 = FILTER R1 BY chrom == 12;
R2a = FILTER R2 BY pos==5;

//join
R4 = JOIN R2 BY (chrom,pos), R3 BY (chrom,pos);
R5 = FILTER R4 BY x==2;

//comment for user script...
R9 = FILTER R5 BY chrom==1;
STORE R9 INTO output02.txt;
STORE R5 INTO '$output.txt';
STORE sad INTO '$asdg';
sadf = LOAD '$adsf' USING pigGene.PigGeneStorage();
REGISTER asdf;
STORE asdf INTO '$asdf';
STORE fweq INTO '$gq';
STORE rqw INTO '$rqwe';
STORE tqwe INTO '$tqw';
STORE weqt INTO '$qwet';
tqwe = LOAD '$wqet' USING PigStorage(' ');
