package example.lang

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExtractorSuite extends FunSuite {

  object Header {
    def unapply(line: String): Option[(String, String)] = {
      val pos = line.indexOf(':')
      for {
        k <- Some(line.take(pos).trim)
        if (!k.isEmpty)
        v <- Some(line.drop(pos + 1).trim)
      } yield (k, v)
    }
  }

  test("Header") {
    "Content-Type: text/plain" match {
      case Header(k, v) =>
        assert(k === "Content-Type")
        assert(v === "text/plain")
      case _ =>
        fail()
    }

    Array(" : No Key", "No separator") foreach {
      case Header(k, v) =>
        fail()
      case _ =>
    }
  }

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

  test("Device") {
    Device("Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X)") match {
      case IPhone(_) =>
      case _ => fail()
    }
    Device("Mozilla/5.0 (iPad; CPU iPhone OS 6_0 like Mac OS X)") match {
      case IPad(_) =>
      case _ => fail()
    }
    Device("Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03S)") match {
      case Nexus(_) =>
      case _ => fail()
    }
    Device("Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)") match {
      case Unknown(_) =>
      case _ => fail()
    }
  }

  object TabletDevice {
    def unapply(device: Device): Boolean = {
      device match {
        case _@ IPad(_) | _@ Nexus(_) => true
        case _ => false
      }
    }
  }

  test("TabletDevice") {
    Array(
      Device("Mozilla/5.0 (iPad; CPU iPhone OS 6_0 like Mac OS X)"),
      Device("Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03S)")) foreach {
        case TabletDevice() =>
        case _ => fail()
      }

    Array(
      Device("Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X)")) foreach {
        case TabletDevice() => fail()
        case _ =>
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
        case _@ IPhone(_) | _@ IPad(_) => Some(Developer.Apple)
        case _@ Nexus(_) => Some(Developer.Google)
        case _ => None
      }
    }
  }

  test("DeviceDeveloper") {
    Array(
      Device("Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X)"),
      Device("Mozilla/5.0 (iPad; CPU iPhone OS 6_0 like Mac OS X)")) foreach {
        case DeviceDeveloper(Developer.Apple) =>
        case _ => fail()
      }

    Array(
      Device("Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03S)")) foreach {
        case DeviceDeveloper(Developer.Google) =>
        case _ => fail()
      }

    Array(
      Device("Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)")) foreach {
        case DeviceDeveloper(_) => fail()
        case _ =>
      }
  }
}
