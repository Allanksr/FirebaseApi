var functionsAuth = document.getElementById("functionsAuth")
var functionsh = false
	function functionsMethods(){
		if(!functionsh){
			functionsh = true
			functionsAuth.setAttribute("src", "img/icons/minus.svg")
			$("#functions").fadeIn(500)
		}else{
			functionsh = false
			functionsAuth.setAttribute("src", "img/icons/plus.svg")
			$("#functions").fadeOut(500)
		} 
	}