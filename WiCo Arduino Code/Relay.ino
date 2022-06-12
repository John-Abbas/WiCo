

void initRelay(void)  {
  
  pinMode(Relay_1, OUTPUT);     // Initialize the Relay_1
  pinMode(Relay_2, OUTPUT);     // Initialize the Relay_2
  pinMode(Relay_3, OUTPUT);     // Initialize the Relay_3
  pinMode(Relay_4, OUTPUT);     // Initialize the Relay_4
  digitalWrite(Relay_1,HIGH);
  digitalWrite(Relay_2,HIGH);
  digitalWrite(Relay_3,HIGH);
  digitalWrite(Relay_4,HIGH);
}
