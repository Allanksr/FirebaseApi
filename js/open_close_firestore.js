var firestoreAuth = document.getElementById("firestoreAuth")
var firestoreh = false
	function firestoreMethods(){
		if(!firestoreh){
			firestoreh = true
			firestoreAuth.setAttribute("src", "img/icons/minus.svg")
			$("#firestore").fadeIn(500)
		}else{
			firestoreh = false
			firestoreAuth.setAttribute("src", "img/icons/plus.svg")
			$("#firestore").fadeOut(500)
		} 
	}