import 'net.owm'

owm := new OwmClient('someowmapikey')
weather := owm.city('Tashkent', 'ru', 'xml')
io.println(weather)