package example.device

object Main extends App {
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

// Device
sealed abstract class Device(val userAgent: String)
case class Unknown(override val userAgent: String) extends Device(userAgent)
case class IPhone(override val userAgent: String) extends Device(userAgent)
case class IPad(override val userAgent: String) extends Device(userAgent)
case class Nexus(override val userAgent: String) extends Device(userAgent)

object Device {
  def apply(ua: String): Device = {
    val ios = """^Mozilla/[^ ]+ \((iPhone|iPod|iPad);.+$""".r
    val android = """^Mozilla/[^ ]+ \(Linux;(?: U;)? Android (?:[^ ]+);(?: ja-jp;)? ([^ ]+).+$""".r
    ua match {
      case ios(nm) => nm match {
        case "iPhone" => IPhone(ua)
        case "iPad" => IPad(ua)
      }
      case android(nm) => nm match {
        case "Nexus" => Nexus(ua)
        case _ => Unknown(ua)
      }
      case _ => Unknown(ua)
    }
  }
}

// Developer (like enum)
sealed abstract class Developer(val name: String)
object Developer {
  case object Apple extends Developer("Apple Inc.")
  case object Google extends Developer("Google Inc.")
}

// Extractors
object DeviceDeveloper {
  def unapply(device: Device): Option[Developer] = {
    device match {
      case _ @ IPhone(_) | _ @ IPad(_)
        => Some(Developer.Apple)
      case _ @ Nexus(_)
        => Some(Developer.Google)
      case _ => None
    }
  }
}

object TabletDevice {
  def unapply(device: Device): Boolean = {
    device match {
      case _ @ IPad(_) | _ @ Nexus(_) => true
      case _ => false
    }
  }
}
