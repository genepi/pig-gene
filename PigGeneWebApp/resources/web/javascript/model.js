var buttons =  [
		{name:"newWfBtn", title:"create new workflow", showState:true, active:"", text:"new", logo:"file"},
		{name:"openWfBtn", title:"open existing workflow", showState:true, active:"", text:"open", logo:"folder-open"},
		{name:"saveWfBtn", title:"save workflow", showState:true, active:"", text:"save", logo:"floppy-disk"},
		{name:"deleteWfBtn", title:"delete workflow", showState:true, active:"", text:"delete", logo:"trash"},
		{name:"downloadScriptBtn", title:"download pig script", showState:true, active:"", text:"download", logo:"cloud-download"},
		{name:"runJobBtn", title:"run workflow on cloudgene cluster", showState:true, active:"", text:"run", logo:"play"}
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