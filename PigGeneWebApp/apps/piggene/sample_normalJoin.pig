--join vcf file with reference file
REGISTER pigGene.jar;
R1 = LOAD '$input1' USING pigGene.storage.merged.PigGeneStorage();
R2 = LOAD '$input2' USING pigGene.storage.reference.PigGeneStorageReferenceFile();
R3 = JOIN R1 BY (chrom,pos), R2 BY (chrom,pos);
R4 = FOREACH R3 GENERATE R1::chrom, R1::pos, R2::id, R1::ref, R1::alt, R1::qual, R1::filt, R1::info, R1::format, R1::genotype, R1::persID;
STORE R4 INTO '$output1';
