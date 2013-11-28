package example.extractor

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
