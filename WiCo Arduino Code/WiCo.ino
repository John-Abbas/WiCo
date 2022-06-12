/*-----------------------------
  Appliances Automation - WiCo
  -----------------------------
  Created : 20-March-2018

  -----------------------------
*/
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <WiFiClient.h>
#include <Wire.h>
#include <Adafruit_ADS1015.h>

WiFiServer server(8080);

String clientData = "";
bool clientConEst = false;

const int Relay_1 = 14;
const int Relay_2 = 12;
const int Relay_3 = 13;
const int Relay_4 = 0;

int16_t lineVolt , ch1_Curr, ch2_Curr, ch3_Curr, ch4_Curr;

bool Pow1 = false,Pow2 = false,Pow3 = false,Pow4 = false;



void setup() {
  initRelay();
  initADC(); 
  WiFi.mode(WIFI_AP);
  WiFi.softAP("WiCo", "12345678");
  server.begin();
  Serial.begin(9600);
  IPAddress IP = WiFi.softAPIP();
  Serial.flush();
  Serial.println();
  Serial.print("Server IP is: ");
  Serial.println(IP);

}

void loop() {
  
  // listen for incoming clients
  WiFiClient client = server.available();
  clientConEst = true;
  
  while(client.connected()){
    readADC();
    if((Pow1 || Pow2 || Pow3 || Pow4)&&(clientConEst)){   
      
      client.println("Voltage " + String(lineVolt));
      client.println("Channel1 Current " + String(ch1_Curr));
      client.println("Channel2 Current " + String(ch2_Curr));
      client.println("Channel3 Current " + String(ch3_Curr));     
      client.println("Channel4 Current " + String(ch4_Curr));

      delay(500);
    }
              
    while(client.available()){
    char Inp = client.read();
    clientData += Inp;
    }
  
    if(clientData != ""){
      clientData.trim();
      if(clientConEst == true){
        if(clientData == "ON1"){
          digitalWrite(Relay_1, LOW);
          client.println("Relay 1 Powered ON");
          Pow1 = true;
        }
        else if(clientData == "OFF1"){
          digitalWrite(Relay_1, HIGH);
          client.println("Relay 1 Powered OFF");
          Pow1 = false;
        }
        else if(clientData == "ON2"){
          digitalWrite(Relay_2, LOW);
          client.println("Relay 2 Powered ON");
          Pow2 = true;
        }
        else if(clientData == "OFF2"){
          digitalWrite(Relay_2, HIGH);
          client.println("Relay 2 Powered OFF");
          Pow2 = false;
        }
        else if(clientData == "ON3"){
          digitalWrite(Relay_3, LOW);
          client.println("Relay 3 Powered ON");
          Pow3 = true;
        }
        else if(clientData == "OFF3"){
          digitalWrite(Relay_3, HIGH);
          client.println("Relay 3 Powered OFF");
          Pow3 = false;
        }
        else if(clientData == "ON4"){
          digitalWrite(Relay_4, LOW);
          client.println("Relay 4 Powered ON");
          Pow4 = true;
        }
        else if(clientData == "OFF4"){
          digitalWrite(Relay_4, HIGH);
          client.println("Relay 4 Powered OFF");
          Pow4 = false;
        }
        else{
          client.println("Invalid Command");
        }
      }
      clientData = "";
    }
  }
}



