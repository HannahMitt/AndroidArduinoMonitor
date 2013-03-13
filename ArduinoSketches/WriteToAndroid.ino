#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>


AndroidAccessory acc("Manufacturer",
		     "Model",
		     "Description",
		     "1.0",
		     "http://yoursite.com",
		     "0000000012345678");

int pirPin = 0; //analog 0

void setup()
{
  Serial.begin(115200);
  acc.powerOn();
  pinMode(pirPin, INPUT);
}
 
void loop()
{
  byte msg[1]; // one byte
  
  if (acc.isConnected()) 
  {
    int pirVal = analogRead(pirPin);
    
    if(pirVal > 100){
      msg[0] = 1;
    } else {
      msg[0] = 0;
    }
      acc.write(msg, 1);
      delay(2000);
  }
}
