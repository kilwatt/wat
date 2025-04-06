# Watt 💡

Watt is a dynamically typed scripting language that combines functional and object-oriented programming paradigms. ⚡
It is designed to be expressive, flexible, and easy to use for scripting and application development.

# New 🚀
Watt is compiled to its own virtual machine, VoltVM.
VoltVM offering great flexibility for Watt development, and, of course,
great reflection.

# Examples 🔋
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
        for i in 0 to pies.size() {
            pies.get(i).cook()
        }
        io.println('🎉 Successfully cooked all pies!')
    }
}
pies := [new Pie(3.6)]
Bakery.bake(pies)
```