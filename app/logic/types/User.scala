package logic.types

case class Role(id: Option[Long], appName: String, roleName: String, userId: Long)
case class User(id: Option[Long], userName: String, email: String)
case class Passwd(id: Option[Long], passwdHash: String, userId: Long)
case class Registration(id: Option[Long], status: Boolean, token: String, tokenDate: String, userId: Long)
