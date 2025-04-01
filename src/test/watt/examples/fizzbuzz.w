import 'std.io'

fun fizz_buzz(n) {
    result := []
    for i in 1 to n + 1 {
        if i % 3 == 0 and i % 5 == 0 {
            result.add('FizzBuzz')
        }
        elif i % 3 == 0 {
            result.add('Fizz')
        }
        elif i % 5 == 0 {
            result.add('Buzz')
        }
        else {
            result.add(i)
        }
    }
    return result
}

result := fizz_buzz(10)
io.println(result.stringify())