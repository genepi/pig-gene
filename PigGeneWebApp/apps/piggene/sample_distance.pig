--Calculates the distance between two locations (given by latitude and longitude) using the haversine formula.
REGISTER pigGene.jar;
REGISTER SeqPig.jar;
REGISTER hadoop-bam-6.0.jar;
REGISTER sam-1.93.jar;
REGISTER picard-1.93.jar;
REGISTER variant-1.93.jar;
REGISTER tribble-1.93.jar;
REGISTER commons-jexl-2.1.1.jar;
--Registers the dataFu library which contains the method to calculate the distance.
REGISTER datafu-0.0.9.jar;
--Loads the file which contains the information about the locations and their positions.
R1 = LOAD '$input1' USING PigStorage('\t') AS (id:int, city:chararray, place:chararray, lat:double, lon:double);
--Loads the same file again. This is needed because the locations get joined with each other to calculate the distance between them.
R2 = LOAD '$input2' USING PigStorage('\t') AS (id:int, city:chararray, place:chararray, lat:double, lon:double);
--Joins locations with the same id.
R3 = JOIN R1 BY (id), R2 BY (id);
--Selects only the needed columns and adds a distance column which is calculated by the dataFu library.
R5 = FOREACH R4 GENERATE R1::id .. R1::lon, R2::city .. R2::lon, datafu.pig.geo.HaversineDistInMiles(R1::lat, R1::lon, R2::lat, R2::lon) as distance;
--Stores the output of the calculation.
STORE R5 INTO '$output1';
R7 = FILTER - BY -;
R8 = FILTER - BY -;
