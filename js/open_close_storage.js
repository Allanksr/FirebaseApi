var storageAuth = document.getElementById("storageAuth")
var storageh = false
	function storageMethods(){
		if(!storageh){
			storageh = true
			storageAuth.setAttribute("src", "img/icons/minus.svg")
			$("#storage").fadeIn(500)
		}else{
			storageh = false
			storageAuth.setAttribute("src", "img/icons/plus.svg")
			$("#storage").fadeOut(500)
		} 
	}