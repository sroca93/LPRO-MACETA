
#include <avr/sleep.h>

//CONSTANTES QUE NO CAMBIAN
const int wakePin = 2;       // PIN DEL BOTON
const int ledPin =  13;      // PIN DEL LED


// VARIABLES
int sleepStatus = 0;         //PETICION DE "SUEÑO"
int count = 0;               //CONTADOR DE SEGUNDOS

void wakeUpNow()        //GESTION DE LA INTERRUPCION DESPUES DE DESPERTAR
{
  /*AQUI NO VAN A FUNCIONAR TIMESR NI NADA QUE DEPENDA DE RELOJ*/
    
    sleep_disable();         // LO PRIMERO AL DESPERTAR, DESACTIVAR EL SUEÑO
                            
    detachInterrupt(0);      // DESACTIVA INT0 PARA QUE ESTA RUTINA NO SE EJECUTE NORMALMENTE
 
}

void setup() {
  
  //PIN DEL LED COMO SALIDA
  pinMode(ledPin, OUTPUT);      
  //PIN DEL BOTON COMO ENTRADA
  pinMode(wakePin, INPUT);     
  
  //TASA DE TX
  Serial.begin(9600);
  
  attachInterrupt(0, wakeUpNow, LOW); 
  /*HABILITA INTERRUPCIONES EN INT0(PIN2) EJECUTANDO LA FUNCION wakeUpNow CUANDO RECIBE UN LOW*/
  
}

void sleepNow(){         // ARDUINO A DORMIR


    /*
     *********************EXTRACTO DEL MANUAL************************** 
     * Now is the time to set the sleep mode. In the Atmega8 datasheet
     * http://www.atmel.com/Images/Atmel-8271-8-bit-AVR-Microcontroller-ATmega48A-48PA-88A-88PA-168A-168PA-328-328P_datasheet.pdf 
     * page 38 there is a list of sleep modes which explains which clocks and
     * wake up sources are available in which sleep mode.
     *
     * In the avr/sleep.h file, the call names of these sleep modes are to be found:
     *
     * The 5 different modes are:
     *     SLEEP_MODE_IDLE         -the least power savings
     *     SLEEP_MODE_ADC
     *     SLEEP_MODE_PWR_SAVE
     *     SLEEP_MODE_STANDBY
     *     SLEEP_MODE_PWR_DOWN     -the most power savings
     *
     * For now, we want as much power savings as possible, so we
     * choose the according
     * sleep mode: SLEEP_MODE_PWR_DOWN
     *
     */  
     
    set_sleep_mode(SLEEP_MODE_PWR_DOWN);   // SE ESCOGE EL MODO DESEADO
 
    sleep_enable();          // SE ACTIVA LA POSIBILIDAD DE MODO SLEEP
 
    /* 
    *********************EXTRACTO DEL MANUAL**************************
     * Now it is time to enable an interrupt. We do it here so an
     * accidentally pushed interrupt button doesn't interrupt
     * our running program. if you want to be able to run
     * interrupt code besides the sleep function, place it in
     * setup() for example.
     *
     * In the function call attachInterrupt(A, B, C)
     * A   can be either 0 or 1 for interrupts on pin 2 or 3.  
     *
     * B   Name of a function you want to execute at interrupt for A.
     *
     * C   Trigger mode of the interrupt pin. can be:
     *             LOW        a low level triggers
     *             CHANGE     a change in level triggers
     *             RISING     a rising edge of a level triggers
     *             FALLING    a falling edge of a level triggers
     *
     * In all but the IDLE sleep modes only LOW can be used.
     */
 
    attachInterrupt(0,wakeUpNow, LOW); //SE USA INT0(PIN2)  
                                       // wakeUpNow CUANDO PIN2 SE PONE A BAJO NIVEL
 
    sleep_mode();            // SE PONE A DORMIR
                             //CONTINUA A PARTIR DE AQUI CUANDO EL SUEÑO ACABA
}
 

void loop(){
    
  
  /*ZONA DE EJEMPLO, EN EL DEFINITIVO AQUI VA LA LECTURA DE SENSORES Y EL ENVIO A ARDUINO*
  *
  * Lo que hace este pedazo de codigo es encender un led, contar 10 s y ponerse a dormir apagandolo antes, cuando se pulsa
  * el boton, este pasa de nivel alto a bajo, lanzandose ina interrupcion que es capturada en int0
  */
  
    digitalWrite(ledPin, HIGH);  
    count++;
  Serial.print("Awake for ");
  Serial.print(count);
  Serial.println("sec");
    delay(1000);   
    
    if(count>=10){
      Serial.println("Timer: Entering Sleep mode");
      delay(100);     // this delay is needed, the sleep
                      //function will provoke a Serial error otherwise!!
      count = 0;
      digitalWrite(ledPin, LOW);  
      sleepNow();     // sleep function called here
    }
    
    /************************************************************************************/
    
    /*ZONA DE LECTURA DE LOS SENSORES*/
    
    
    /********************************/
    
    
     /*ZONA DE TRANSMISIÓN DE INFORMACIÓN*/
    
    
    /********************************/
    
    
    
    
    
}
