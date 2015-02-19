var buttons =  [
		{name:"newCompBtn", title:"create new component", showState: true, active:"", text:"new component", logo:"file"},
		{name:"newWfBtn", title:"create new workflow", showState: true, active:"", text:"new workflow", logo:"tasks"},
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