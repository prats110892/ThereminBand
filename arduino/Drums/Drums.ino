/*
  Analog Input
 Demonstrates analog input by reading an analog sensor on analog pin 0 and
 turning on and off a light emitting diode(LED)  connected to digital pin 13. 
 The amount of time the LED will be on and off depends on
 the value obtained by analogRead(). 
 
 The circuit:
 * Potentiometer attached to analog input 0
 * center pin of the potentiometer to the analog pin
 * one side pin (either one) to ground
 * the other side pin to +5V
 * LED anode (long leg) attached to digital output 13
 * LED cathode (short leg) attached to ground
 
 * Note: because most Arduinos have a built-in LED attached 
 to pin 13 on the board, the LED is optional.
 
 
 Created by David Cuartielles
 modified 30 Aug 2011
 By Tom Igoe
 
 This example code is in the public domain.
 
 http://arduino.cc/en/Tutorial/AnalogInput
 
 */

int cymbalPin = A0;    // select the input pin for the potentiometer
int drumPin = A5;
int ledPin = 13;      // select the pin for the LED
int cymbalValue = 0;  // variable to store the value coming from the sensor
int drumValue = 0; 
boolean drumDetected=false;
boolean cymbalDetected=false;
void setup() {
  // declare the ledPin as an OUTPUT:
  pinMode(ledPin, OUTPUT);  
  pinMode(cymbalPin, INPUT);
  pinMode(drumPin, INPUT);
  Serial.begin(9600);
}

void loop() {
  // read the value from the sensor:
  cymbalValue = analogRead(cymbalPin);  
  //Serial.println(sensorValue);
  delay(10);
  if(cymbalValue < 800&&cymbalDetected==false)
  {
    cymbalDetected=true;
    Serial.println("#c#");
  }
  else if (cymbalValue >900 &&cymbalDetected==true)
  {
    cymbalDetected=false;
  }
  
  drumValue = analogRead(drumPin); 
  if(drumValue < 800&&drumDetected==false)
  {
    drumDetected=true;
    Serial.println("#d#");
  }
  else if (drumValue >900 &&drumDetected==true)
  {
    drumDetected=false;
  }
                    
}
