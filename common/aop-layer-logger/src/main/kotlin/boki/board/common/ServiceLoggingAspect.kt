package boki.board.common

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.Date
import kotlin.system.measureTimeMillis

@Aspect
@Component
class ServiceLoggingAspect {

    private val log = LoggerFactory.getLogger(ServiceLoggingAspect::class.java)

    @Pointcut("execution(* boki.board..*Service.*(..))")
    fun loggerPointCut() {}

    @Around("loggerPointCut()")
    @Throws(Throwable::class)
    fun methodLogger(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val serviceName = proceedingJoinPoint.signature.declaringType.simpleName
        val methodName = proceedingJoinPoint.signature.name

        log.debug("  Service execution start")
        val result: Any
        val executionTime = measureTimeMillis {
            result = proceedingJoinPoint.proceed()
        }

        val params: MutableMap<String, Any> = HashMap()
        try {
            params["service"] = serviceName
            params["method"] = methodName
            params["params"] = proceedingJoinPoint.args.contentToString()
            params["log_time"] = Date()
            params["execution_time"] = "$executionTime ms"
        } catch (e: Exception) {
            log.error("LoggingAspect error", e)
        }

        log.debug("  Service execution ends: {}", params)
        return result
    }
}