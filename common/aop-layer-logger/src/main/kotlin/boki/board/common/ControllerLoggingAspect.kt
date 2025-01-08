package boki.board.common

import jakarta.servlet.http.HttpServletRequest
import java.util.*
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import kotlin.system.measureTimeMillis

@Aspect
@Component
class ControllerLoggingAspect {

    private val log = LoggerFactory.getLogger(ControllerLoggingAspect::class.java)

    @Pointcut("execution(* boki.board..*Controller.*(..))")
    fun loggerPointCut() {}

    @Around("loggerPointCut()")
    @Throws(Throwable::class)
    fun methodLogger(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val controllerName = proceedingJoinPoint.signature.declaringType.simpleName
        val methodName = proceedingJoinPoint.signature.name

        log.debug("Controller execution start")
        val result: Any
        val executionTime = measureTimeMillis {
            result = proceedingJoinPoint.proceed()
        }

        val params = mutableMapOf<String, Any?>()
        try {
            params["controller"] = controllerName
            params["method"] = methodName
            params["params"] = getParams(request)
            params["log_time"] = Date()
            params["request_uri"] = request.requestURI
            params["http_method"] = request.method
            params["execution_time"] = "$executionTime ms"
        } catch (e: Exception) {
            log.error("LoggingAspect error", e)
        }

        log.debug("Controller execution ends: {}", params)
        return result
    }

    companion object {
        private fun getParams(request: HttpServletRequest): Map<String, String?> {
            return request.parameterNames.asSequence().associateBy(
                { it.replace(".", "-") },
                { request.getParameter(it) }
            )
        }
    }
}
