import 'std.time'
import 'ext.tui'
import 'ext.wfiglet'
import 'std.threads'

while true {
    wfiglet.println(
        'E:\wat_lang\wat\src\test\watt\examples\time\beer_pub.flf',
        time.format(time.now())
    )
    tui.clear()
    threads.sleep(1000)
}