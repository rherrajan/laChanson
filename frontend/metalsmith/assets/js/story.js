function loadStory() {
  	var uuid = getUUID();
  
  	//alert(" --- lets get ready to rumble " + uuid + "!!! --- ");
  
	var statusURL = createBackendURL("loadStory");
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4) {

			if(this.status == 200){
			  	alert(" --- go: " + xhttp.responseText);
			} else {
				alert("error " + this.status);
			}
		};
	}
	
	xhttp.setRequestHeader("uuid", uuid);
	xhttp.open("GET", statusURL, true);

	xhttp.send();

}