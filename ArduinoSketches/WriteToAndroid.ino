#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>
 
AndroidAccessory acc("Manufacturer",
		     "Model",
		     "Description",
		     "1.0",
		     "http://yoursite.com",
		     "0000000012345678");
void setup()
{
  Serial.begin(115200);
  acc.powerOn();
}
 
void loop()
{
  byte msg[1]; // one byte
  int value=100; // value to send,we'll increment and decrement this variable
  if (acc.isConnected()) 
  {
    // is connected
    while(value>0)
    {
      // count down
      msg[0] = value;
      acc.write(msg, 1);
      delay(1000);
      value-=1;
    }
    
    while(value<=10)
    {
      // count up
      msg[0] = value;
      acc.write(msg, 1);
      delay(1000);
      value+=1;
    }
  }
}
