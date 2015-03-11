//DEPENDENCIES
fs = require('fs');
moment = require('moment');
var n = 3;
//CONFIG PARAMETERS
var minutes = 0.1; //minutes between every check
var path_file = "./capture.txt"; //watched file
var verbose = true; //do you want dates and event printed?

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
		//ultimaMedicion();
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

//GUARDARMEDICION
function guardarMedicion(){

	fs.readFile(path_file, 'utf8', function(err, data) {
			  if (err) throw err;
			  Comment('OK: ' + path_file);
			  //Comment(data);
var pieces = data.split("\n");

var send="";
for(i=0;i<=n;i++){
send+=pieces.pop()+'*';
}

send = send.substring(0, send.length-1);
send= send.replace('\n','');

Comment(send);

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