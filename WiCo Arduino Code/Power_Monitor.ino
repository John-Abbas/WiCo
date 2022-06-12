Adafruit_ADS1115 Volt(0x49);  /* AC Voltage (ADC) Sensor */
Adafruit_ADS1115 Curr(0x48);  /* AC Current (ADC) Sensor */

#define LINE_VOLT 0

#define CURR_LINE1  0
#define CURR_LINE2  1
#define CURR_LINE3  2
#define CURR_LINE4  3

void initADC(void)  {
  
  Volt.setGain(GAIN_TWOTHIRDS);  // 2/3x gain +/- 6.144V  1 bit = 0.1875mV (default)
  Curr.setGain(GAIN_ONE);        // 1x gain   +/- 4.096V  1 bit = 0.125mV
  Volt.begin();
  Curr.begin();
}

void readADC(void)  {
  
  lineVolt = Volt.readADC_SingleEnded(LINE_VOLT);  
  ch1_Curr = Curr.readADC_SingleEnded(CURR_LINE1);
  ch2_Curr = Curr.readADC_SingleEnded(CURR_LINE2);
  ch3_Curr = Curr.readADC_SingleEnded(CURR_LINE3);
  ch4_Curr = Curr.readADC_SingleEnded(CURR_LINE4);
}

