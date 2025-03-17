import 'ext.tui'
import 'std.io'

window := new TuiWindow()
window.render_line('Hello world!')
window.render_line('key: ' + window.read_key().toString())
window.clear()