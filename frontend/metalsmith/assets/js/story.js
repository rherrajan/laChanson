function loadStory() {
  	var uuid = getUUID();
	var statusURL = createBackendURL("loadStory");
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4) {
			if(this.status == 200){
				showStory(JSON.parse(xhttp.responseText));
			} else if(this.status == 0){
				alert("cors error. is backend running?");
			} else {
				alert("error, response: " + this.status + " ("+ this.statusText + ")");
			}
		};
	}
	
	xhttp.open("POST", statusURL, true);
	xhttp.send("uuid:"+uuid);
};

function showStory(story) {
	var storyPart = document.querySelector('#story-part');
	storyPart.innerHTML = story.location.markup;
}
