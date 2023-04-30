### Rust的开发环境与工具

##### 环境安装指导文档

##### LSP

##### Hello_World

使用Cargo：Rust默认的项目管理工具

```shell
cargo new hello
cd hello 
tree .
cargo build
```

### Rust基本数据类型

##### 类型系统概述

rust是静态强类型语言

##### 变量和可变性

```rust
// 常量在编译期间求值，不能把函数返回值作为常量
const A_CONST: i32 = 42;
fn get_number() -> i32 {
  42
}
fn main(){
  // 变量默认是不可变的，mut使变量可变
  let mut x = 5;
  println!("The value of x is {}", x);
  x = 6;
  println!("The value of x is {}", x);
  println!("The value of A_CONST is {}", A_CONST);
  let r = get_number();
  println!("The value of r is {}", r);
  // Shadowing 
  let x=5;
  let x=x+1;
  let x=x*2;
  println!("The value of x is {}", x);
}
// cargo run
```

##### 基础数据类型

```rust
fn main(){
  let x = 2.0; // f64
  let y : f32 = 3.0; // f32
  let t = true;
  let f: bool = false;
  let c = 'z';
}
```

##### 整数溢出

Hacker's Delight

```rust
fn main(){
	let a: u32 = "4294967295".parse::<u32>().unwrap();
  let b: u32 = 1;
  // let sum = a + b;
  let (sum, is_overflow) = a.overflowing_add(b);
  println!("sum = {:?}, is_overflow = {:?}", sum, is_overflow);
}
// cargo build --release
// ./target/release/rcrash
```

##### 元组

```rust
fn main() {
  let a: i32 = 10;
  let b: char = 'A';
  let mytuple: (i32, char) = (a, b);
  let (c, d) = mytuple;
  println!("c={} d={}", c, d);
  println!("mytuple.0 = {}", mytuple.0);
  println!("mytuple.1 = {}", mytuple.1);
  let (result, is_overflow) = a.overflowing_add(10);
  println!("{} {}", result, is_overflow);
}
```

##### 数组

```rust
fn main() {
  let myarray: [u32, 5] = [1, 2, 3, 4, 5];
  println!("myarray[1] = {}", myarray[1]);
  let index = "5".parse::<usize>().unwrap();
  println!("myarray[5] = {}", myarray[index]);
  let mut mybuffer : [u32; 32 * 1024] = [0: 32 * 1024];
  println!("mybuffer[1024] = {}", mybuffer[1024]);
  mybuffer[1024] = 13;
  println!("mybuffer[1024] = {}", mybuffer[1024]);
}
```

##### 切片类型

```rust
fn main() {
  let mut arr: [i32; 5] = [1, 2, 3, 4, 5];
  let slice = &arr[0..3]; // ...是Rust Range语法，&是引用符号
  println!("slice[0] = {}, len = {}", slice[0], slice.len());
  
  let slice2 = &arr[3..5];
  println!("slice2[0] = {} slice[1] = {}", slice2[0], slice2[1]);
  println!("slice2_len = {}", slice2.len());
  
  let mut slice3 = &mut arr[..];
  slice3[0] = 6;
  println!("arr[0] = {}", arr[0]);
}
//cargo run
```

##### 结构体

```rust
// 元组结构
struct Pair(i32, f32);
// 标准的C结构
#[derive(Debug)]
struct Person {
  name: String,
  age: u32,
}
// 单元结构（无字段）
struct Unit;
fn main() {
  let pair = Pair(10, 4.2);
  println!("{}", pair.0)
  let jack = Person{
    name: String::from('jack'),
    age: 6,
  }
  println!("name = {}, age = {}", jack.name, jack.age);
  println!("{:?}", jack);
  let unit = Unit;
}
```

##### 枚举

```rust
enum IpAddr {
  IPv4(u8, u8, u8, u8)
  IPv6(u8, u8, u8, u8, u8, u8, u8, u8, u8, u8, u8, u8, u8, u8, u8, u8)
}
fn main() {
  let localhost: IpAddr = IpAddr::IPv4(127, 0, 0, 1);
  match localhost {
    IpAddr::IPv4(a, b, c, d) => {
      println!("{} {} {} {}", a, b, c, d);
    }
    _ => {}
  }
}
```

##### 在不同类型之间转换

```rust
use std::mem;
fn main() {
  unsafe {
    let a = [0u8, 1u8, 0u8, 0u8];
    let b: u32 = mem::transmute(a);
    println!("{}", b);
  }
  let a: i8 = -10;
  let b = a as u8;
  println!("a = {}, b = {}", a, b);
}
```



### Rust流程控制

##### if_else选择结构

```rust
fn main() {
  let n = 5;
  
  if n > 0 {
		println!("{} is positive", n);
	} else if n < 0 {
    println!("{} is negative", n);
  } else {
    println!("{} is zero", n);
  }
  let m = if n < 0 {
    2.0
  } else {
    3.0
  };
}
```

##### 使用loop循环

