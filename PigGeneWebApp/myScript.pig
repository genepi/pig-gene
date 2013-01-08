REGISTER pigGene.jar;
R1 = FILTER a BY chrom==20;
R2 = JOIN c BY (id), d BY (id1);
R3 = FILTER f BY pos>10000;
