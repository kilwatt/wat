import 'net.mail'
import 'std.io'

io.println('creating connection with smtp...')
client := new SmtpClient('some_mail_here', 'some_application_password_here')
io.println('sending mail...')
client.send_mail('some_recipient@gmail.com', 'some test', 'test message')
io.println('success!')
client.close()
io.println('connection closed.')