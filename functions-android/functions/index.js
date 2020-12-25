const functions = require('firebase-functions')
const getJSON = require('get-json')

var d = new Date(),		
    month = `${(d.getMonth() + 1)}`,
    day =   `${(d.getDate() + 1)}`,
    year =      d.getFullYear(),
    hour =      d.getHours(),
    minute =      d.getMinutes(),
    seconds =      d.getSeconds()     
    if (month.length < 2) month = '0' + month
    if (day.length < 2) day = '0' + day        
        var dateFormat = [year, month, day, hour, minute, seconds].join('-')
        let globalReturn
        exports.functionsData = functions.https.onCall(async (req, res) => {
            if(req !== null){
                //http://www.floatrates.com/daily/usd.json
                await getJSON('https://economia.awesomeapi.com.br/'+`${req.data}`+'/1')
                    .then(response => 
                        globalReturn = {data : [`${req.data}`, JSON.stringify(response)]}
                    ).catch(error => 
                        globalReturn = {data : [`${req.data}`, error]}
                        )

            }else{
                globalReturn = {data : ["singleCall", dateFormat]} 
            }       
          //console.log(`globalReturn ${globalReturn.data[1]}`)
        return globalReturn
        })
