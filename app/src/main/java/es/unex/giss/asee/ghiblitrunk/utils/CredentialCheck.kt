package es.unex.giss.asee.ghiblitrunk.utils

class CredentialCheck private constructor() {

    var fail: Boolean = false
    var msg: String = ""
    var error: CredentialError = CredentialError.PasswordError

   /* Clase que va a llevar a cabo el control de errores,
    para ello, cuenta con un campo fail, que estará a true
    si las credenciales introducidas incumplen alguna condición
    y a false si to-do es correcto. Por otro lado el campo
    "msg" cuenta con el comentario que se mostrará por pantalla,
    el cual puede ser de exito o de error. Por último, tenemos el
    campo error, de tipo Enum, en funcion del tipo de error.*/

    companion object{

        private val TAG = CredentialCheck::class.java.canonicalName
        private val MINCHARSname = 3
        private val MINCHARS = 4
        private val MINCHARSemail = 10

        /* La variable checks es de tipo array, y
        en cada posición del vector vamos a almacenar una
        configuración distinta de los atributos de la clase CredentialCheck.
        Para poder configurar estos atributos, va a ser necesario el uso
        de la función "apply", la cual nos va a permitir modificar el estado de los atributos
        de una clase.
         */

        private val checks = arrayOf(
            CredentialCheck().apply {
                fail = false
                msg = "Your credentials are OK"
                error = CredentialError.Success
            },
            CredentialCheck().apply {
                fail = true
                msg = "Invalid username"
                error = CredentialError.UsernameError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Invalid password"
                error = CredentialError.PasswordError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Passwords do not match"
                error = CredentialError.PasswordError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Password must have at least one number and upper letter"
                error = CredentialError.PasswordError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Invalid first name"
                error = CredentialError.NameError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Invalid second name"
                error = CredentialError.NameError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Invalid email"
                error = CredentialError.EmailError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Email and password doesn't match"
                error = CredentialError.LoginError
            }


        )

        fun login(username: String, password: String): CredentialCheck {
            return if (username.isBlank() || username.length < MINCHARS) checks[1]
            else if (password.isBlank() || password.length < MINCHARS) checks[2]
            else checks[0]
        }

        /*
        Las funciones de login y join hacen prácticamente lo mismo, solo que una se utiliza para
        el login y otra para el join. En este caso, son varios ifs anidados en los que se comprueba
        que el join y el login sean correctos. En función de esto, se retornará una posición diferente
        del vector definido previamente.
         */

        fun join(firstname: String, secondname: String, username: String,
                 email: String, password: String, repassword: String): CredentialCheck {
            return if (username.isBlank() || username.length < MINCHARS) checks[1]
            else if (password.isBlank() || password.length < MINCHARS) checks[2]
            else if (password!=repassword) checks[3]
            else if (!containsNumbersAndMayus(password)) checks[4]
            else if (firstname.isBlank() || firstname.length < MINCHARSname) checks[5]
            else if (secondname.isBlank() || secondname.length < MINCHARSname) checks[6]
            else if (email.isBlank() || email.length < MINCHARSemail) checks[7]
            else checks[0]
        }

        /*
        Devuelve True si el String contiene, al menos, un número y una mayúscula
         */
        fun containsNumbersAndMayus(texto: String): Boolean {
            val regex = Regex("[0-9]")
            return (texto.any { it.isUpperCase() } && regex.containsMatchIn(texto))
        }

        fun passwordOk(passText: String, password: String): CredentialCheck {
            return if (passText != password) checks[8]
            else checks[0]
        }


    }

    enum class CredentialError {
        PasswordError, NameError, EmailError, UsernameError, Success, LoginError
    }
}