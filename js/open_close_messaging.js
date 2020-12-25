var messagingAuth = document.getElementById("messagingAuth")
var messagingh = false
	function messagingMethods(){
		if(!messagingh){
			messagingh = true
			messagingAuth.setAttribute("src", "img/icons/minus.svg")
			$("#messaging").fadeIn(500)
		}else{
			messagingh = false
			messagingAuth.setAttribute("src", "img/icons/plus.svg")
			$("#messaging").fadeOut(500)
		} 
	}