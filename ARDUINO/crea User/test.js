//DEPENDENCIES
fs = require('fs');
moment = require('moment');

//CONFIG PARAMETERS

var n = 2;
var interv = 1; //tiempo entre medidas, en minutos 
var minutes = 0.1; //minutes between every check
var path_file = "./capture.txt"; //watched file
var verbose = true; //do you want dates and event printed?


//AUX FUNCTIONS

//COMMENT, filtering log if unnecessary
var Comment = function(comment){
if(verbose) console.log("["+moment(new Date()).format("hh:mm")+"] [interval.js:log] "+comment);
}

function replaceAll(find, replace, str) {
  return str.replace(new RegExp(find, 'g'), replace);
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
	eventEmitter.on(current_time, imprimeBody);
	
	var options = {
  		host: '193.146.210.69',
  		port: 80,
  		path: '/consultas.php?consulta='+consulta,
  		method: 'GET'
		};
		
	
	for (key in parametros) {
    		if (parametros.hasOwnProperty(key)) {
        		options.path = options.path + "&"+key+"="+parametros[key];
    		}
	}  
	options.path = replaceAll('\n', '', options.path);
	options.path = replaceAll('\r', '', options.path);
	Comment("Iniciando consulta: "+ options.path);
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
function guardarPlanta(index){

	
Comment("DATOS A ENVIAR: ");
var mediaFluz = 50;
var mediaFhumedad = 50;
var mediaFhumedad2 = 50;
var mediaFtemperatura = 50;
var parametros = {
			plantName: "Bug"+index,
			plantTipo: "Bug",
			myId: "55",
			luz: (mediaFluz+ 10*(Math.random()-0.5)).toString(),
			humedad: (mediaFhumedad+ 10*(Math.random()-0.5)).toString(),
			humedad2: (mediaFhumedad2+ 10*(Math.random()-0.5)).toString(),
			temperatura: (mediaFtemperatura+ 10*(Math.random()-0.5)).toString()
		};
		Comment(parametros.toString());
		var miConsulta = "storeFakePlant"; 
		consultaPHP(miConsulta,parametros);



		


		

};

////PROGRAM

var i=0;
for(i=0; i<100; i++){
guardarPlanta(i);
}

