var hostingAuth = document.getElementById("hostingAuth")
var hostingh = false
	function hostingMethods(){
		if(!hostingh){
			hostingh = true
			hostingAuth.setAttribute("src", "img/icons/minus.svg")
			$("#hosting").fadeIn(500)
		}else{
			hostingh = false
			hostingAuth.setAttribute("src", "img/icons/plus.svg")
			$("#hosting").fadeOut(500)
		} 
	}