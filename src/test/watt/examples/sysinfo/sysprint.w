import 'syscolors.w'
import 'ext.tui'
import 'std.io'

type Label(key, value) -> {
    fun print(window) -> {
        window.render_line('· ' + key + ': ' + value)
    }
}

type Title(title, labels) -> {
    fun print(window) -> {
        window.render_line(colors.green + title + colors.reset)
        for i in 0 to labels.size() {
            labels.get(i).print(window)
        }
    }
}

type Printer() -> {
    window := new TuiWindow()
    last_title := null

    fun refresh(titles) -> {
        window.clear()
        for i in 0 to titles.size() {
            titles.get(i).print(window)
        }
    }
}