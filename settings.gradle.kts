rootProject.name = "boki-board"

include("common")
include("common:snowflake")
include("common:rest-exception-handler")
include("common:aop-layer-logger")

include("service")
include("service:article")
include("service:comment")
include("service:view")
include("service:like")
include("service:hot-article")
include("service:article-read")