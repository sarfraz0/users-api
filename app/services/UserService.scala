package services

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.Future
import logic.types.User
import models.UserModel

@Singleton
class UserService @Inject() (userModel: UserModel) {

  def addUser(user: User): Future[String] = userModel.add(user)

  def deleteUser(id: Long): Future[Int] = userModel.delete(id)

  def getUser(id: Long): Future[Option[User]] = userModel.get(id)

  def getUser(name: String): Future[Option[User]] = userModel.get(name)

  def listAllUsers: Future[Seq[User]] = userModel.listAll
}
