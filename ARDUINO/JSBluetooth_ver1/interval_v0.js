//DEPENDENCIES
fs = require('fs');
moment = require('moment');

//CONFIG PARAMETERS
var minutes = 0.3; //minutes between every check
var path_file = "./test.txt"; //watched file
var verbose = true; //do you want dates and event printed?
var n = 3;

//AUX FUNCTIONS

//COMMENT, filtering log if unnecessary
var Comment = function(comment){
if(verbose) console.log("["+moment(new Date()).format("hh:mm")+"] [interval.js:log] "+comment);
}

//CALLBACK, the function looking for modifications.
var callback = function(){
	Comment("Checking...");
	var old = new Date(fecham).getTime();
	var newone = new Date(fs.statSync(path_file).mtime).getTime();
	if(old!=newone){
		Comment("File has been modified");
		fecham = newone;
		Comment("New last modified date: "+ moment(fecham).format('MMMM Do YYYY, h:mm:ss a'));

		//DO HERE WHATEVER YOU WANT,
		//SENDING MEASURES FOR INSTANCE.
		
		guardarMedicion();
		

		
	}else{
		Comment("Nothing has changed.");
		Comment("LastMeasure: ");
		ultimaMedicion();
	}
};


////PROGRAM

console.log("\n\n\n");
var fecham = fs.statSync(path_file).mtime;
Comment('Original last modification date: '+moment(fecham).format('MMMM Do YYYY, h:mm:ss a'));

setInterval(callback, minutes*60*1000);

//CONSULTA UTILS
var http = require("http"); //para hacer peticiones http
var moment = require("moment");
var events = require('events'); //para poder lanzar eventos personalizados
var eventEmitter = new events.EventEmitter();



function imprimeBody(current_time, body){
Comment("IMPRIMIENDO BODY del evento: "+current_time );
Comment(body.replace(/[^\x20-\x7E]+/g, ''));
eventEmitter.removeListener(current_time, imprimeBody);

};

//CONSULTA GENÉRICA
function consultaPHP(consulta,parametros){
	var current_time = Date.now();
	Comment("Iniciando consulta "+current_time+"...");
	eventEmitter.on(current_time, imprimeBody);
	
	var options = {
  		host: '193.146.210.69',
  		port: 80,
  		path: '/consultas.php?consulta='+consulta,
  		method: 'GET'
		};
		
		Comment("LA URLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL " + options.path)

	for (key in parametros) {
    		if (parametros.hasOwnProperty(key)) {
        		options.path = options.path + "&"+key+"="+parametros[key];
    		}
	}  

	Comment("LA URLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL " + options.path)


	var req = http.request(options, function(res) {
  		Comment('RESPUESTA: ' + res.statusCode);
  		//Comment('HEADERS: ' + JSON.stringify(res.headers));
  		res.setEncoding('utf8');
  		res.on('data', function (chunk) {
    			//Comment('BODY: ' + chunk);
			eventEmitter.emit(current_time, current_time, chunk);
  		});
		
	});

	req.on('error', function(e) {
  		Comment('problem with request: ' + e.message);
	});
	req.end();
}


//GUARDARMEDICION
function guardarMedicion(){

	fs.readFile(path_file, 'utf8', function(err, data) {
			  if (err) throw err;
			  Comment('OK: ' + path_file);
			  Comment(data);
var lines = data.split('\n');

Comment("primer spliiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiit");
for (holis in lines){
Comment(lines[holis]);
}


for(line in lines){
	var pieces = lines[line].split(';');
	Comment(pieces);
	var parametros = {
			temperatura: (pieces[5]),
			luminosidad: (pieces[8]),
			humedad: (pieces[4]),
			plantID: pieces[0]
		};
		Comment(parametros.toString());
		var miConsulta = "guardarMedicion"; 
		consultaPHP(miConsulta,parametros);
		//setTimeout(function(){},3000);

	}
	
		});
		

};


//ULTIMAMEDICION
function ultimaMedicion(){
	var parametros = {
			plantID: 3,
			numero: 1
		};

		var miConsulta = "obtenerMediciones"; 
		consultaPHP(miConsulta,parametros);	

};
