# react-native-mobiprint3plus

## Getting started

`$ npm install react-native-mobiprint3plus --save`

### Mostly automatic installation

`$ react-native link react-native-mobiprint3plus`

## Usage

```javascript
import Mobiprint3plus from "react-native-mobiprint3plus";

// TODO: What to do with the module?
const print = async () => {
  await Mobiprint3plus.connectPOS();
  //second step
  await Mobiprint3plus.printImage(/* Image by bytes data */ byteData);
  await Mobiprint3plus.printHeader("CompanyName");
  await Mobiprint3plus.printText("Hello dear!"); /* simple text */
  await Mobiprint3plus.printLine();
  await Mobiprint3plus.printCustomText(
    /* Line of text */ "How are you?",
    /* FontSize : normal = 0, small = 1, medium = 2, large = 3 */ 0,
    /* left = 0 , right = 1 */ 0,
    /* center = 1, null = 0 */ 0,
    /* isBold */ false,
    /* isUnderLine */ false
  );
  await Mobiprint3plus.printLine();
  await Mobiprint3plus.printCustomText("Happy coding!!", 0, 0, 0, false, false);
  await Mobiprint3plus.printCustomText("Happy coding!!", 1, 0, 0, false, false);
  await Mobiprint3plus.printLine();
  await Mobiprint3plus.printCustomText("Cool code!!", 1, 0, 1, true, false);
  await Mobiprint3plus.printLeftText(
    /* Line of text */ "Cool code left!!",
    /* FontSize : normal = 0, small = 1, medium = 2, large = 3 */ 0,
    /* isBold */ true,
    /* isUnderLine */ false
  );
  await Mobiprint3plus.printLeftText(
    /* Line of text */ "Cool code!!",
    /* FontSize : normal = 0, small = 1, medium = 2, large = 3 */ 1,
    /* isBold */ false,
    /* isUnderLine */ false
  );
  await Mobiprint3plus.printRightText(
    /* Line of text */ "Cool code right!!",
    /* FontSize : normal = 0, small = 1, medium = 2, large = 3 */ 2,
    /* isBold */ false,
    /* isUnderLine */ true
  );
  await Mobiprint3plus.printCenterText(
    /* Line of text */ "Cool code center!!",
    /* FontSize : normal = 0, small = 1, medium = 2, large = 3 */ 3,
    /* isBold */ true,
    /* isUnderLine */ false
  );
  //finale step
  await Mobiprint3plus.print();
};
```
