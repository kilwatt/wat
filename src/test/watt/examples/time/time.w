import 'std.time'
import 'ext.tui'
import 'ext.wfiglet'

window := new TuiWindow()
while true {
    wfiglet.println(
        'E:\wat_lang\wat\src\test\watt\examples\time\beer_pub.flf',
        time.format(time.now())
    )
    window.clear()
}