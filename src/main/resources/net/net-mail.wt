type SmtpClient(login, password) {
    // рефлексия
    smtp_reflection := __refl__.reflect(
        'com.kilowatt.Compiler.Builtins.Libraries.Net.NetMail',
        []
    )
    // сессия
    session := smtp_reflection.create_session(login, password)
    // соеденение
    connection := smtp_reflection.connect(session)

    // отправка письма
    fun send_mail(_to, subject, text) {
        smtp_reflection.send_mail(session, connection, login, _to, subject, text)
    }

    // закрытие соеденения
    fun close {
        connection.close()
    }
}