```rust
fn main() {
  let mut sum = 0;
  let mut n = 1;
  loop {
    sum += n;
    n += 1;
    if n > 100 {
      break
    }
  }
  println!("1+2+...+100 = {}", sum);
  
  let mut counter = 0;
  let result = loop {
    counter += 1;
    if counter == 10 {
      break counter * 2;
    }
  };
  println!("result = {}", result);
}
```

##### 使用while循环

```rust
fn main() {
  let mut n = 1;
  while n < 101 {
    if n % 15 == 0 {
      println!("fizzbuzz");
    } else if if n % 3 == 0 {
      println!("fizz");
    } else if if n % 3 == 0 {
      println!("buzz");
    } else {
      println!("{}", n);
    }
    n += 1;
  }
}
```

##### 使用for_range进行迭代

```rust
fn main() {
  for i in 0..5 {
    println!("{}", i);
  }
  for i in 0..=5 {
    println!("{}", i)
  }
  println!(" --- ")
  let mut muarray = [1, 2, 3];
  for i in myarray.iter() {
    println!("{}", i);
  }
  for i in myarray.iter_mut() {
    *i *= 2;
  }
  for i in myarray.iter() {
    println!("{}", i);
  }
}
```

##### Rust中的match

```rust
enum Alphabet {
  A,
  B
}
fn main() {
  let letter = Alphabet::A;
  match letter {
    Alphabet::A => {
      println!("It is A");
    }
    Alphabet::B => {
      println!("It is B");
    }
  }
}
```

##### if_let语法糖

if let 是 Rust 中的一个语法糖，它主要简化了match操作。如果我们仅仅想当匹配发生时做某些操作，那么就可以使用 if let 替代 match。

```rust
enum Alphabet {
  A,
  B
}
fn main() {
  let letter = Alphabet::A;
  if let Alphabet::A = letter {
    println!("It is A");
  }
}
```

##### while_let语法糖

```rust
enum Alphabet {
  A,
  B
}
fn main() {
  let mut letter = Alphabet::A;
  while let Alphabet::A = letter {
    println!("It is A");
    letter = Alphabet::B;
  }
}
```

##### 函数与方法

```rust
#[derive(Debug)]
fn fibonacci(n: u64) -> u64 {
  if n < 2 {
    n
  } else {
    fibonacci(n - 1) + fibonacci(n - 2)
  }
}

struct Point {
  x: u64,
  y: u64,
}

impl Point {
  fn new(x: u64, y: u64) -> Point {
    Point { x, y }
  }
  fn get_x(&self) -> u64 {
    self.x
  }
  fn set_x(&mut self, x: u64) {
    self.x = x
  }
}

fn main() {
  println!("{:?}", fibonacci(10));
  let p = Point::new(10, 20);
  println("{:?}", p);
  println("{:?}", p.get_x());
  p.set_x(30);
  println("{:?}", p);
}
```

##### 函数与闭包

```rust
use std::thread;
fn main() {
  let myclosures = |n: u32| -> u32 { n * 3 };
  println!("{}", myclosures(1))
  
  let hello_message = "Hello World.";
  thread::spawn(move || {
    peintln!("{}", hello_message);
  }).join();
}
```

##### 高阶函数

- 把函数作为参数传递
- 把函数作为返回值

```rust
type Method = fn(u32, u32) -> u32;
fn calc(method: Method, a: u32, b: u32) -> u32 {
  method(a, b)
}
fn calc2(method: &str) -> Method {
  match method {
    "add" => add,
    "sub" => sub,
    _ => unimplemented!(),
  }
}
fn add(a: u32, b: u32) -> u32 {
  a + b
}
fn sub(a: u32, b: u32) -> u32 {
  a - b
}
fn main() {
  println!("{}", calc(add, 10, 20));
  println!("{}", calc(sub, 10, 20));
  println!("{}", calc2("sub")(10, 20));
}
```

##### 发散函数

发散函数永远不会被返回，他们的返回值被标记为｜，这是一个空类型

```rust
fn foo() -> ! {
  panic!("This call never returns.");
}
fn main() {
  let a = if true {
    10
  } else {
    foo()
  };
  println!("{}", a);
}
```

##### 实践:猜数字游戏

```rust
// Corgo.toml
rand = "*"

// main.rs
use rand::Rng;
use std::cmp::Ordering;
use std::io;

fn main() {
  println!("Guess the number!");
  // 获取随机整数
  let secret_number = rand::thread_rng().gen_range(1..101);
  loop {
    println!("Please input your guess.");
    let mut guess = String::new();
    io::stdin()
    	.read_line(&mut guess)
    	.unwrap()
    	.expect("Failed to read line");
    let guess: u32 = match guess.trim().parse() {
      Ok(num) => num,
      Err(_) => continue,
    };
    println!("You guessed: {}", guess);
    if guess > secret_number {
      println!("Too big.");
    } else if guess < secret_number {
      println!("Too small.");
    } else {
      println!("You win.");
      break;
    }
  }
}
```

