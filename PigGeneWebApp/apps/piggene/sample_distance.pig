--Calculates the distance between two locations (given by latitude and longitude) using the haversine formula.
REGISTER pigGene.jar;
REGISTER SeqPig.jar;
REGISTER hadoop-bam-6.0.jar;
REGISTER sam-1.93.jar;
REGISTER picard-1.93.jar;
REGISTER variant-1.93.jar;
REGISTER tribble-1.93.jar;
REGISTER commons-jexl-2.1.1.jar;
--Selects only the needed columns and adds a distance column which is calculated by the dataFu library.
R5 = FOREACH R445 GENERATE R1::id .. R1::lon, R2::city .. R2::lon, datafu.pig.geo.HaversineDistInMiles(R1::lat, R1::lon, R2::lat, R2::lon) as distance;
--Stores the output of the calculation.
STORE R005 INTO '$output1';
