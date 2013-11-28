package example.extractor

object Main extends App {
  for (
    str <- Array(
      "Content-Type: text/plain",
      " : No Key",
      "No separator"
    )
  ) str match {
    case Header(k, v) => println("name=" + k + ", value=" + v)
    case _ =>
  }

  val devices = List(
    Device("Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X)")
  , Device("Mozilla/5.0 (iPad; CPU iPhone OS 6_0 like Mac OS X)")
  , Device("Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03S)")
  , Device("Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)")
  )
  for (d <- devices) {
    d match {
      case DeviceDeveloper(developer) => println(d + " is developed by " + developer.name + ".")
      case _ => println(d + " is developed by unknown developer.")
    }
    d match {
      case TabletDevice() => println(d + " is a tablet device.")
      case _ => println(d + " is not a tablet device.")
    }
  }
}
