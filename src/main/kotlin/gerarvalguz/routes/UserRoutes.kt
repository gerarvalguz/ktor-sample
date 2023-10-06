package gerarvalguz.routes

import gerarvalguz.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val users = mutableListOf(
    User(1,"Ivy Valguz",19, "ivy@email.com"),
    User(2,"Angel Valguz",29, "angel@email.com"),
    User(3,"Gera Valguz",43, "gera@email.com"),
)


fun Route.userRouting(){
    route("/user"){
        get{
            if(users.isNotEmpty()){
                call.respond(users)
            }else{
                call.respondText("User list is empty", status= HttpStatusCode.OK)
            }
        }
        get("{id}"){
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Id not found",
                status = HttpStatusCode.BadRequest
            )

            if( id.toIntOrNull() == null ){
                return@get call.respondText(
                    "Invalid Id",
                    status = HttpStatusCode.BadRequest
                )
            }

            val user = users.find { it.id == id.toInt() } ?: return@get call.respondText(
                "User not found",
                status = HttpStatusCode.NotFound
            )
            call.respond(user)
        }
        post {
            val user = call.receive<User>()
            users.add(user)
            call.respondText( "User added correctly", status = HttpStatusCode.Created )
        }
        delete ( "{id}" ){
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Id not found",
                status = HttpStatusCode.BadRequest
            )

            if( id.toIntOrNull() == null ){
                return@delete call.respondText(
                    "Invalid Id",
                    status = HttpStatusCode.BadRequest
                )
            }

            if(users.removeIf { it.id == id.toInt() }){
                call.respondText("User removed successfully", status = HttpStatusCode.Accepted)
            }else{
                call.respondText( "User not found", status = HttpStatusCode.NotFound)
            }

        }
    }
}