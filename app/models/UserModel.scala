package models

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.db.slick.DatabaseConfigProvider
import play.api.data._
import play.api.data.Forms._
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import logic.Constants
import logic.types.User

/*
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_name TEXT NOT NULL,
    email TEXT NOT NULL
);
*/

class UserTable(tag: Tag) extends Table[User](tag, "users") {

  def id = column[Long](Constants.idKey, O.PrimaryKey,O.AutoInc)
  def userName = column[String](Constants.userNameKey)
  def email = column[String](Constants.emailKey)

  override def * = (id.?, userName, email) <> (User.tupled, User.unapply)
}

object UserForm {

  val form = Form(
    mapping(
      Constants.idKey -> optional(longNumber),
      Constants.userNameKey -> nonEmptyText,
      Constants.emailKey -> email
    )(User.apply)(User.unapply)
  )
}

@Singleton
class UserModel @Inject() (dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  val users = TableQuery[UserTable]

  def add(user: User): Future[String] = {
    dbConfig.db.run(users += user).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(users.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[User]] = {
    dbConfig.db.run(users.filter(_.id === id).result.headOption)
  }

  def get(name: String): Future[Option[User]] = {
    dbConfig.db.run(users.filter(_.userName === name).result.headOption)
  }

  def listAll: Future[Seq[User]] = {
    dbConfig.db.run(users.result)
  }
}
