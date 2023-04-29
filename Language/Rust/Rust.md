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
