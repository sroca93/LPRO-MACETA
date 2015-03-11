
 //*****************************************************************************
 //                              LIBRARIES
 //*****************************************************************************
 
 #include <avr/sleep.h>
 #include <avr/power.h>
 #include <avr/wdt.h>  
 #include <DHT.h>
 #include <SoftwareSerial.h>
 //*****************************************************************************
 //*****************************************************************************

 //*****************************************************************************
 //                        CONSTANTS DEFINITION
 //*****************************************************************************

 // CONTROL 
 #define tam 3                   // MEASURE BUFFER SIZE
 
 // 555 TIMER
 #define wakePin 2               // INTERRUPT WAKEUP PIN
 #define trig    3               // 555 TIMER CIRCUIT TRIGGER PIN
 #define res     4               // 555 TIMER CIRCUIT RESET PIN

 // LED PINS
 #define ledSTUP 13              // SETUP LED PIN
 #define ledREAD 12              // READ SENSORS LED PIN
 #define ledTX   11              // TRANSMISION LED PIN
 
 // BLUETOOTH
 #define pow 7                   // BLUETOOTH POWER SUPPLY PIN
 #define BT_tx 9                 // BLUETOOTH SOFTWARE COM PINOUT
 #define BT_rx 10
 #define DevNo 0001              // DEFINE THE DEVICE NUMBER
 #define BLUETOOTH_SPEED 57600   // BLUETOOTH DEVICE BAUDRATE
 
 // SENSORS PINS
 #define DHTTYPE DHT22           // DHT 22  (AM2302) TYPE
 #define  DHTPIN 6               // PINS DE LOS SENSORES
 #define  L_PIN 0                // LDR PIN
//*****************************************************************************


//*****************************************************************************
 //               SOFTWARE SERIAL PORT INITIALIZATION
//*****************************************************************************

 //   Pin 9 --> Bluetooth RX
 //   Pin 10 --> Bluetooth TX
 SoftwareSerial BT(BT_rx, BT_tx); // RX, TX
//*****************************************************************************
//*****************************************************************************

//*****************************************************************************
 //                 DHT22 PINOUT AND TYPE DEFINITION
//*****************************************************************************

 // Connect pin 1 (on the left) of the sensor to +5V
 // Connect pin 2 of the sensor to where your DHTPIN is
 // Connect pin 4 (on the right) of the sensor to GROUND
 // Connect a 10K resistor from pin 2 (data) to pin 1 (power) of the sensor

 // Initialize DHT sensor for normal 16mhz Arduino
 
 DHT dht(DHTPIN, DHTTYPE);
//*****************************************************************************
//*****************************************************************************


//*****************************************************************************
//                           GLOBAL VARIABLES
//*****************************************************************************

 char frme_rx = 'D';         // HANDSHAKING CONNECTION BT VARIABLE

 // COUNTERS
 int pck = 0,mea = 0;        // PACK NUMBER AND MEASURE NUMBER

 // MEASURES
 float T[tam] = {};          // TEMPERATURE
 float H[tam] = {};          // HUMIDITY
 float F[tam] = {};          // TEMPERATURE (F)
 float HI[tam] = {};         // HEAT INDEX 
 float L[tam] = {};          //LIGHT LEVEL
 
 // AUX
 float l_aux =  0;
 int ind = 0 ;
