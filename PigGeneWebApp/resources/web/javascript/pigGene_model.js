var workflow = [
    {relation:"R1", input:"input1", input2:"-", operation:"LOAD", options:"vcf", options2:"-", comment:"Loads the input file.", active:false},
    {relation:"R2", input:"R1", input2:"-", operation:"FILTER", options:"pos==138004", options2:"-", comment:"Filters all lines that match position '138004'.", active:false},
    {relation:"R3", input:"R2", input2:"", operation:"STORE", options:"-", options2:"-", comment:"-", active:false}          
 ];