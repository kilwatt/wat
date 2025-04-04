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