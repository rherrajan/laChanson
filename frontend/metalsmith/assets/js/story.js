function loadStory() {
  	var uuid = getUUID();
  
  	alert(" --- lets get ready to rumble " + uuid + "!!! --- ");
  
	var statusURL = createBackendURL("loadStory");
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4) {

			if(this.status == 200){
			  	alert(" --- : " + xhttp.responseText);
			} else {
				alert("error " + this.status + " ("+ this.statusText + ")");
			}
		};
	}
	
	xhttp.open("POST", statusURL, true);
	//xhttp.setRequestHeader('Content-Type', 'application/json');
	xhttp.send(JSON.stringify({
      uuid: uuid
	}));

}
