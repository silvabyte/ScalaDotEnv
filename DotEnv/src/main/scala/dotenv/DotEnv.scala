package dotenv

import scala.io.Source

case class DotEnv(env: Map[String, String]) {
  var _env = env
  def get(key: String): Option[String] = {
    _env.get(key)
  }

  def getOrElse(key: String, default: String): String = {
    _env.getOrElse(key, default)
  }

  def set(key: String, value: String): Unit = {
    _env = _env + (key -> value)
  }
}

object DotEnv {

  /**
   * Load environment variables from a .env file into the system environment.
   *
   * @param filePath
   *   Path to the .env file. Defaults to ".env" in the current directory.
   * @param overrideExisting
   *   If true, existing environment variables will be overridden.
   * @return
   *   A map of all loaded key-value pairs.
   */
  def load(filePath: String = ".env", overrideExisting: Boolean = true): DotEnv = {
    val env = os.read.lines(os.Path(filePath)).map(parseLine).foldLeft(Map[String, String]()) {
      case (acc, Some((key, value))) =>
        if (overrideExisting || sys.env.get(key).isEmpty) {
          acc + (key -> value)
        } else {
          acc
        }
      case (acc, None) => acc
    }
    DotEnv(env.to(Map))
  }

  /**
   * Parse a line from the .env file into a key-value pair.
   *
   * @param line
   *   A line from the .env file.
   * @return
   *   An optional key-value pair.
   */
  private def parseLine(line: String): Option[(String, String)] = {
    val trimmed = line.trim
    if (trimmed.isEmpty || trimmed.startsWith("#")) {
      None // Skip empty lines and comments
    } else {
      val parts = trimmed.split("=", 2).map(_.trim)
      if (parts.length == 2) {
        Some(parts(0) -> unquote(parts(1)))
      } else {
        None // Skip malformed lines
      }
    }
  }

  /**
   * Remove surrounding quotes from a value, if present.
   *
   * @param value
   *   The value string.
   * @return
   *   The unquoted value.
   */
  private def unquote(value: String): String = {
    if (
      (value.startsWith("\"") && value.endsWith("\"")) ||
      (value.startsWith("'") && value.endsWith("'"))
    ) {
      value.substring(1, value.length - 1)
    } else {
      value
    }
  }
}
