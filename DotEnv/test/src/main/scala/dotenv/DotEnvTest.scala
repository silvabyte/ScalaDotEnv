package dotenv

import dotenv.{DotEnv}
import utest._

object DotEnvTest extends TestSuite {
  val envDir = sys.env.getOrElse("ENV_DIR", os.pwd)
  os.write.over(os.Path(s"$envDir/.env.test"), "TEST_VAR=test_value")
  val tests = this {
    test("DotEnv.load should load environment variables from a file") {
      val env = DotEnv.load(s"$envDir/.env.test")
      assert(env.get("TEST_VAR").contains("test_value"))
    }
  }
}
