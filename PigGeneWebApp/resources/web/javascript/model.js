var buttons =  [
		{name:"components", title:"open component view", showState: true, active:"", text:"component", logo:"list"},
		{name:"workflows", title:"open workflow view", showState: true, active:"", text:"workflow", logo:"tasks"},
		{name:"library", title:"add library file", showState: true, active:"", text:"library file", logo:"inbox"}
	];

var componentButtons = [
        {name:"createComponentBtn", title:"create new component", showState: true, active:"", text:"new", logo:"file"},
        {name:"openComponentBtn", title:"open existing component", showState: true, active:"", text:"open", logo:"folder-open"},
		{name:"deleteComponentBtn", title:"delete component", showState: true, active:"", text:"delete", logo:"trash"},           
    ];

var workflowButtons = [
        {name:"createWfBtn", title:"create new workflow", showState: true, active:"", text:"new", logo:"file"},
        {name:"openWfBtn", title:"open existing workflow", showState: true, active:"", text:"open", logo:"folder-open"},
		{name:"deleteWfBtn", title:"delete workflow", showState: true, active:"", text:"delete", logo:"trash"},
		{name:"downloadScriptBtn", title:"download pig script", showState: true, active:"", text:"download pig", logo:"cloud-download"},
		{name:"downloadZipBtn", title:"download workflow zip-file", showState: true, active:"", text:"download zip", logo:"briefcase"}
    ];

var scriptType = [
        {id:0, name:"Apache Pig Script"},
        {id:1, name:"R Markdown Script"}
    ];

var anchors = [
               [1, 0.5,  1, 0, 0, 0],
               [0, 0.5, -1, 0, 0, 0]
    ];

var wfAbbr = "wf";
var compAbbr = "comp";