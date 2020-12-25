const functions = require('firebase-functions')
var admin = require("firebase-admin")
const serviceAccount = require('./credential.json')
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://fir-2870a.firebaseio.com'
})

var db = admin.database()

let valueGlobal
exports.pushData = functions.https.onCall( async (req, res) => {
	if(req !== null){
		
		if(req.messageData[0] === "0"){
			var topic = 'firebasetest'
			var payload  = {
				  data: {
					checkUpdate: "0",
					backGroundMessage: req.messageData[1]			
				  },
				  //topic: topic, /* <- Uncommon for sending messages in the background to all devices | and comment it ↓ */
				  token: req.token /*   <--------------------------------------------------------------------------------*/
			}      
			   await admin.messaging().send(payload)
				.then(response => valueGlobal = {data:["OK"]}) 		
				 .catch(error => valueGlobal = {data:["ERROR"]})
		}else{
		 var message = {
			android:{
			  notification:{
				title: "Title from Cloud",
				body: req.messageData[1],
				sound:"msn"
			  }
		  },
			token: req.token
		  }
			 await admin.messaging().send(message)
			 .then(response => valueGlobal = {data:["OK"]}) 		
				 .catch(error => valueGlobal = {data:["ERROR"]})
		}
		
		
	}
    
		
          console.log("response:", valueGlobal)		  
		return valueGlobal 
 })

