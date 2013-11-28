package example.extractor

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

sealed abstract class Developer(val name: String)
object Developer {
  case object Apple extends Developer("Apple Inc.")
  case object Google extends Developer("Google Inc.")
}

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
