const functions = require('firebase-functions')
const express = require('express')
const fs = require('fs')
const path = require('path')
const app = express()

app.get('/android', (req, res) => {
  var auth = req.query.auth
  var name = req.query.name
    const argv = ["credentials.txt"]
    const filePath = path.join(__dirname, argv[0])
      fs.readFile(filePath, {encoding: 'utf-8'}, (err, data) =>{
        if(!err){
          if(auth === data){  
            if(name !== null){
              res.json({status:`200`, optional:`${name}`})
            }else{
              res.json({status:`200`, optional:`Type name in url query params`})
            }            
          }else{
            res.json({status:`500`, optional:`wrong credentials`})
          }
        }        
      })
})

app.get('/cached', (req, res) => {
    res.set('Cache-Control', 'public, max-age=30, s-maxage=60')
    res.json({timeStamp:`${Date.now()}`})
  })


exports.app = functions.https.onRequest(app)
