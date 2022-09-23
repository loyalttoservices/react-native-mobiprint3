# react-native-mobiprint3plus

## Getting started

`$ npm install react-native-mobiprint3plus --save`

### Mostly automatic installation

`$ react-native link react-native-mobiprint3plus`

## Usage

```javascript
import Mobiprint3plus from "react-native-mobiprint3plus";

// TODO: What to do with the module?
const print = () => {
  Mobiprint3plus.connectPOS();
  //second step
  Mobiprint3plus.printImage(byteData);
  Mobiprint3plus.printHeader("Company");
  Mobiprint3plus.printText("Hello dear!");
  Mobiprint3plus.printLine();
  Mobiprint3plus.printText("How are you?");
  //second finale
  Mobiprint3plus.print();
};
```
