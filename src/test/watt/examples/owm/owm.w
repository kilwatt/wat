import 'net.owm'

owm := new OwmClient('someowmapikey')
weather := owm.city('Moscow', 'ru', 'json')
io.println(weather)