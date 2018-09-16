package right.apps.fire.konfig

import com.google.firebase.FirebaseException

class FireKonfigException(msg: String) : FirebaseException(msg)

fun NoSuchParameterException(parameter: String) = FireKonfigException("No parameter $parameter in Remote Config, check your config")