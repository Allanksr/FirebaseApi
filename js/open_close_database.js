var databaseAuth = document.getElementById("databaseAuth")
var databaseh = false
	function databaseMethods(){
		if(!databaseh){
			databaseh = true
			databaseAuth.setAttribute("src", "img/icons/minus.svg")
			$("#database").fadeIn(500)
		}else{
			databaseh = false
			databaseAuth.setAttribute("src", "img/icons/plus.svg")
			$("#database").fadeOut(500)
		} 
	}