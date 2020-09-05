#include <SPI.h>
#include <MFRC522.h>
#include <SoftwareSerial.h>

#define RST_PIN         9          // Configurable, see typical pin layout above
#define SS_PIN          10         // Configurable, see typical pin layout above

MFRC522 mfrc522(SS_PIN, RST_PIN);  // Create MFRC522 instance
SoftwareSerial EEBlue(3, 4); // RX | TX

boolean newCard = false;
int noCardCounter = 0;

void setup()
{ 
  Serial.begin(9600);   // Logging to terminal for debugging 
  EEBlue.begin(9600);   // Default Baud for comm, it may be different for your Module. 
  Serial.println("The bluetooth gates are open.\n Connect to HC-05 from any other bluetooth device with 1234 as pairing key!.");

  SPI.begin();          // Init SPI bus
  mfrc522.PCD_Init();   // Init MFRC522
  delay(4);             // Optional delay. Some board do need more time after init to be ready, see Readme
  mfrc522.PCD_DumpVersionToSerial();  // Show details of PCD - MFRC522 Card Reader details
  Serial.println(F("Scan PICC to see UID, SAK, type, and data blocks..."));
}

void loop() {
  if (mfrc522.PICC_IsNewCardPresent()&&mfrc522.PICC_ReadCardSerial()) {  
    noCardCounter = 0;
    String cardUid;
    for (byte i = 0; i < mfrc522.uid.size; i++) {
      //Serial.print(mfrc522.uid.uidByte[i], HEX);
      cardUid+= String(mfrc522.uid.uidByte[i], HEX);
    }
    // check if there was a significant time when there was no card
    // this time is 3 loops in this case. We need to make sure at least two-three
    // iterations of no card present before we know the card was removed
    // as long as the PICC_IsNewCardPresent switches back and forth every iteration
    // we can assume that the card has not been removed (hence the delay check)
    if (!newCard) {
      return;
    }

    EEBlue.println(cardUid);
    Serial.println("{" + cardUid + "}");
  } else {
   // Serial.println("nothing");
  }

  // if no card counter > 3 then we know that there is no card present
  // the PICC_IsNewCardPresent is not stable enugh to trust, it will 
  // flick between true and false
  noCardCounter += 1;
  if(noCardCounter > 3) {
    newCard = true;
  } else {
    newCard = false;
  }   
}
