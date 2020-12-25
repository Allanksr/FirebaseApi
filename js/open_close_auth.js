var image_div = document.getElementById("image_div")
var image_see = document.getElementById("image_see")
var image_inflate = false
	function imageInflate(src){
		if(!image_inflate){
				image_inflate = true
				image_div.setAttribute("class", "image_onclick_view")
			if(src.length>0){
				image_see.setAttribute("src", ""+src+"")
			}			
		}else{
			image_inflate = false
			image_div.setAttribute("class", "image_onclick")
		} 
	}


var plusAuth = document.getElementById("plusAuth")
var authServiceh = false
	function authMethods(){
		if(!authServiceh){
			authServiceh = true
			plusAuth.setAttribute("src", "img/icons/minus.svg")
			$(".table").fadeIn(500)
		}else{
			authServiceh = false
			plusAuth.setAttribute("src", "img/icons/plus.svg")
			$(".table").fadeOut(500)
		} 
	}

var plusEmail = document.getElementById("plusEmail")
var emailh = false
	function emailHint(){
		if(!emailh){
			emailh = true
			plusEmail.setAttribute("src", "img/icons/minus.svg")
			$("#emailHint").fadeIn(500)
		}else{
			emailh = false
			plusEmail.setAttribute("src", "img/icons/plus.svg")
			$("#emailHint").fadeOut(500)
		} 
	}

var plusSmartphone = document.getElementById("plusSmartphone")
var phoneh = false
	function phoneHint(){
			if(!phoneh){
			phoneh = true
			plusSmartphone.setAttribute("src", "img/icons/minus.svg")
			$("#phoneHint").fadeIn(500)
		}else{
			phoneh = false
			plusSmartphone.setAttribute("src", "img/icons/plus.svg")
			$("#phoneHint").fadeOut(500)
		} 
	}

var plusGoogle = document.getElementById("plusGoogle")
var googleh = false
	function googleHint(){
		if(!googleh){
			googleh = true
			plusGoogle.setAttribute("src", "img/icons/minus.svg")
			$("#googleHint").fadeIn(500)
		}else{
			googleh = false
			plusGoogle.setAttribute("src", "img/icons/plus.svg")
			$("#googleHint").fadeOut(500)
		} 
	}

var plusGames = document.getElementById("plusGames")
var gamesh = false
	function gamesHint(){
		if(!gamesh){
			gamesh = true
			plusGames.setAttribute("src", "img/icons/minus.svg")
			$("#gamesHint").fadeIn(500)
		}else{
			gamesh = false
			plusGames.setAttribute("src", "img/icons/plus.svg")
			$("#gamesHint").fadeOut(500)
		} 
	}

var plusFace = document.getElementById("plusFace")
var faceh = false
	function faceHint(){
		if(!faceh){
			faceh = true
			plusFace.setAttribute("src", "img/icons/minus.svg")
			$("#faceHint").fadeIn(500)
		}else{
			faceh = false
			plusFace.setAttribute("src", "img/icons/plus.svg")
			$("#faceHint").fadeOut(500)
		} 
	}

var plusTwitter = document.getElementById("plusTwitter")
var twitterh = false
	function twitterHint(){
		if(!twitterh){
			twitterh = true
			plusTwitter.setAttribute("src", "img/icons/minus.svg")
			$("#twitterHint").fadeIn(500)
		}else{
			twitterh = false
			plusTwitter.setAttribute("src", "img/icons/plus.svg")
			$("#twitterHint").fadeOut(500)
		} 
	}

var plusGithub = document.getElementById("plusGithub")
var githubh = false
	function githubHint(){
		if(!githubh){
			githubh = true
			plusGithub.setAttribute("src", "img/icons/minus.svg")
			$("#githubHint").fadeIn(500)
		}else{
			githubh = false
			plusGithub.setAttribute("src", "img/icons/plus.svg")
			$("#githubHint").fadeOut(500)
		} 
	}
	
	var plusYahoo = document.getElementById("plusYahoo")
var yahooh = false
	function yahooHint(){
		if(!yahooh){
			yahooh = true
			plusYahoo.setAttribute("src", "img/icons/minus.svg")
			$("#yahooHint").fadeIn(500)
		}else{
			yahooh = false
			plusYahoo.setAttribute("src", "img/icons/plus.svg")
			$("#yahooHint").fadeOut(500)
		} 
	}
	
		var plusMicrosoft = document.getElementById("plusMicrosoft")
var microsofth = false
	function microsoftHint(){
		if(!microsofth){
			microsofth = true
			plusMicrosoft.setAttribute("src", "img/icons/minus.svg")
			$("#microsoftHint").fadeIn(500)
		}else{
			microsofth = false
			plusMicrosoft.setAttribute("src", "img/icons/plus.svg")
			$("#microsoftHint").fadeOut(500)
		} 
	}
	
	
	
	
	
	