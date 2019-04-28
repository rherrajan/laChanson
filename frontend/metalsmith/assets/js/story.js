function loadStory() {
  	var uuid = getUUID();
	var statusURL = createBackendURL("loadStory");
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4) {
			if(this.status == 200){
				showStory(JSON.parse(xhttp.responseText));
			} else if(this.status == 0){
				alert("cors error. is backend running and servlet existing?");
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
	
	var links = storyPart.querySelectorAll('a');
	
	links.forEach(function(link) {

		var query;
		if(link.search){
		  query = link.search+"&destination="+link.pathname;
		} else {
		  query = "?destination="+link.pathname;
		}

		var backendURL = createBackendURL("progressStory"+query);
		//link.href="";	// remove the link to ensure that it can not be opened in new tab
		
		link.onclick = function(){
		  //alert(" --- click detected for: " + backendURL);
		  sendProgress(backendURL);
		  return false;
		};
	})
	
	if(story.player.additionalKnowledge){
		alert("Wissen hinzugewonnen");
	}

}

function sendProgress(backendURL) {
  	var uuid = getUUID();
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4) {
			if(this.status == 200){
				showStory(JSON.parse(xhttp.responseText));
			} else if(this.status == 0){
				alert("cors error. is backend running and servlet existing?");
			} else {
				alert("error, response: " + this.status + " ("+ this.statusText + ")");
			}
		};
	}
	
	xhttp.open("POST", backendURL, true);
	xhttp.send("uuid:"+uuid);
}
