package infrastructure.controller

import application.command.body.modify.{ModifyBodyContentCommand, ModifyBodyNameCommand, ModifyBodyTypeCommand}
import application.commandhandler.body.modify.{ModifyBodyContentHandler, ModifyBodyNameHandler, ModifyBodyTypeHandler}
import infrastructure.EnvironmentSingleton
import infrastructure.element.body.Body
import infrastructure.element.body.BodyType.BodyType

/**
  * Controls the visual and behavior aspects of a body
  *
  * @param body body to control
  */
class BodyController(body: Body) {
  private val propertiesBox = body.propertiesBox

  propertiesBox.setOnBodyContentChanged(newContent => BodyController.modifyBodyContent(body, newContent))
  propertiesBox.setOnBodyNameChanged(newName => BodyController.modifyBodyName(body, newName))
  propertiesBox.setOnBodyTypeChanged(newBodyType => BodyController.modifyBodyType(body, newBodyType))
}

/**
  * Operations that can be done with a body
  */
object BodyController {
  private val environment = EnvironmentSingleton.get()

  /**
    * Modifies the content of a body
    *
    * @param body       body to be modified
    * @param newContent new content
    */
  def modifyBodyContent(body: Body, newContent: String): Unit = {
    new ModifyBodyContentHandler(environment).execute(new ModifyBodyContentCommand(body.name, newContent)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        body.content = newContent

        println("Body content changed to -> " + newContent)
    }
  }

  /**
    * Modifies the name of a body
    *
    * @param body    body to be modified
    * @param newName new name
    */
  def modifyBodyName(body: Body, newName: String): Unit = {
    new ModifyBodyNameHandler(environment).execute(new ModifyBodyNameCommand(body.name, newName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        body.name = newName

        println("Body content changed to -> " + newName)
    }
  }

  /**
    * Modifies the type of a body
    *
    * @param body        body to be modified
    * @param newBodyType new body type
    */
  def modifyBodyType(body: Body, newBodyType: BodyType): Unit = {
    new ModifyBodyTypeHandler(environment).execute(new ModifyBodyTypeCommand(body.name, newBodyType match {
      case infrastructure.element.body.BodyType.RDF => application.command.body.modify.BodyType.RDF
      case infrastructure.element.body.BodyType.JSON => application.command.body.modify.BodyType.JSON
      case infrastructure.element.body.BodyType.SPARQL => application.command.body.modify.BodyType.SPARQL
    })) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        body.bodyType = newBodyType

        println("Body type changed to -> " + newBodyType)
    }
  }

  /**
    * Draws a body in the application
    *
    * @param body body to be drawn
    */
  def drawBody(body: Body): Unit = {
    body.propertiesBox.setBodyName(body.name)
    body.propertiesBox.setBodyType(body.bodyType)
    body.propertiesBox.setBodyContent(body.content)

    new BodyController(body)
  }
}
