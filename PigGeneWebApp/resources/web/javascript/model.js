var buttons =  [
		{name:"components", title:"open component view", showState: true, active:"", logo:"fa fa-file-code-o"},
		{name:"workflows", title:"open workflow view", showState: true, active:"", logo:"fa fa-tasks"},
		{name:"library file", title:"add library file", showState: true, active:"", logo:"fa fa-puzzle-piece"}
	];

var componentButtons = [
        {name:"createComponentBtn", title:"create new component", showState: true, active:"", text:"new", logo:"fa fa-file-text-o"},
        {name:"openComponentBtn", title:"open existing component", showState: true, active:"", text:"open", logo:"fa fa-folder-open-o"},
		{name:"deleteComponentBtn", title:"delete component", showState: false, active:"", text:"delete", logo:"fa fa-trash-o"},           
    ];

var workflowButtons = [
        {name:"createWfBtn", title:"create new workflow", showState: true, active:"", text:"new", logo:"fa fa-file-text-o"},
        {name:"openWfBtn", title:"open existing workflow", showState: true, active:"", text:"open", logo:"fa fa-folder-open-o"},
		{name:"deleteWfBtn", title:"delete workflow", showState: false, active:"", text:"delete", logo:"fa fa-trash-o"},
		{name:"downloadScriptBtn", title:"download pig script", showState: false, active:"", text:"download pig", logo:"fa fa-cloud-download"},
		{name:"downloadZipBtn", title:"download workflow zip-file", showState: false, active:"", text:"download zip", logo:"fa fa-file-archive-o"}
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