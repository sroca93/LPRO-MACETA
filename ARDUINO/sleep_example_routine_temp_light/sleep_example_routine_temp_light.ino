
#include <avr/sleep.h>

//CONSTANTES QUE NO CAMBIAN
const int wakePin = 2;       // PIN DEL BOTON

const int ledSTUP =  13;     // PINS DE LOS LEDS
const int ledREAD = 12;
const int ledSLE = 11;

const int T_PIN = 0;         // PINS DE LOS SENSORES
const int L_PIN = 1;

// VARIABLES

int var = 0;
float Tmp = 0;
float light = 0;
float t_aux = 0;
float l_aux =  0;

void setup() {
  
  //PIN DEL LED COMO SALIDA
  pinMode(ledSTUP, OUTPUT);      
  pinMode(ledREAD, OUTPUT);   
  pinMode(ledSLE, OUTPUT);   
  
  //PIN DEL BOTON COMO ENTRADA
  pinMode(wakePin, INPUT);     
  
  //TASA DE TX
  Serial.begin(9600);
  
  digitalWrite(ledSTUP,HIGH);
  delay(2000);
  digitalWrite(ledSTUP,LOW);

}
 
void loop(){
    
    /*ZONA DE LECTURA DE LOS SENSORES*/
    
   digitalWrite(ledREAD,HIGH);    //Comienza la lectura de datos
    
   delay(2000);                   //tiempo de espera
   t_aux=analogRead(T_PIN);       //lectura de T analogica 0-1023 
    
   Tmp = 5*100*t_aux/1024; //10mV -> 1 ºC, con eso escalamos 
  
  //COM: puerto serie de los datos de T 
  Serial.print("Temperature: ");
  Serial.print(Tmp);
  Serial.println(" C");
  delay(100);
  
  l_aux=analogRead(L_PIN);    
  light = map(l_aux,0,1023,0,100);
  
  //COM: Puerto serie de los datos de L
  Serial.print("Luminance: ");
  Serial.print(light);
  Serial.println(" %");
  
  
  delay(100);
  digitalWrite(ledREAD,LOW);
  
    /********************************/
    
     /*ZONA DE TRANSMISIÓN DE INFORMACIÓN*/
    
    var++;
    Serial.print("Measure number ");
    Serial.println(var);

    /********************************/
    
    
  digitalWrite(ledSLE,HIGH);
  Serial.println("Entering Sleep mode...");
  Serial.print("\n\n");
  delay(1000);
  digitalWrite(ledSLE,LOW);
  delay(200);
  
  sleepNow();  
 
 
}


void wakeUpNow()        //GESTION DE LA INTERRUPCION DESPUES DE DESPERTAR
{
  /*AQUI NO VAN A FUNCIONAR TIMESR NI NADA QUE DEPENDA DE RELOJ*/
    
    sleep_disable();         // LO PRIMERO AL DESPERTAR, DESACTIVAR EL SUEÑO
                            
    detachInterrupt(0);      // DESACTIVA INT0 PARA QUE ESTA RUTINA NO SE EJECUTE NORMALMENTE
 
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
