import 'std.typeof'

unit math_ext {
    // константы
    pi := 3.141592
    e := 2.718281
    tau := 6.283185

    // рефлекися
    math_reflection := __refl__.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Std.Math.StdMathExt',
        []
    )

    // функции
    fun sin(x) {
        return math_reflection.sin(x)
    }

    fun cos(x) {
        return math_reflection.cos(x)
    }

    fun tan(x) {
        return math_reflection.tan(x)
    }

    fun asin(x) {
        return math_reflection.asin(x)
    }

    fun acos(x) {
        return math_reflection.acos(x)
    }

    fun atan(x) {
        return math_reflection.atan(x)
    }
}

// пример:
// [0, 1, 2] row row
// [3, 4, 5]
// [6, 7, 8]
type Matrix(fill) {
    // список
    arr := []

    // инициализация
    fun init() {
        // проверка типов
        if (typeof(fill) != 'WattList') {
            error(
                'matrix fill must be a list, not ' + typeof(fill),
                'check for types.'
            )
        }

        // создаём массив
        for y in 0 to fill.size() {
            row := []
            for x in 0 to fill.size() {
                row.add(fill.get(y).get(x))
            }
            arr.add(row)
        }
    }

    // изменение позиции
    fun set(x, y, value) {
        // проверка типов
        if (typeof(x) != 'int') {
            error(
                'matrix x amount must be an int, not ' + typeof(x),
                'check for types.'
            )
        }
        if (typeof(y) != 'int') {
            error(
                'matrix y amount must be an int, not ' + typeof(y),
                'check for types.'
            )
        }
        // изменение
        arr.get(y).set(x, value)
    }

    // сложение матриц
    fun add(matrix) {
        // проверка типов
        if (typeof(matrix) != 'Matrix') {
            error(
                'to add matrix, value must be an Matrix, not ' + typeof(matrix),
                'check for types.'
            )
        }
        // проверка размеров
        if (matrix.arr.size() != arr.size()) {
            error(
                'could not add matrix with different size',
                'check for sizes.'
            )
        }
        // новая матрица
        new_matrix := new Matrix(self.arr)
        // складываем
        for y in 0 to arr.size() {
            for x in 0 to arr.size() {
                old_value := new_matrix.arr.get(y).get(x)
                new_matrix.arr.get(y).set(x, old_value+matrix.arr.get(y).get(x))
            }
        }
        // возвращаем
        return new_matrix
    }

    // вычитание матриц
    fun sub(matrix) {
        // проверка типов
        if (typeof(matrix) != 'Matrix') {
            error(
                'to sub matrix, value must be an Matrix, not ' + typeof(matrix),
                'check for types.'
            )
        }
        // проверка размеров
        if (matrix.arr.size() != arr.size()) {
            error(
                'could not sub matrix with different size',
                'check for sizes.'
            )
        }
        // новая матрица
        new_matrix := new Matrix(self.arr)
        // складываем
        for y in 0 to arr.size() {
            for x in 0 to arr.size() {
                old_value := new_matrix.arr.get(y).get(x)
                new_matrix.arr.get(y).set(x, old_value-matrix.arr.get(y).get(x))
            }
        }
        // возвращаем
        return new_matrix
    }

    // умножение матриц
    fun mul(matrix) {
        // проверка типов
        if (typeof(matrix) != 'Matrix') {
            error(
                'to mul matrix, value must be an Matrix, not ' + typeof(matrix),
                'check for types.'
            )
        }
        // проверка размеров
        if (matrix.arr.size() != arr.size()) {
            error(
                'could not mul matrix with different size',
                'check for sizes.'
            )
        }
        // новая матрица
        new_matrix := new Matrix(self.arr)
        // складываем
        for y in 0 to arr.size() {
            for x in 0 to arr.size() {
                old_value := new_matrix.arr.get(y).get(x)
                new_matrix.arr.get(y).set(x, old_value*matrix.arr.get(y).get(x))
            }
        }
        // возвращаем
        return new_matrix
    }

    // деление матриц
    fun div(matrix) {
        // проверка типов
        if (typeof(matrix) != 'Matrix') {
            error(
                'to div matrix, value must be an Matrix, not ' + typeof(matrix),
                'check for types.'
            )
        }
        // проверка размеров
        if (matrix.arr.size() != arr.size()) {
            error(
                'could not div matrix with different size',
                'check for sizes.'
            )
        }
        // новая матрица
        new_matrix := new Matrix(self.arr)
        // складываем
        for y in 0 to arr.size() {
            for x in 0 to arr.size() {
                old_value := new_matrix.arr.get(y).get(x)
                new_matrix.arr.get(y).set(x, old_value/matrix.arr.get(y).get(x))
            }
        }
        // возвращаем
        return new_matrix
    }

    // в строку
    fun to_string() {
        str := '['

        for elem in 0 to arr.size() {
            str += '[' + arr.get(elem).stringify() + ']'
        }

        str += ']'

        return str
    }
}

