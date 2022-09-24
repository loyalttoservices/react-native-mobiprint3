# react-native-mobiprint3plus

## Getting started

Install via NPM [Check In NPM](https://www.npmjs.com/package/react-native-mobiprint3plus)
```bash
npm install react-native-mobiprint3plus --save
```

### Mostly automatic installation

```bash
react-native link react-native-mobiprint3plus
```

## Usage

```javascript
import Mobiprint3plus from "react-native-mobiprint3plus";

// TODO: What to do with the module?
const print = async () => {
  await Mobiprint3plus.connectPOS();
  //second step
  const base64Image = "iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAA8FBMVEUAAABCQkJDQ0NFRUU/Pz9BQUFAQEBERERDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0MAAAA0ZZMIAAAATnRSTlMAAAAAAAAAABWFz8JdBQFHt9OYIxSi/PBsBFHjvCSk/vJt5b7mo26h75ziIZkD1csRXvpziwvx+QadveRSSA3XF6r31DMPOSLWzMTZFgd4wftfAAAAAWJLR0QAiAUdSAAAAAlwSFlzAAALEgAACxIB0t1+/AAAAaBJREFUSMe11dlSwjAUgOE2WmUTQRBtBQVBREREQEVUFkHcz/s/jklbQ7YOhwtz2fzftJ1OTi0rWDaJxRPJ1A6xxEXSu5nsXo7Ylrpskt8vABwcuqIgG94RABRLmtgk+eMTugXliiAI8U7ZRaiqwvnrJUH7WnBRFfR5zsKeinoohN4XRHyeZc8F2RJ6SSh9KJReeCpH7QOh9st76L3/5lrPRf5c6wEaF039IlQvmYgXAL1aVxQk8D20YxQk1wDXHQpuGui+22Pv4FbK2L5/639Rt44TYY8WvEcKoUcJqUcIpV8ptN4Xd5H9vd5TMXiIBMOOoXe8x0igzJKgf6pB9JJmCaIXJkPYb6/oFYHoJYHqxXllo/qlcDxcz8VzE9lTkWInLoPuAZIjCrJrgPGEgtYaYDqgIFc07LwMTbNkNmfvQEpVbafbfzXMkvbCn622Lth50adP2BuEf740MVvwP4oi+LyShNArQphXgpB69v/jQppXXCi9IJR5FQqt50KbV74w9Ey8td4/etq8Sn1+TeeGngn3u5PW7myPJj/G/v/WL4DMswebZ4AxAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE1LTA2LTI1VDA4OjQ0OjQ2KzA4OjAww1b9dwAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNS0wNi0yNVQwODo0NDo0NiswODowMLILRcsAAAAASUVORK5CYII=";

  await Mobiprint3plus.printImage(
    /* Image base64 encoder */ base64Image,
    /* Image option */ { width: 200, left: 40 }
  );
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
  let columnWidths = [12, 6, 6, 8];
  await Mobiprint3plus.printColumn(
    columnWidths,
    [0 /* left */, 1 /* center */, 1 /* center */, 2 /* right */],
    ["Produit", "quantité", "Prix ​​unitaire", "Montant"],
    {}
  );
  //finale step
  await Mobiprint3plus.print();
};
```
