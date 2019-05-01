package application.command.guard.remove

/**
  *
  * @param guardName      name of the guard to remove
  * @param transitionName name of the transition where the guard is
  */
class RemoveGuardFromTransitionCommand(val guardName: String, val transitionName: String) {

}
