# TinkerScript

A dynamically-typed , high-level programming language


## Table of Contents
- [About](#about)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## About

TinkerScript is a dynamically-typed, high-level programming language written in Java. Initially, it started as an implementation of the Lox programming language from the book [Crafting Interpreters](https://craftinginterpreters.com/). However, as I continued adding more features to it, the language began to diverge significantly from its original form. So I have renamed it to TinkerScript.




##### Features from the book
- Variables
- Control flow (if, while, for, switch, try-catch and throw)
- Functions 
- Closures
- Classes
- Inheritance

##### Features added
- Multi-line comments
- Support for Escaping characters and Unicode characters in strings
- More Operators 
- Break and Continue
- Switch Case
- Try-Catch
- Ternary
- Spread operator
- Increment and Decrement
- Compound assignment operators
- Array
- Slice
- Map
- Variadic functions
- Lambdas
- Static methods
- Getters
- Reading user input
- Added more native functions
## Installation
### From JAR file
1. Download the JAR file from the [releases]
2. Run the JAR file using the following command:
```bash
java -jar TinkerScript.jar
```
### Using Maven
1. Clone the repository:
```bash
git clone https://github.com/sravand123/TinkerScript
```
2. Navigate to the project directory:
```bash
cd TinkerScript
```
3. Run the command:
```bash
  chmod +x tinkerscript
```
4. Run the command:
```bash
  ./tinkerscript
```


## Usage
To run the TinkerScript REPL, run the following command:
```bash
./tinkerscript
```
To run a TinkerScript file, run the following command:
```bash
./tinkerscript [file]
```
To run the TinkerScript tests, run the following command:
```bash
mvn test
```

## Syntax

### Primitive Data Types
####  `number`
- All integers and decimals are considered as numbers


####  `string`
- Strings are enclosed in double quotes: `"Hello, World!"`
- Supports escaping characters: `"\n"`, `"\r", "\t", "\b", "\f" ,"\\", "\"`
- Supports Unicode characters: `"\u0041"`

####  `boolean`
- `true` or `false`
####  `nil`
- `nil`


### Comment
#### Single Line
```javascript
// This is a comment
```
#### Multi Line
```javascript
/* This is a 
multi-line comment */
```
### Variables
Variables are declared using the `var` keyword: 
```javascript
var x = 10;
var y = "Hello, World!";
```
Variables can also be declared with a shorthand syntax:
```javascript
x := 10;
y := "Hello, World!";
```

### Operators
TinkerScript supports the following operators:
#### Binary
##### Arithmetic
- `+`, `-`, `*`, `/`, `%`
```javascript
x := 5;
y := 10;
z := x+y; // 15
```

##### Comparison
- `==`, `!=`, `>`, `<`, `>=`, `<=`
```javascript
x := 5;
y := 10;
z := x==y; // false
```

##### Logical
- `&&`, `||`
```javascript
x := 5;
y := 10;
z := x>5 && y<10; // true
```

##### Bitwise
- `&`, `|`, `^`
```javascript
x := 5;
y := 10;
z := x & y; // 0
```
#### Unary
##### Arithmetic
- `-`
```javascript
x := 5;
y := -x; // -5
```
##### Logical
- `!`
```javascript
x := true;
y := !x; // false
```
##### Bitwise
- `~`
```javascript
x := 5;
y := ~x; // -6
```

#### Increment and Decrement
##### Postfix
- `++`, `--`
```javascript
x := 5;
x++; // 6
x--; // 5
```

##### Prefix
- `++`, `--`
```javascript
x := 5;
++x; // 6
--x; // 5
```


#### Assignment
- `=`,
- `+=`, `-=`, `*=`, `/=`, `%=`, `&=`, `|=`, `^=` (compound assignment operators)
```javascript
x := 5;
x = 10;
x /= 5; // 2
```

#### Ternary
- `? :`
```javascript
x := 5;
y := x > 5 ? "x is greater than 5" : "x is less than or equal to 5"; // "x is greater than 5"
```

### IO
#### Output
- `print()` can be used to display output
```javascript
print("Hello, World!"); // prints "Hello, World!"
// Accepts multiple arguments
print("Hello", "World!"); // prints "Hello World!"
```
- `println()` can be used to print output with a new line
```javascript
print("Hello, World!"); // prints "Hello, World!" with a new line
```
#### Input
- `read()` can be used to read input from the user
```javascript
x := read(); // reads input
```
- User can optionally provide a prompt
```javascript
x := read("Enter a number: "); // reads input
```

- Operators follow the same precedence as C.
### Control Flow
####  If Else
```javascript
x := 10;
if (x > 5) {
  print("x is greater than 5");
} else if (x < 5) {
  print("x is less than 5");
} else {
  print("x is less than or equal to 5");
} // prints "x is greater than 5"
```

####  While
```javascript
x:= 0;
while (x < 10) {
  x++;
}
print(x); // prints 10
```

####  For
```javascript
for (i := 0; i < 10; i++) {
  print(i);
} // prints 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
```

#### Break
```javascript
x := 0;
while (x < 10) {
  if (x == 5) {
    break;
  }
  x++;
}
print(x); // prints 5
```

#### Continue
```javascript
for (i := 0; i < 5; i++) {
  if (i == 3) {
    continue;
  }
  print(i);
} // prints 0, 1, 2, 4
```

####  Switch Case
```javascript
x := 1;
switch (x) {
  case 0:
    print("x is 0");
    break;
  case 1:
    print("x is 1");
    break;
  default:
    print("x is neither 0 nor 1");
} // prints "x is 1"
```

#### Try Catch and Throw
##### Catch errors
```javascript
try {
  x := [1,2,3];
  x[100] = 10; // throws error
} catch (e) {
  print(e);  // Index 100 out of range.
}
```
##### Throw errors
```javascript
try {
  x := [1,2,3];
  throw "10"; // throws an error
} catch (e) {
  print(e); // prints 10
}
```
### Functions

#### Function declaration
```javascript
fun add(x, y) {
  return x + y;
}
```
#### Function call
```javascript
x := 5;
y := 10;
z := add(x, y); // 15
```

#### Function expression
```javascript
add := fun (x, y) {
    return x+y;
};
add(5,10); // 15
```

#### Lambda
```javascript

add := (x, y) -> x + y;
add(5, 10); // 15
```

#### Variadic functions
TinkerScript also supports variadic functions ( functions with variable number of arguments).

- Variable parameter can be defined using the spread `...` syntax
- A function can have only one variadic parameter
- The variadic parameter must be the last parameter
```javascript
fun sum(...values){
  total := 0;
  for(i:=0; i<len(values); i++){
    total += values[i];
  }
  return total;
}
sum(1, 2, 3, 4, 5); // 15
```

#### Spreading arguments
```javascript
fun add(x, y, z){
  total := 0;
  for(i:=0; i<len(values); i++){
    total += values[i];
  }
  return total;
}
x := [1, 2, 3];
add(...x); // 15
```

#### Closures
```javascript
fun outer() {
  outerVariable := 10;
  fun inner() {
    return outerVariable;
  }
  return inner;
}
outer()(); // 10
```


#### Native functions
- print - prints given values separated by a space
- println - prints given values separated by a space ended with a new line
- read - reads a line from standard input
- len - returns the length of a given array or string

### Classes

#### Class declaration
```javascript
class Point {
}
```



#### Class instantiation
```javascript
point :=  Point();
```

#### Properties
```javascript
point.x = 10;
point.y = 20;
```
#### Constructor
```javascript
class Point {
    init(x, y) {
      this.x = x;
      this.y = y;
    }
}
```
#### Methods
```javascript
class Point {
    getX() {
      return this.x;
    }
    getY() {
      return this.y;
    }
}
point :=  Point();
point.x = 10;
point.y = 20;
point.getX(); // 10
```
#### Constructor
```javascript
class Point {
    init(x, y) {
      this.x = x;
      this.y = y;
    }
}
point :=  Point(10, 20);
point.getX(); // 10
point.getY(); // 20
```

#### Static methods
```javascript
class Point {
    static midpoint(point1, point2) {
    const midX = (point1.x + point2.x) / 2;
    const midY = (point1.y + point2.y) / 2;
    return  Point(midX, midY);
   }
}
point1 :=  Point(2, 2);
point2 :=  Point(0, 0); 
Point.midpoint(point1, point2); // Point(1, 1)
```

#### Getters
```javascript
class Rectangle {
    init( width, height) {
      this.width = width;
      this.height = height;
    }
    area {
      return this.width * this.height;
    }
}
rectangle :=  Rectangle(2, 3);
println(rectangle.area); // 6
```

### Arrays

#### Array declaration
```javascript
array := [1, 2, 3, 4, 5];
```


#### Array access
```javascript
print(array[2]); // 3
```
#### Slice
```javascript
array := [1, 2, 3, 4, 5];
array[1:3]; // [2, 3]
array[3:]; // [4, 5]
array[:3]; // [1, 2, 3]
array[:]; // [1, 2, 3, 4, 5]
```
#### Spread
```javascript
array := [1, 2, 3, 4, 5];
newArray := [...array,6,7]; // [1, 2, 3, 4, 5, 6, 7]
```



#### Array built-in methods
```javascript
array := [1, 2, 3, 4, 5];
array.push(6); // [1, 2, 3, 4, 5, 6]
array.pop(); // 6
array.length(); // 5
```

### Maps

#### Map declaration
```javascript
map := {"key1": "value1", 2: "value2", true: "value3", false: "value4"};
```

#### Map access
##### Get
```javascript
print(map["key1"]); // value1
print(map[2]); // value2
print(map[true]); // value3
print(map[false]); // value4
```

##### Set
```javascript
map["key1"] = "value1";
map[2] = "value2";
map[true] = "value3";
map[false] = "value4";
```
#### Map Built-in methods
```javascript
map.keys(); // [key1, 2, true, false]
map.values(); // ["value1", "value2", "value3", "value4"]
```


## Contributing
Pull requests are always welcome.

## License
```
Copyright (c) 2012-2024 Scott Chacon and others

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```

## Acknowledgements
 - [Crafting Interpreters](https://craftinginterpreters.com)



## Contact Me
- Email: sravandasari111@gmail.com
- [LinkedIn](https://linkedin.com/in/sravan-kumar-dasari-02ab93195)