import 'std.refl'
import 'std.io'

type WattReflectionTestClass(id) -> {
    _required := 3
    fun example(required) -> {
        assert(id == required)
    }
}

_instance := reflect.instance(
    reflect.lookup_type('WattReflectionTestClass'),
    [3]
)
reflect.call_function(_instance, 'example', [reflect.get_field(_instance, '_required')])