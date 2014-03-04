var buttons =  [
		{name:"newWfBtn", title:"create new workflow", showState:true, active:"active", text:"new", logo:"file"},
		{name:"showWfBtn", title:"open existing workflow", showState:true, active:"", text:"open", logo:"folder-open"},
		{name:"saveWfBtn", title:"save workflow", showState:true, active:"", text:"save", logo:"floppy-disk"},
		{name:"deleteWfBtn", title:"delete workflow", showState:true, active:"", text:"delete", logo:"trash"},
		{name:"downloadScriptBtn", title:"download pig script", showState:true, active:"", text:"download", logo:"cloud-download"},
		{name:"runJobBtn", title:"run workflow on cluster", showState:true, active:"", text:"run", logo:"play"}
	];

var operations = [
		{name: 'REGISTER'},
		{name: 'LOAD'},
		{name: 'FILTER'},
		{name: 'JOIN'},
		{name: 'SELECT'},
		{name: 'GROUP BY'},
		{name: 'ORDER BY'},
		{name: 'USER SCRIPT'},
		{name: 'STORE'}       
	];

var workflow = [
	    {relation:"R1", input:"input1", input2:"-", operation:"LOAD", options:"vcf", options2:"-", comment:"Loads the input file.", active:false},
	    {relation:"R2", input:"R1", input2:"-", operation:"FILTER", options:"pos==138004", options2:"-", comment:"Filters all lines that match position '138004'.", active:false},
	    {relation:"R3", input:"R2", input2:"-", operation:"STORE", options:"-", options2:"-", comment:"-", active:false},
	    {relation:"R4", input:"R2", input2:"R1", operation:"JOIN", options:"a == a", options2:"-", comment:"conducts a join", active:false}   
    ];


//////////////////////////////////////////////////////////////////////////////////////////
// test

var messages = [{ id: 0, sender: 'jean@somecompany.com', subject: 'Hi there, old friend',
    date: 'Dec 7, 2013 12:32:00', recipients: ['greg@somecompany.com'],
    message: 'Hey, we should get together for lunch sometime and catch up.'
    +'There are many things we should collaborate on this year.' }, 
  	
  	{ id: 1,  sender: 'maria@somecompany.com',
    subject: 'Where did you leave my laptop?',
    date: 'Dec 7, 2013 8:15:12', recipients: ['greg@somecompany.com'],
    message: 'I thought you were going to put it in my desk drawer.'
    +'But it does not seem to be there.' }, 
  	
  	{ id: 2, sender: 'bill@somecompany.com', subject: 'Lost python',
    date: 'Dec 6, 2013 20:35:02', recipients: ['greg@somecompany.com'],
    message: 'blubbbbb' }, 
];

var wiw = [
			{name:"wf1", description:"desc1", data: [
			                                         {relation:"R1", input:"input1", input2:"-", operation:"LOAD", options:"vcf", options2:"-", comment:"Loads the input file.", active:false},
			                                         {relation:"R2", input:"R1", input2:"-", operation:"FILTER", options:"pos==138004", options2:"-", comment:"Filters all lines that match position '138004'.", active:false},
			
			                                         {name:"wf3", description:"desc4", data: [
			                                                                                  {name:"wf4", description:"desc4", data: [
			                                                                                                                           {relation:"R8", input:"input1", input2:"-", operation:"LOAD", options:"vcf", options2:"-", comment:"Loads the input file.", active:false}
			                                                                                                                           ]
			                                                                                  },
			                                                                                  {relation:"R6", input:"R2", input2:"-", operation:"STORE", options:"-", options2:"-", comment:"-", active:false},
			                                                                                  {relation:"R7", input:"R2", input2:"-", operation:"STORE", options:"-", options2:"-", comment:"-", active:false}
			                                                                                  ]
			                                         },
			
			                                         {relation:"R3", input:"R2", input2:"R1", operation:"JOIN", options:"a == a", options2:"-", comment:"conducts a join", active:false}
			                                         ]
			},
			{name:"wf2", description:"desc6", data: [
                                                         {relation:"R4", input:"input1", input2:"-", operation:"LOAD", options:"vcf", options2:"-", comment:"Loads the input file.", active:false},
                                                         {relation:"R5", input:"R2", input2:"-", operation:"STORE", options:"-", options2:"-", comment:"-", active:false},
                                                         ]
            },
		];