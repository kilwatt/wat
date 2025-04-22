<p align="center">
  <pre>
 __          __  _______ _______ 
 \ \        / /\|__   __|__   __|
  \ \  /\  / /  \  | |     | |   
   \ \/  \/ / /\ \ | |     | |   
    \  /\  / ____ \| |     | |   
     \/  \/_/    \_\_|     |_|   
  </pre>
  <h1 align="center">⚡🍹 Watt</h1>
  <p align="center"><i>A lightweight, expressive scripting language powered by VoltVM</i></p>
</p>

<p align="center">
<img alt="Version" src="https://img.shields.io/badge/version-0.1.0-blue?style=flat-square" />
<img alt="Build" src="https://img.shields.io/badge/build-passing-brightgreen?style=flat-square" />
<img alt="License" src="https://img.shields.io/badge/license-MIT-yellow?style=flat-square" />
<img alt="Language" src="https://img.shields.io/badge/made_with-Watt-ff69b4?style=flat-square" />
</p>


# 🤔 About 
Watt is a dynamically typed scripting language that combines functional and object-oriented programming paradigms. ⚡
It is designed to be expressive, flexible, and easy to use for scripting and application development.

# ✨ Features

- 🔄 Dynamically typed
- 🧠 FP + OOP 
- 🪶 Clean syntax
- ⚙️ Compiled to bytecode (VoltVM)
- 🔍 Built-in reflection support
- 😋 Easy to learn

# 🚀 New 
Watt is compiled to its own virtual machine, VoltVM.
Volt VM brings:
- Great flexibility 🧩
- Good performance 🐇
- Nice reflection 🪞


# 💡 Examples

A few simple programs to show the expressive power of **Watt**.

> ✨ Watt files use the `.wt` extension.  
> 📂 More examples live in [`src/test/watt/examples`](src/test/watt/examples)

🪶 hello_world.wt
```kotlin
import 'std.io'
io.println('Hello, world!')
```

🪶 pie_recipe.wt
```kotlin
import 'std.io'
import 'std.convert'

type Pie(weight) {
    fun cook() {
        io.println('🥧 Cooking pie...')
        io.println('⚡ Pie cooked! Weight: '
                    + convert.to_string(weight)
        )
    }
}
unit Bakery {
    fun bake(pies) {
        io.println('🍪 Cooking: ')
        for i in 0..pies.size() {
            pies.get(i).cook()
        }
        io.println('🎉 Successfully cooked all pies!')
    }
}
pies := [new Pie(3.6)]
Bakery.bake(pies)
```

🪶 fibonacci.wt
```kotlin
fun fib(n) {
    cache := {}

    fun fib_inner(n) {
        if n <= 1 {
            return n
        }
        if cache.has_key(n) {
            return cache.get(n)
        }
        result := fib_inner(n - 1) + fib_inner(n - 2)
        cache.set(n, result)
        return result
    }
    return fib_inner(n)
}

io.println(fib(1000))
```

# 📚 Documentation
Work in progress... Stay tuned! 🛠️
📖 Official Docs (coming soon)

# ❤️ Thanks
Big thanks to all contributors and enthusiasts!
Feel free to ⭐️ star the project, give feedback, or contribute!