//*****************************************************************************
//*****************************************************************************


 void setup() {
 
  // PIN MODES
  pinMode(ledSTUP, OUTPUT);      
  pinMode(ledREAD, OUTPUT);   
  pinMode(ledTX, OUTPUT);   
  pinMode(trig,OUTPUT);

  
  // INTERRUPT PIN MODE
  pinMode(wakePin, INPUT);     
  
  
  // TX RATES
  Serial.begin(9600);
  BT.begin(BLUETOOTH_SPEED);
  
   Serial.println("Init.\n");
   digitalWrite(ledSTUP,HIGH);
  
  // INIT OF THE DHT22 SENSOR AND BT CONNECTION  
  dht.begin();
  BTconn();
  
  // 555 TIMER CIRCUIT SETUP
  digitalWrite(trig,HIGH);    // DISABLES LOW LEVEL TRIGGERING
  digitalWrite(res,LOW);      // LOW LEVEL RESET ENABLE    
  delay(100);
  digitalWrite(res,HIGH);     // RESET SIGNAL END
  
  wdt_enable(WDTO_8S);        // WATCHDOG TIMER ENABLED 
 
 
  digitalWrite(ledSTUP,LOW);  // END OF SETUP ZONE
}
 
 void loop(){
   
 //***************************************************************************** 
 //                         SENSORS ADQUISITION ZONE
 //*****************************************************************************
 
  digitalWrite(ledREAD,HIGH);                // START OF THE ADQ TIME
    
  delay(500);                                // WAITS 0.5 S
  
  H[ind] = dht.readHumidity();   
  T[ind] = dht.readTemperature();            // READ TEMPERATURE AS CELSIUS
  F[ind] = dht.readTemperature(true);        // READ TEMPERATURE AS FAHRENHEIT
  
  //Heat index(temp must be in fahrenheit)  
  HI[ind] = dht.computeHeatIndex(F[ind], H[ind]);  
   
  // Check if any reads failed and exit early (to try again).
  if (isnan(H[ind]) || isnan(T[ind]) || isnan(F[ind])) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  } 
     
  l_aux=analogRead(L_PIN);                   // READ LIGHT LEVEL FROM THE ANALOG LDR
  L[ind] = map(l_aux,0,1023,0,100);          // MAPS THE PERCENTAGE
  
  delay(100);           
  
  ind++;                                     // POINTER TO THE NEXT MEASURE
  mea++;                                     // MEASURE COUNT   
  wdt_reset();                               // RESET THE WDT 

  digitalWrite(ledREAD,LOW);                 // END OF THE ADQ ZONE
 //*****************************************************************************
 //***************************************************************************** 
  
 //*****************************************************************************
 //                        SERIAL TRANSMISSION ZONE
 //*****************************************************************************
     
  if(ind == tam ){                           // IF THE BUFFER IS FULL

    wdt_reset();                               // RESET THE WDT
    pck++;                                     // PACK COUNTER
  
    digitalWrite(ledTX,HIGH);
  
   //*****************************************************************************
   //                          COM: SERIAL PORT DEBUG
   //*****************************************************************************
  
    Serial.println("\n\nHumidity: ");
  
    for(int ii=0;ii<tam;ii++)
    {
      Serial.print(H[ii]);
      Serial.println(" %");
    }
 
    delay(100);
  
    Serial.println("\n\nTemperature: ");
  
    for(int ii=0;ii<tam;ii++)
    {
      Serial.print(T[ii]);
      Serial.println(" C");
  
      Serial.print(F[ii]);
      Serial.println(" F");
    }
 
    delay(100);
  
    Serial.println("\n\nHeat Index: ");
  
    for(int ii=0;ii<tam;ii++)
    {
      Serial.print(HI[ii]);
      Serial.println(" u.");
    }
 
    delay(100);
  
    Serial.println("\n\nLuminance: ");
  
    for(int ii=0;ii<tam;ii++)
    {
      Serial.print(L[ii]);
      Serial.println(" %");
    }
    //*****************************************************************************
    //*****************************************************************************
    
    //*****************************************************************************
    //                         COM: SERIAL PORT BT
    //*****************************************************************************
    
     //*****************************************************************************
     // BT PORT FRAME:
     //              (DEVICE_No;FRME_No;SBF_No;MEA_No;HUM;TMP_C;TMP_F;HI;L)
     //*****************************************************************************
    
    wdt_reset();
    for(int ii=0;ii<tam;ii++)
    {    
      BT.print(DevNo);               // DEVICE NUMBER
      BT.print(";");                  
      BT.print(pck);                 // FRAME NUMBER
      BT.print(";");
      BT.print(ii);                  // SUBFRAME NUMBER 
      BT.print(";");
      BT.print(mea-ind+ii);          // MEASURE NUMBER
      BT.print(";");
      BT.print(H[ii]);               // HUMIDITY LEVEL
      BT.print(";");       
      BT.print(T[ii]);               // TEMPERATURE (CELSIUS)
      BT.print(";");              
      BT.print(F[ii]);               // TEMPERATURE (FAHRENHEIT)
      BT.print(";");
      BT.print(HI[ii]);              // HEAT INDEX
      BT.print(";");
      BT.print(L[ii]);               // LIGHT LEVEL 
      BT.println("");  
   }
    
    Serial.print("\nPack number: ");
    Serial.println(pck);
    Serial.print("\n\n");
    ind=0;                           // RESETS THE BUFFER
  }
  
  delay(100);
  digitalWrite(ledTX,LOW);
 //*****************************************************************************
 //*****************************************************************************
  
 //*****************************************************************************
 //                                 SLEEP ZONE
 //*****************************************************************************
  
  wdt_reset();
  
  Serial.println("Entering Sleep mode...");
  digitalWrite(trig,LOW);                      // LOW LEVEL 555 TIMER TRIGGER
  delay(100);
  digitalWrite(trig,HIGH);
  delay(200);

  sleepNow();  
}


 //*****************************************************************************
 // HANDSHAKING USED TO START THE MEASUREMENTS WHEN THE BT CONN IS ENABLED 
 // AND THE LINK IS SET.
 //*****************************************************************************
 void BTconn()
 {
   
  pinMode(pow,OUTPUT);
  digitalWrite(pow,HIGH); 
  while(1)                  // WILL LAST FOREVER
  {                
    while(BT.available())     // IF THERE'S SERIAL DATA TO READ
    {
      char rea=BT.read();     // THEN READ IT  
      if(rea=='E'){             // IF THE LINK IS ENABLED
      return;                 // ENDS THE WAITING
      }
    }
  }
}
 //*****************************************************************************
 //*****************************************************************************

 //*****************************************************************************
 //                          HANDLING INTERRUPT
 //*****************************************************************************
  
  void wakeUpNow()       
  {
    power_all_enable();    // ENABLES ALL MODULES

    
    wdt_enable(WDTO_8S);   // RE ENABLES WDT 
    wdt_reset();           //RESETS WDT 
    
    sleep_disable();       // DISABLES THE SLEEP MODE
                            
    detachInterrupt(0);    // DISABLES INTERRUPTS
 
  }
 //*****************************************************************************
 //*****************************************************************************
 
 //*****************************************************************************
 //                       DRIVES ARDUINO INTO SLEEP MODE
 //*****************************************************************************
 
 void sleepNow()
 {        
    /*
     *********************TUTORIAL PORTION************************* 
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
     
    set_sleep_mode(SLEEP_MODE_PWR_DOWN);   // SWITCH THE DESIRED SLEEP MODE
 
    sleep_enable();          // ENABLES SLEEP
 
    /* 
    *********************TUTORIAL PORTION**************************
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
 
     wdt_disable();                // DISABLES WDT
 
     power_all_disable();          // DISABLES ALL MODULES

	
	
    attachInterrupt(0,wakeUpNow, LOW); //INT 0 (PIN2)
                                       // wakeUpNow WHEN PIN2 GOES LOW PIN2 
 
    sleep_mode();            // IT GOES TO SLEEP
                             // CONTINUES FROM HERE WHEN AWAKE
}