type Vec3(x,y,z) {
    fun init() {
        // функция проверки типа
        fun check_type(name, val) {
            if (val != 'int' and val != 'float' and val != 'y_type') {
                error(
                    'to create vector, value: ' + name + ' must be a number, not ' + typeof(matrix),
                    'check for types.'
                )
            }
        }
        // проверка типов
        check_type('x', typeof(x))
        check_type('y', typeof(y))
        check_type('z', typeof(z))
    }
    fun add(vec) {
        if (typeof(vec) != 'Vec3') {
            error (
                'could not add ' + vec_type + ' to Vec3',
                'check for types.'
            )
        }
        x += vec.x
        y += vec.y
        z += vec.z
    }
    fun sub(vec) {
        if (typeof(vec) != 'Vec3') {
            error (
                'could not sub ' + vec_type + ' to Vec3',
                'check for types.'
            )
        }
        x -= vec.x
        y -= vec.y
        z -= vec.z
    }
    fun mul(vec) {
        if (typeof(vec) != 'Vec3') {
            error (
                'could not mul ' + vec_type + ' to Vec3',
                'check for types.'
            )
        }
        x *= vec.x
        y *= vec.y
        z *= vec.z
    }
    fun div(vec) {
        if (typeof(vec) != 'Vec3') {
            error (
                'could not div( ' + vec_type + ' to Vec3',
                'check for types.'
            )
        }
        x /= vec.x
        y /= vec.y
        z /= vec.z
    }
    fun to_string() {
        return '(' + 'x: ' + x + ', y: ' + y + ', z: ' + z + ')'
    }
}

type Vec2(x,y) {
    fun init() {
        // функция проверки типа
        fun check_type(name, val) {
            if (val != 'int' and val != 'float' and val != 'y_type') {
                error(
                    'to create vector, value: ' + name + ' must be a number, not ' + typeof(matrix),
                    'check for types.'
                )
            }
        }
        // проверка типов
        check_type('x', typeof(x))
        check_type('y', typeof(y))
    }
    fun add(vec) {
        if (typeof(vec) != 'Vec2') {
            error (
                'could not add ' + vec_type + ' to Vec2',
                'check for types.'
            )
        }
        x += vec.x
        y += vec.y
    }
    fun sub(vec) {
        if (typeof(vec) != 'Vec2') {
            error (
                'could not sub ' + vec_type + ' to Vec2',
                'check for types.'
            )
        }
        x -= vec.x
        y -= vec.y
    }
    fun mul(vec) {
        if (typeof(vec) != 'Vec2') {
            error (
                'could not mul ' + vec_type + ' to Vec2',
                'check for types.'
            )
        }
        x *= vec.x
        y *= vec.y
    }
    fun div(vec) {
        if (typeof(vec) != 'Vec2') {
            error (
                'could not div( ' + vec_type + ' to Vec2',
                'check for types.'
            )
        }
        x /= vec.x
        y /= vec.y
    }
    fun to_string() {
        return '(' + 'x: ' + x + ', y: ' + y + ')'
    }
}