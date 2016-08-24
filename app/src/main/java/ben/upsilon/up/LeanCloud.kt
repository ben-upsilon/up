package ben.upsilon.up

import java.net.URL
import java.nio.charset.Charset

/**
 * 爬api坑
 *
 * Created by ben on 3/27/16.
 */
class LeanCloud {
    var baseURI: String = ""
    var baseHeader: Map<String, String> = hashMapOf()
    var baseKey: String = ""
    var baseID: String = ""

    companion object {
        var instance: LeanCloud = LeanCloud()
    }
}


val host: String = "https://api.leancloud.cn"
val version: String = "1.1"

enum class classes(val obj: String) {
    User("_user")
}

enum class Method(val value: String) {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
}

interface Handler {
    fun success(request: Request, response: Response)
    fun failure(request: Request, response: Response, error: Exception)
}

class Request {

    var timeoutInMillisecond = 1000

    lateinit var httpMethod: Method
    lateinit var path: String
    lateinit var url: URL
    var httpBody: ByteArray = ByteArray(0)

    var baseHeaders = hashMapOf<String, String>()

    fun header(_header: Map<String, Any>?): Request {
        _header?.forEach {
            it.let {
                if (!baseHeaders.containsKey(it.key)) {
                    baseHeaders.plusAssign(Pair(it.key, it.value.toString()))
                }
            }
        }
        return this
    }

    fun body(body: ByteArray): Request {
        httpBody = body
        return this
    }

    fun body(body: String, charset: Charset = Charsets.UTF_8): Request = body(body.toByteArray(charset))

    fun responseString(handler: () -> Unit) {

    }


    //string


}

class Response {
    lateinit var url: URL

    var httpStatusCode = -1
    var httpResponseMessage = ""
    var httpResponseHeaders = emptyMap<String, List<String>>()
    var httpContentLength = 0L

    var data = ByteArray(0)
}

fun LeanCloud.get(params: Map<String, Any>? = null): Request {
    params.toString()
    return Request()
}