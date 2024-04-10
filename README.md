# TinkerScript
A dynamically-typed, interpreted, high-level, toy programming language.

## Table of Contents
- [About](#about)
- [Installation and Usage](#installation-and-usage)
- [Syntax](#syntax)
- [Contributing](#contributing)
- [License](#license)

## About

TinkerScript is a dynamically-typed, interpreted, high-level, toy programming language. Initially, it started as an implementation of a tree walk interpreter for the Lox programming language from the book [Crafting Interpreters](https://craftinginterpreters.com/). However, as I continued adding more features to it, the language began to diverge significantly from its original form. Therefore, I have renamed it to TinkerScript.

#### Features
- Comments 
  * Single line - `//`
  * Multi-line - `/* */`
- Primitive Data Types
  * Numbers 
  * Strings
  * Booleans
  * `nil`
- Operators 
  * Arithmetic -`+`, `-`, `*`, `/`, `%`,`**`
  * Relational - `==`, `!=`, `>`, `<`, `>=`, `<=`
  * Bitwise  - `|`, `^`, `&`, `~`
  * Logical  `or` (`||`) , `and` (`&&`), `!`
  * Assignment -`=`,`:=`, `+=`, `-=`, `*=`, `/=`, `%=`, `**=`, 
  `&=`, `|=`, `^=`, `||=`, `&&=`, `++`, `--`
  * Conditional - `? :`
  * Spread - `...`
- Variables
  * Declaration and initialization
  * Assignment
- Control flow
  * `if` and `else`
  * `while`
  * `for`
  * `break` and `continue`
  * `switch` `case`
- Functions 
  * Declaration
  * Function calls
  * Return values
  * Closures
  * Variadic functions
  * Function Expression
  * Lambdas
- Classes
  * Declaration
  * Fields
  * Methods
    * Instance methods
    * Static methods
    * Getters
  * Constructors
  * Inheritance
- Data structures
  *  Arrays 
  *  Maps
- Error Handling
  * `try` `catch`
  * `throw`
- I/O 
  * `print()` 
  * `println()`
  * `read()`
- Native functions
  * `clock()`
  * `len()`
  * `strlen()`
  * `string()`
  * `number()`
- Feature Rich REPL
  
## Installation and Usage
### From JAR file
1. Download the JAR file from the [releases](https://github.com/sravand123/TinkerScript/releases)
2. To start TinkerScript REPL,
```bash
java -jar TinkerScript.jar
```
3. To run a TinkerScript file:
```bash
java -jar TinkerScript.jar [file]
```

### From Source
Make sure you have Java 11 or higher and Maven installed on your machine
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
4. To start TinkerScript REPL:
```bash
  ./tinkerscript
```
5. To run a TinkerScript file:
```bash
  ./tinkerscript [file]
```

6. To run tests:

```bash
  mvn test
```
> Some of the testcases  in the repository were taken from [Tests](https://github.com/munificent/craftinginterpreters/tree/master/test) of Lox Interpreter and were modified to test the initial implementation.

## Syntax

### Primitive Data Types
#### Number
- All integers and decimals are considered as numbers

#### String
- Strings are enclosed in double quotes: `"Hello, World!"`
- Supports escaping characters: `\n`,`\r`, `\t`, `\b`, `\f` ,`\`, `"`
- Supports Unicode characters

#### Boolean
- `true`, `false`
####  nil
`nil` is a special value like null in other programming languages, that represents an empty value.

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
var x;
var y = "Hello, World!";
```
Variables can also be declared and initialized with a shorter syntax:
```javascript
x := 10;
y := "Hello, World!";
```

### Operators

Operators follow the same precedence as C.
#### Binary
Let `a` = 2 and `b` = 10
##### Arithmetic
<!-- Table of Operators -->
| Operator |   Description    | Example                |
| :-------- | :------- | :------------------------- |
| `+` | Addition | a + b is 12 |
| `-` | Subtraction | a - b is -8 |
| `*` | Multiplication | a * b is 20 |
| `/` | Division | a / b is 0.2 |
| `%` | Modulus |  b % a is 0 |
| `**` | Exponent | a ** b is 1024 |

##### Comparison

| Operator |   Description    | Example                |
| :-------- | :------- | :------------------------- |
| `>` | Greater than | a > b is false |
| `<` | Less than | a < b is true |
| `>=` | Greater than or equal to | a >= b is false |
| `<=` | Less than or equal to | a <= b is true |
| `==` | Equality | a == b is false |
| `!=` | Inequality | a != b is true |

##### Logical

| Operator |   Description    | Example                |
| :-------- | :------- | :------------------------- |
| `\|\|` (or) | Logical OR | a \|\| b is 2 |
| `&&` (and) | Logical AND | a && b is 10 |


##### Bitwise

| Operator |   Description    | Example                |
| :-------- | :------- | :------------------------- |
| `&` | Bitwise AND | a & b is 2 |
| `\|` | Bitwise OR | a \| b is 10 |
| `^` | Bitwise XOR | a ^ b is 8 |
#### Unary
| Operator |   Description    | Example                |
| :-------- | :------- | :------------------------- |
| `-` | Negation | -a is -2 |
| `!` | Logical complement | !a is false |
| `~` | Bitwise complement | ~a is -3 |



#### Assignment
##### Direct assignment
- Variables can be assigned  using the `=` operator:
```javascript
var x;
x = 10;
```
##### Compound Assignment
* Compound assignment operators are shorthand operators used  to combine an arithmetic or logical or bitwise operation with assignment.


Let `a := 2` and `b := 10`
| Assignment Operator  | Example                    |
| :-------- | :------------------------- |
| `+=` |`a += b` results in `a` being `12`. |
| `-=` |  `a -= b` results in `a` being `-8`. |
| `*=` | `a *= b` results in `a` being `20`. |
| `/=` |  `a /= b` results in `a` being `0.2`. |
| `%=` |  `b %= a` results in `b` being `0`. |
| `**=`|  `a **= b` results in `a` being `1024`. |
| `\|\|=` | `a \|\|= b` results in `a` being `2`. |
| `&&=` |  `a &&= b` results in `a` being `10`. |
| `&=`  | `a &= b` results in `a` being `2`. |
| `\|=` |  `a \|= b` results in `a` being `10`. |
| `^=`  |  `a ^= b` results in `a` being `8`. |

#### Increment and Decrement
##### Postfix
In postfix increment (`++`) /decrement (`--`), the value of the variable is used in the expression first, and then the value is incremented/decremented.
```javascript
x := 5;
y := x++; // y = 5, x = 6

```

##### Prefix
In prefix increment (`++`) /decrement (`--`),the value of the variable is incremented/decremented before the value is used in the expression.
```javascript
x := 5;
y := ++x; // y = 6, x = 6
```

#### Conditional Operator

```javascript
x := 5;
y := x > 5 ? 2*x : 3*x; // y = 15
```

### IO
#### Output
TinkerScript provides built-in functions for doing I/O.

- `print()` can be used to display output
```javascript
print("Hello, World!"); // prints "Hello, World!"

print("Meaning of Life", 42 ); // prints "Meaning of Life 42"
```
- `println()` can be used to print output with a new line
```javascript
println("Hello, World!"); // prints "Hello, World!" with a new line
```
#### Input
- `read()` can be used to read input.
```javascript
x := read(); // reads input
```
- Users can also provide prompt when reading input
```javascript
x := read("Enter a number: "); // reads input
```

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
cnt:= 0;
while (cnt < 10) {
  cnt++;
}
print(cnt); // prints 10
```

####  For
```javascript
for (i := 0; i < 5; i++) {
    print(i,"");
}
// prints 0 1 2 3 4
```

#### Break
```javascript
x := 0;
while (x < 5) {
  if (x == 3) {
    break;
  }
  print(x,"");
  x++;
}
// prints 0 1 2
```

#### Continue
```javascript
for (i := 0; i < 5; i++) {
  if (i == 3) {
    continue;
  }
  print(i,"");
}
// prints 0 1 2 4
```

####  Switch Case
```javascript
x := 1;
var y;
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
z := add(x, y); // returns 15
```

#### Closures
Functions support closures by retaining access to the enclosing scope where they were defined even after the enclosing scope is executed.

```javascript
fun outer() {
  outerVariable := 10;
  fun inner() {
    return outerVariable;
  }
  return inner;
}
outer()(); // returns 10
```

#### Function expression

```javascript
add := fun (x, y) {
    return x+y;
};
add(5,10); // returns 15
```

#### Lambda function

```javascript

add := (x, y) -> x + y;
add(5, 10); // returns 15
```

```javascript
adder := x -> y -> x + y;
adder(5)(10); // returns 15
```


#### Variadic functions

```javascript
fun sum(...values){
  total := 0;
  for(i:=0; i<len(values); i++){
    total += values[i];
  }
  return total;
}
sum(1, 2, 3, 4, 5); // returns 15
```

#### Spreading arguments

```javascript
fun add(x, y, z){
  return x + y + z;
}
x := [1, 2, 3];
add(...x); // returns 6
```




#### Native functions

| Function   | Arguments             | Return Type | Description                               |
|------------|-----------------------|-------------|-------------------------------------------|
| `string()` | `string`       | `string`    | Returns a string representation of the input value. |
| `number()` | `number`       | `number`    | Returns a numerical representation of the input value. |
| `clock()`  | -                     | `number`    | Returns the current time in seconds.      |
| `strlen()` | `string`         | `number`    | Returns the length of the input string.   |
| `len()`    | `Array`     | `number`    | Returns the length of the input array.    |
| `print()`  | `...any`      | -           | Prints the given values to the console.   |
| `println()`| `...any`      | -           | Prints the given values to the console, followed by a new line. |
| `read()`   | `string ?`                      | `string`    | Reads a line from input.                  |



### Error Handling
##### Catch errors
```javascript
try {
  println(2+[1,2,3]);
} catch (error) {
  print(error.message);  // prints Error: Operands must be two numbers or two strings.
}
```
##### Throw errors
```javascript
try {
  throw Error("error"); 
} catch (error) {
  print(error.message); // prints error
}
```

### Classes
#### Class declaration
```javascript
class Point { }
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
print(point.getX(), point.getY()); // 10 20
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
print(point.x, point.y); // 10 20
```

#### Inheritance
```javascript
class Rectangle {
    init(width, height) {
      this.width = width;
      this.height = height;
    }
    area() {
      return this.width * this.height;
    }
}
class Square < Rectangle {
    init(width) {
      super.init(width, width);
    }
}
print(Square(5).area()); // 25
```

#### Static methods
```javascript
class Point {
    init(x, y) {
      this.x = x;
      this.y = y;
    }
    static midPoint(point1, point2) {
       midX := (point1.x + point2.x) / 2;
       midY := (point1.y + point2.y) / 2;
      return  Point(midX, midY);
   }
}
point1 :=  Point(2, 2);
point2 :=  Point(0, 0); 
midPoint := (Point.midPoint(point1, point2));
print(midPoint.x, midPoint.y); // 1 1
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
array := [1.2, "hello", true, nil,[1,2,3]];
```

#### Array access
##### Indexing ( 0 based )
```javascript
array[0]; // 1.2
array[2]; // true
array[4]; // nil
```
##### Slicing 
```javascript
array := [1, 2, 3, 4, 5];
array[1:3]; // [2, 3]
array[3:]; // [4, 5]
array[:3]; // [1, 2, 3]
array[:]; // [1, 2, 3, 4, 5]
```

#### Using Spread in array
```javascript
array := [1, 2, 3, 4, 5];
newArray := [...array,6,7]; // newArray = [1, 2, 3, 4, 5, 6, 7]
```



#### Array built-in methods
```javascript
array := [1, 2, 3];
array.push(4); // array = [1, 2, 3, 4]
array.pop(); // 4
```

### Maps
#### Map declaration
```javascript
map := {"key1": "value1", 2: "value2", true: "value3", false: "value4"};
```

#### Map access
##### Get
```javascript
map["key1"]; // value1
map[2]; // value2
map[true]; // value3
map[false]; // value4
```

##### Set
```javascript
map["key1"] = 10;
map[2] =  nil;
map[true] = [1,2,3];
map[false] = {"a":1, "b":2};
```
#### Map Built-in methods
```javascript
map.keys(); // [key1, 2, false, true]
map.values(); // [10, nil, {a: 1, b: 2}, [1, 2, 3]]
```


## Contributing
If you find any bugs or have any features you would like to see added, feel free to open an issue or submit a pull request

## License
```
Copyright (c) 2024 Sravan Kumar Dasari

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
- sravandasari111@gmail.com
- [LinkedIn](https://linkedin.com/in/sravan-kumar-dasari-02ab93195)
