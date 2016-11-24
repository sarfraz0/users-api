package controllers

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import logic.types.User
import logic.Constants
import models.UserForm
import play.Logger
import services.UserService

import scala.concurrent.Future

class UsersController @Inject() (userService: UserService) extends Controller {

  def get = Action.async {
    implicit request =>
      userService.listAllUsers map {
        users => {
          implicit val userWrites: Writes[User] = (
            (JsPath \ Constants.idKey).writeNullable[Long] and
            (JsPath \ Constants.userNameKey).write[String] and
            (JsPath \ Constants.emailKey).write[String]
          )(unlift(User.unapply))
          Ok(Json.toJson(users))
        }
      }
  }

  def post() = Action.async {
    implicit request => {
      UserForm.form.bindFromRequest.fold(
        formWithErrors => {
          // binding failure, you retrieve the form containing errors:
          Future.successful(BadRequest("wtf"))
        },
        userData => {
          /* binding success, you get the actual value. */
          userService.addUser(userData).map {
            id => {
              Ok(id)
            }
          }
        }
      )
    }
  }

  def delete(username: String) = Action { request =>
    Ok("")
  }

}
